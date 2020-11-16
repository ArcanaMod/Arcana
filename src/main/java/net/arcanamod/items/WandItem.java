package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaSounds;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.CrucibleBlock;
import net.arcanamod.client.render.aspects.AspectHelixParticleData;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.Focus;
import net.arcanamod.items.attachment.FocusItem;
import net.arcanamod.systems.spell.Spell;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.core.CastCircle;
import net.arcanamod.systems.spell.modules.core.CastMethod;
import net.arcanamod.util.Pair;
import net.arcanamod.util.VisUtils;
import net.arcanamod.world.AuraView;
import net.arcanamod.world.Node;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static net.arcanamod.systems.spell.Spell.Samples.createBasicSpell;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WandItem extends Item{
	
	public WandItem(Properties properties){
		super(properties);
	}
	
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
			battery.createCell(new AspectCell(getCore(stack).maxVis(), aspect));
		return battery;
	}
	
	public ActionResultType onItemUse(ItemUseContext context){
		return convert(context.getWorld(), context.getPos(), context.getPlayer());
	}
	
	public static ActionResultType convert(World world, BlockPos pos, @Nullable PlayerEntity player){
		BlockState state = world.getBlockState(pos);
		if(state.getBlock() == Blocks.CAULDRON){
			world.setBlockState(pos, ArcanaBlocks.CRUCIBLE.get().getDefaultState().with(CrucibleBlock.FULL, state.get(CauldronBlock.LEVEL) >= 2));
			world.playSound(player, pos, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1, 1);
			for(int i = 0; i < 20; i++)
				world.addParticle(ParticleTypes.END_ROD, pos.getX() + world.rand.nextDouble(), pos.getY() + world.rand.nextDouble(), pos.getZ() + world.rand.nextDouble(), 0, 0, 0);
			return ActionResultType.SUCCESS;
		}
		if(state.getBlock() == Blocks.CRAFTING_TABLE){
			world.setBlockState(pos, ArcanaBlocks.ARCANE_CRAFTING_TABLE.get().getDefaultState());
			world.playSound(player, pos, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1, 1);
			for(int i = 0; i < 20; i++)
				world.addParticle(ParticleTypes.END_ROD, (pos.getX() - .1f) + world.rand.nextDouble() * 1.2f, (pos.getY() - .1f) + world.rand.nextDouble() * 1.2f, (pos.getZ() - .1f) + world.rand.nextDouble() * 1.2f, 0, 0, 0);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand){
		ArcanaSounds.playSpellCastSound(player);
		Focus focus = getFocus(player.getHeldItem(hand));
		if(focus != Focus.NO_FOCUS){
			Spell spell = focus.getSpell(player.getHeldItem(hand));
			if(spell != null){
				IAspectHandler handler = IAspectHandler.getFrom(player.getHeldItem(hand));
				// oh my god this code is terrible // YES, I know Xd.
				// time for more VisUtils I guess
				if(spell.getSpellCosts().toList().stream().allMatch(stack -> handler.findAspectInHolders(stack.getAspect()).getCurrentVis() >= stack.getAmount())){
					if (player.isCrouching())
						Spell.runSpell(spell,player,player.getHeldItem(hand), ICast.Action.SPECIAL);
					else
						Spell.runSpell(createBasicSpell(),player,player.getHeldItem(hand), ICast.Action.USE);
					// remove aspects from wand if spell successes.
					for(AspectStack cost : spell.getSpellCosts().toList())
						handler.findAspectInHolders(cost.getAspect()).drain(cost, false);
				}
			}else
				player.sendStatusMessage(new TranslationTextComponent("status.arcana.null_spell"), true);
		}
		AuraView view = AuraView.SIDED_FACTORY.apply(world);
		ItemStack itemstack = player.getHeldItem(hand);
		AtomicReference<ActionResult<ItemStack>> ret = new AtomicReference<>(ActionResult.resultConsume(itemstack));
		view.raycast(player.getEyePosition(0), player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue(), player).ifPresent(node -> {
			player.setActiveHand(hand);
			ret.set(ActionResult.resultConsume(itemstack));
		});
		return ret.get();
		//player.setActiveHand(hand);
		//return ActionResult.resultConsume(player.getHeldItem(hand));
	}
	
	public int getUseDuration(ItemStack stack){
		return 72000;
	}
	
	public void onUsingTick(ItemStack stack, LivingEntity player, int count){
		World world = player.world;
		AuraView view = AuraView.SIDED_FACTORY.apply(world);
		Optional<Node> nodeOptional = view.raycast(player.getEyePosition(0), player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue(), player);
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
						Vec3d nodePos = new Vec3d(node.getX(), node.getY(), node.getZ());
						Vec3d playerPos = player.getEyePosition(1);
						Vec3d diff = nodePos.subtract(playerPos);
						Vec3d direction = diff.normalize().mul(-1, -1, -1);
						int life = (int)Math.ceil(diff.length() / .05f);
						world.addParticle(new AspectHelixParticleData(aspect, life, world.rand.nextInt(180), direction), nodePos.x, nodePos.y, nodePos.z, 0, 0, 0);
					}
				}
		}else
			player.stopActiveHand();
		//world.addParticle(new AspectHelixParticleData(Aspects.EXCHANGE, 450, world.rand.nextInt(180), player.getLookVec()), player.getPosX(), player.getPosYEye(), player.getPosZ(), 0, 0, 0);
	}
	
	public ITextComponent getDisplayName(ItemStack stack){
		return new TranslationTextComponent(getCore(stack).getCoreTranslationKey(), new TranslationTextComponent(getCap(stack).getPrefixTranslationKey()));
	}
	
	public boolean canSwapFocus(){
		return true;
	}
	
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		if(isInGroup(group)){
			// iron/wooden, silver/dair, gold/greatwood, thaumium/silverwood, void/arcanium
			items.add(withCapAndCoreForCt("iron_cap", "wood_wand"));
			items.add(withCapAndCoreForCt("silver_cap", "dair_wand"));
			items.add(withCapAndCoreForCt("gold_cap", "greatwood_wand"));
			items.add(withCapAndCoreForCt("void_cap", "arcanium_wand"));
		}
	}
	
	public static ItemStack withCapAndCoreForCt(String cap, String core){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("cap", "arcana:" + cap);
		nbt.putString("core", "arcana:" + core);
		ItemStack stack = new ItemStack(ArcanaItems.WAND.get(), 1);
		stack.setTag(nbt);
		return stack;
	}
	
	public static ItemStack withCapAndCore(String cap, String core){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("cap", cap);
		nbt.putString("core", core);
		ItemStack stack = new ItemStack(ArcanaItems.WAND.get(), 1);
		stack.setTag(nbt);
		return stack;
	}
	
	public static ItemStack withCapAndCore(ResourceLocation cap, ResourceLocation core){
		return withCapAndCore(cap.toString(), core.toString());
	}
	
	public static ItemStack withCapAndCore(Cap cap, Core core){
		return withCapAndCore(cap.getId(), core.getId());
	}
	
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag){
		super.addInformation(stack, world, tooltip, flag);
		// Add focus info
		Spell spell = getFocus(stack).getSpell(stack);
		if(spell != null){
			Optional<ITextComponent> name = spell.getName(getFocusData(stack).getCompound("Spell"));
			name.ifPresent(e -> tooltip.add(new TranslationTextComponent("tooltip.arcana.spell", e,
					spell.getSpellCosts().toList().stream()
						.map(AspectStack::getAspect)
						.map(aspect -> I18n.format("aspect." + aspect.name()))
						.collect(Collectors.joining(", ")))));
		}
	}
}