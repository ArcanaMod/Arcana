package net.arcanamod.items;

import net.arcanamod.aspects.*;
import net.arcanamod.client.render.aspects.AspectHelixParticleData;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.Focus;
import net.arcanamod.items.attachment.FocusItem;
import net.arcanamod.util.VisUtils;
import net.arcanamod.world.AuraView;
import net.arcanamod.world.Node;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class MagicDeviceItem extends Item{
	public MagicDeviceItem(Properties properties) {
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
			battery.createCell(new AspectCell(getCore(stack).maxVis(), aspect));
		return battery;
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

	public ITextComponent getDisplayName(ItemStack stack){
		return new TranslationTextComponent(getCore(stack).getCoreTranslationKey(), new TranslationTextComponent(getCap(stack).getPrefixTranslationKey()));
	}

	public boolean canSwapFocus(){
		return true;
	}
}
