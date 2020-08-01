package net.arcanamod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.tiles.AlembicTileEntity;
import net.arcanamod.blocks.tiles.CrucibleTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.WandItem;
import net.arcanamod.util.GogglePriority;
import net.arcanamod.world.AuraView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Arcana.MODID)
public final class Huds{
	
	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event){
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if(player != null && event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)){
			if(player.getHeldItemMainhand().getItem() instanceof WandItem){
				IAspectHandler aspects = IAspectHandler.getFrom(player.getHeldItemMainhand());
				if(aspects != null){
					float length = AspectUtils.primalAspects.length;
					int baseX = ArcanaConfig.WAND_HUD_LEFT.get() ? 24 : event.getWindow().getScaledWidth() - 24 - 16;
					int baseY = ArcanaConfig.WAND_HUD_TOP.get() ? 24 : event.getWindow().getScaledHeight() - 24 - 16 - 4;
					for(int i = 0; i < length; i++){
						Aspect primal = AspectUtils.primalAspects[i];
						AspectStack stack = aspects.findAspectInHolders(primal).getContainedAspectStack();
						int x = (int)(baseX + Math.sin((i / length * 2) * Math.PI) * 20);
						int y = (int)(baseY + Math.cos((i / length * 2) * Math.PI) * 20);
						if(!stack.isEmpty())
							UiUtil.renderAspectStack(stack, x, y);
						else{
							RenderSystem.pushMatrix();
							RenderSystem.color4f(.5f, .5f, .5f, 1);
							UiUtil.renderAspect(primal, x, y);
							RenderSystem.popMatrix();
						}
					}
					// if a focus is present
					Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(new ItemStack(ArcanaItems.FOCUS_PARTS.get()), baseX, baseY);
				}
			}
			double reach = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
			Vec3d start = player.getEyePosition(1);
			Vec3d facing = player.getLookVec();
			Vec3d end = start.add(facing.getX() * reach, facing.getY() * reach, facing.getZ() * reach);
			BlockPos targeted = player.world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player)).getPos();
			TileEntity te = player.world.getTileEntity(targeted);
			if(te instanceof CrucibleTileEntity){
				CrucibleTileEntity crucible = (CrucibleTileEntity)te;
				GogglePriority priority = GogglePriority.getClientGogglePriority();
				if(priority == GogglePriority.SHOW_NODE || priority == GogglePriority.SHOW_ASPECTS){
					List<AspectStack> stacks = new ArrayList<>(crucible.getAspectStackMap().values());
					int size = 20;
					int baseX = (event.getWindow().getScaledWidth() - stacks.size() * size) / 2;
					int baseY = (event.getWindow().getScaledHeight() - size) / 2 - (ArcanaConfig.BLOCK_HUDS_TOP.get() ? 10 : -10);
					for(int i = 0, stacksSize = stacks.size(); i < stacksSize; i++){
						AspectStack stack = stacks.get(i);
						int x = baseX + i * size;
						int y = baseY - 10;
						if(i % 2 == 0)
							y += 8;
						UiUtil.renderAspectStack(stack, x, y);
					}
				}
			}
			if(te instanceof AlembicTileEntity){
				AlembicTileEntity alembic = (AlembicTileEntity)te;
				GogglePriority priority = GogglePriority.getClientGogglePriority();
				if(priority == GogglePriority.SHOW_NODE || priority == GogglePriority.SHOW_ASPECTS){
					AspectBattery stacks = alembic.aspects;
					int size = 20;
					int baseX = (event.getWindow().getScaledWidth() - stacks.getHoldersAmount() * size) / 2;
					int baseY = (event.getWindow().getScaledHeight() - size) / 2 - (ArcanaConfig.BLOCK_HUDS_TOP.get() ? 10 : -10);
					for(int i = 0, stacksSize = stacks.getHoldersAmount(); i < stacksSize; i++){
						IAspectHolder stack = stacks.getHolder(i);
						int x = baseX + i * size;
						int y = baseY - 10;
						if(i % 2 == 0)
							y += 8;
						AspectStack stack1 = stack.getContainedAspectStack();
						if(!stack1.isEmpty())
							UiUtil.renderAspectStack(stack1, x, y);
					}
				}
			}
			GogglePriority priority = GogglePriority.getClientGogglePriority();
			if(priority == GogglePriority.SHOW_ASPECTS){
				AuraView view = AuraView.SIDED_FACTORY.apply(Minecraft.getInstance().world);
				Vec3d position = player.getEyePosition(Minecraft.getInstance().getRenderPartialTicks());
				view.raycast(position, reach, player).ifPresent(node -> {
					List<IAspectHolder> holders = node.getAspects().getHolders();
					int baseX = event.getWindow().getScaledWidth() / 2 - 8;
					int baseY = event.getWindow().getScaledHeight() / 2 - 8;
					for(int i = 0, size = holders.size(); i < size; i++){
						IAspectHolder holder = holders.get(i);
						AspectStack stack = holder.getContainedAspectStack();
						int x = (int)(baseX + Math.sin((i / (float)size * 2) * Math.PI) * (30 + 1 / MathHelper.fastInvSqrt(size)));
						int y = (int)(baseY + Math.cos((i / (float)size * 2) * Math.PI) * (30 + 1 / MathHelper.fastInvSqrt(size)));
						if(!stack.isEmpty())
							UiUtil.renderAspectStack(stack, x, y);
						else
							// the holder is empty, so instead defer to the whitelisted aspect
							UiUtil.renderAspectStack(holder.getContainedAspect(), 0, x, y);
					}
				});
			}
		}
	}
}