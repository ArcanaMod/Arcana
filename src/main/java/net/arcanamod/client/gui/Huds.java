package net.arcanamod.client.gui;

import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.tiles.AlembicTileEntity;
import net.arcanamod.blocks.tiles.CrucibleTileEntity;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.WandItem;
import net.arcanamod.util.GogglePriority;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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
						int x = (int)(baseX + Math.sin((i / length * 2) * Math.PI) * 20);
						int y = (int)(baseY + Math.cos((i / length * 2) * Math.PI) * 20);
						Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(primal), x, y);
						Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, AspectUtils.getItemStackForAspect(primal), x - 1, y + 3, String.valueOf(aspects.findAspectInHolders(primal) == null ? -1 : aspects.findAspectInHolders(primal).getCurrentVis()));
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
						Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(stack.getAspect()), x, y);
						Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, AspectUtils.getItemStackForAspect(stack.getAspect()), x, y + 3, String.valueOf(stack.getAmount()));
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
						Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(stack.getContainedAspect()), x, y);
						Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, AspectUtils.getItemStackForAspect(stack.getContainedAspect()), x, y + 3, String.valueOf(stack.getContainedAspectStack().getAmount()));
					}
				}
			}
		}
	}
}