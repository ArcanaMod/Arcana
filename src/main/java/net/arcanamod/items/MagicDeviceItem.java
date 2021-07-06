package net.arcanamod.items;

import com.google.common.collect.Sets;
import net.arcanamod.ArcanaSounds;
import net.arcanamod.aspects.*;
import net.arcanamod.client.render.particles.AspectHelixParticleData;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.Focus;
import net.arcanamod.items.attachment.FocusItem;
import net.arcanamod.systems.spell.MDModifier;
import net.arcanamod.systems.spell.Spell;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.vis.VisUtils;
import net.arcanamod.world.AuraView;
import net.arcanamod.world.Node;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class MagicDeviceItem extends Item{
	public MagicDeviceItem(Properties properties){
		super(properties);
	}
	
	public abstract boolean canCraft();
	
	public abstract boolean canUseSpells();
	
	public abstract String getDeviceName();
	
	public static Cap getCap(ItemStack stack){
		String cap = stack.getOrCreateTag().getString("cap");
		return Cap.getCapOrError(new ResourceLocation(cap));
	}
	
	public static Focus getFocus(ItemStack stack){
		int focus = stack.getOrCreateTag().getInt("focus");
		return Focus.getFocusById(focus).orElse(Focus.NO_FOCUS);
	}
	
	public static Core getCore(ItemStack stack){
		String core = stack.getOrCreateTag().getString("core");
		return Core.getCoreOrError(new ResourceLocation(core));
	}
	
	public static CompoundNBT getFocusData(ItemStack stack){
		return stack.getOrCreateChildTag("focusData");
	}
	
	public static Optional<ItemStack> getFocusStack(ItemStack stack){
		return getFocus(stack).getAssociatedItem().map(ItemStack::new).map(stack1 -> {
			stack1.setTag(getFocusData(stack));
			return stack1;
		});
	}
	
	public static void setFocusFromStack(ItemStack wand, ItemStack focus){
		if(focus.getItem() instanceof FocusItem){
			wand.getOrCreateTag().put("focusData", focus.getOrCreateTag());
			wand.getOrCreateTag().putInt("focus", Focus.FOCI.indexOf(focus.getItem()));
		}else{
			wand.getOrCreateTag().put("focusData", new CompoundNBT());
			wand.getOrCreateTag().putInt("focus", 0);
		}
	}
	
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt){
		AspectBattery battery = new AspectBattery(6, 0);
		for(Aspect aspect : AspectUtils.primalAspects)
			battery.createCell(new AspectCell((int)((getCore(stack).maxVis() + getCap(stack).visStorage()) * getVisModifier()), aspect));
		return battery;
	}
	
	protected abstract float getVisModifier();
	
	protected abstract float getDifficultyModifier();
	
	protected abstract float getComplexityModifier();
	
	public void onUsingTick(ItemStack stack, LivingEntity player, int count){
		World world = player.world;
		AuraView view = AuraView.SIDED_FACTORY.apply(world);
		Optional<Node> nodeOptional = view.raycast(player.getEyePosition(0), player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue(), player);
		if(nodeOptional.isPresent()){
			final int ASPECT_DRAIN_AMOUNT = 2;
			final int ASPECT_DRAIN_WAIT = 3;
			// drain
			IAspectHandler wandHolder = IAspectHandler.getFrom(stack);
			// TODO: non-destructive node draining?
			// with research, of course
			if(wandHolder != null)
				if(world.getGameTime() % (ASPECT_DRAIN_WAIT + 1 + world.rand.nextInt(3)) == 0){
					Node node = nodeOptional.get();
					IAspectHandler aspects = node.getAspects();
					IAspectHolder holder = aspects.getHolder(world.rand.nextInt(aspects.getHoldersAmount()));
					Aspect aspect = holder.getContainedAspect();
					boolean moved = holder.getCurrentVis() > 0;
					VisUtils.moveAspects(holder, wandHolder, ASPECT_DRAIN_AMOUNT + world.rand.nextInt(1));
					if(moved){
						// spawn aspect helix particles
						Vector3d nodePos = new Vector3d(node.getX(), node.getY(), node.getZ());
						Vector3d playerPos = player.getEyePosition(1);
						Vector3d diff = nodePos.subtract(playerPos);
						Vector3d direction = diff.normalize().mul(-1, -1, -1);
						int life = (int)Math.ceil(diff.length() / .05f);
						world.addParticle(new AspectHelixParticleData(aspect, life, world.rand.nextInt(180), direction), nodePos.x, nodePos.y, nodePos.z, 0, 0, 0);
					}
				}
		}else
			player.stopActiveHand();
		//world.addParticle(new AspectHelixParticleData(Aspects.EXCHANGE, 450, world.rand.nextInt(180), player.getLookVec()), player.getPosX(), player.getPosYEye(), player.getPosZ(), 0, 0, 0);
	}
	
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand){
		// TODO: only do this if you're casting a spell
		// first do node raycast check, and then check if you have a focus
		if(canUseSpells()){
			ArcanaSounds.playSpellCastSound(player);
			Focus focus = getFocus(player.getHeldItem(hand));
			if(focus != Focus.NO_FOCUS){
				Spell spell = focus.getSpell(player.getHeldItem(hand));
				if(spell != null && spell.mainModule != null){
					IAspectHandler handler = IAspectHandler.getFrom(player.getHeldItem(hand));
					// oh my god this code is terrible // YES, I know Xd.
					// time for more VisUtils I guess
					if(spell.getSpellCosts().toList().stream().allMatch(stack -> findAspectInHoldersOrEmpty(handler, stack.getAspect()).getCurrentVis() >= stack.getAmount()) ||
							spell.getSpellCosts().toList().stream().allMatch(stack -> stack.getAspect() == Aspects.EMPTY)){
						Spell.runSpell(spell, world, player, player.getHeldItem(hand), player.isCrouching() ? ICast.Action.SPECIAL : ICast.Action.USE);
						// remove aspects from wand if spell successes.
						for(AspectStack cost : spell.getSpellCosts().toList())
							if(cost.getAspect() != Aspects.EMPTY)
								handler.findAspectInHolders(cost.getAspect()).drain(cost, false);
					}
				}else
					player.sendStatusMessage(new TranslationTextComponent("status.arcana.null_spell"), true);
			}
		}
		AuraView view = AuraView.SIDED_FACTORY.apply(world);
		ItemStack itemstack = player.getHeldItem(hand);
		AtomicReference<ActionResult<ItemStack>> ret = new AtomicReference<>(ActionResult.resultConsume(itemstack));
		view.raycast(player.getEyePosition(0), player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue(), player).ifPresent(node -> {
			player.setActiveHand(hand);
			ret.set(ActionResult.resultConsume(itemstack));
		});
		return ret.get();
	}
	
	private IAspectHolder findAspectInHoldersOrEmpty(IAspectHandler handler, Aspect aspect){
		@Nullable IAspectHolder nullableHolder = handler.findAspectInHolders(aspect);
		return nullableHolder != null ? nullableHolder : new AspectCell();
	}
	
	@Nonnull
	public ITextComponent getDisplayName(@Nonnull ItemStack stack){
		return new TranslationTextComponent(getCore(stack).getCoreTranslationKey(), new TranslationTextComponent(getCap(stack).getPrefixTranslationKey()), new TranslationTextComponent(getDeviceName()));
	}
	
	@OnlyIn(Dist.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag){
		super.addInformation(stack, world, tooltip, flag);
		// Add info
		boolean creative = getCore(stack).modifier() instanceof MDModifier.Creative;
		if(creative)
			tooltip.add(new TranslationTextComponent("tooltip.arcana.creative_wand").mergeStyle(TextFormatting.AQUA));
		tooltip.add(new StringTextComponent(""));
		tooltip.add(new TranslationTextComponent("tooltip.arcana.properties").mergeStyle(TextFormatting.GRAY));
		tooltip.add(new StringTextComponent(" " + (creative ? I18n.format("tooltip.arcana.infinity") : (int)((getCore(stack).maxVis() + getCap(stack).visStorage()) * getVisModifier()) + " "+I18n.format("tooltip.arcana.max")) + " "+I18n.format("tooltip.arcana.vis")).mergeStyle(TextFormatting.DARK_GREEN));
		tooltip.add(new StringTextComponent(" " + (creative ? I18n.format("tooltip.arcana.infinity") : (int)(getCore(stack).difficulty() * getDifficultyModifier())) + " "+I18n.format("tooltip.arcana.difficulty")).mergeStyle(TextFormatting.DARK_GREEN));
		tooltip.add(new StringTextComponent(" " + (creative ? I18n.format("tooltip.arcana.infinity") : (int)(getCap(stack).complexity() * getComplexityModifier())) + " "+I18n.format("tooltip.arcana.complexity")).mergeStyle(TextFormatting.DARK_GREEN));
	}
	
	public boolean canSwapFocus(PlayerEntity player){
		return player.inventory.hasAny(Sets.newHashSet(ArcanaItems.DEFAULT_FOCUS.get())) || (getFocus(player.getHeldItem(Hand.MAIN_HAND)) != Focus.NO_FOCUS||getFocus(player.getHeldItem(Hand.OFF_HAND)) != Focus.NO_FOCUS);
	}
}