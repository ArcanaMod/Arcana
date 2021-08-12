package net.arcanamod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.tiles.AlembicTileEntity;
import net.arcanamod.blocks.tiles.CrucibleTileEntity;
import net.arcanamod.client.ClientAuraHandler;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.MagicDeviceItem;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.settings.GogglePriority;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.arcanamod.Arcana.arcLoc;

@Mod.EventBusSubscriber(modid = Arcana.MODID, value = Dist.CLIENT)
public final class Huds{
	
	public static final ResourceLocation FLUX_METER_FRAME = arcLoc("textures/gui/hud/flux_meter_frame.png");
	public static final ResourceLocation FLUX_METER_FILLING = arcLoc("textures/gui/hud/flux_chaos.png");
	
	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event){
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if(player != null && event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)){
			ItemStack mainHand = player.getHeldItemMainhand();
			ItemStack offHand = player.getHeldItemOffhand();
			
			ItemStack wand = ItemStack.EMPTY;
			ItemStack meter = ItemStack.EMPTY;
			if(mainHand.getItem() instanceof MagicDeviceItem){
				wand = mainHand;
			}else if(offHand.getItem() instanceof MagicDeviceItem){
				wand = offHand;
			}
			if(mainHand.getItem().equals(ArcanaItems.FLUX_METER.get())){
				meter = mainHand;
			}else if(offHand.getItem().equals(ArcanaItems.FLUX_METER.get())){
				meter = offHand;
			}
			
			// wand GUI (high render priority)
			if(wand != ItemStack.EMPTY){
				Core core = MagicDeviceItem.getCore(wand);
				IAspectHandler aspects = IAspectHandler.getFrom(wand);
				if(aspects != null){
					int offX = ArcanaConfig.WAND_HUD_X.get().intValue();
					int offY = ArcanaConfig.WAND_HUD_Y.get().intValue();
					float scale = ArcanaConfig.WAND_HUD_SCALING.get().floatValue();
					int baseX = (int)(ArcanaConfig.WAND_HUD_LEFT.get() ? offX / scale : (event.getWindow().getScaledWidth() - offX) / scale - 49);
					int baseY = (int)(ArcanaConfig.WAND_HUD_TOP.get() ? offY / scale : (event.getWindow().getScaledHeight() - offY) / scale - 49);
					RenderSystem.pushMatrix();
					RenderSystem.scalef(scale,scale,2);
					ClientUiUtil.renderVisCore(event.getMatrixStack(), core, baseX, baseY);
					ClientUiUtil.renderVisMeter(event.getMatrixStack(), aspects, baseX, baseY);
					MagicDeviceItem.getFocusStack(wand).ifPresent(item -> Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(item, baseX + 1, baseY + 1));
					if (player.isCrouching())
						ClientUiUtil.renderVisDetailInfo(event.getMatrixStack(),aspects);
					RenderSystem.popMatrix();
				}
				// flux meter GUI
			}else if(meter != ItemStack.EMPTY){
				// display filling at 8,8
				// 10 frames, 32x100
				int frame = (int)((player.ticksExisted + event.getPartialTicks()) % 10);
				float flux = ClientAuraHandler.currentFlux;
				int pixHeight = (int)Math.min(flux, 100);
				Minecraft.getInstance().getTextureManager().bindTexture(FLUX_METER_FILLING);
				ClientUiUtil.drawModalRectWithCustomSizedTexture(event.getMatrixStack(), 8, 8 + (100 - pixHeight), 0, 100 * frame, 32, pixHeight, 1024, 1024);
				// display the frame at top-left
				Minecraft.getInstance().getTextureManager().bindTexture(FLUX_METER_FRAME);
				ClientUiUtil.drawTexturedModalRect(event.getMatrixStack(), 0, 0, 0, 0, 48, 116);
				// if flux is over max, flash white
				if(flux > 100){
					int amount = (int)(Math.abs(((MathHelper.sin((player.ticksExisted + event.getPartialTicks()) / 3f)) / 3f)) * 255);
					int colour = 0x00ffffff | (amount << 24);
					GuiUtils.drawGradientRect(event.getMatrixStack().getLast().getMatrix(), 1, 8, 8, 40, 108, colour, colour);
				}
				// if the player is sneaking, display the amount of flux "exactly"
				// rounded to 2dp
				if(player.isSneaking())
					Minecraft.getInstance().fontRenderer.drawStringWithShadow(event.getMatrixStack(), String.format("%.2f", flux), 47, 8 + (97 - pixHeight), -1);
			}
			double reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
			Vector3d start = player.getEyePosition(1);
			Vector3d facing = player.getLookVec();
			Vector3d end = start.add(facing.getX() * reach, facing.getY() * reach, facing.getZ() * reach);
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
						AspectStack aspStack = stacks.get(i);
						int x = baseX + i * size;
						int y = baseY - 10;
						if(i % 2 == 0)
							y += 8;
						ClientUiUtil.renderAspectStack(event.getMatrixStack(), aspStack, x, y);
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
						IAspectHolder aspStack = stacks.getHolder(i);
						int x = baseX + i * size;
						int y = baseY - 10;
						if(i % 2 == 0)
							y += 8;
						AspectStack stack1 = aspStack.getContainedAspectStack();
						if(!stack1.isEmpty())
							ClientUiUtil.renderAspectStack(event.getMatrixStack(), stack1, x, y);
					}
				}
			}
		}
	}
}