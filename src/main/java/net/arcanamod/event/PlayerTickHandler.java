package net.arcanamod.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.JarTileEntity;
import net.arcanamod.client.render.ArcanaParticles;
import net.arcanamod.client.render.AspectParticleData;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.GogglePriority;
import net.arcanamod.util.NodeHelper;
import net.arcanamod.util.RayTraceUtils;
import net.arcanamod.world.AuraView;
import net.arcanamod.world.Node;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

@Mod.EventBusSubscriber
public class PlayerTickHandler{

	@SubscribeEvent
	public static void tick(TickEvent.PlayerTickEvent event){
		PlayerEntity player = event.player;
		
		if(player instanceof ServerPlayerEntity && event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END){
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
			// If the player is near a node,
			AuraView view = new ServerAuraView(serverPlayerEntity.getServerWorld());
			Collection<Node> ranged = new ArrayList<>(view.getNodesWithinAABB(player.getBoundingBox().grow(2)));
			
			// and is holding the scribbled notes item,
			if(!ranged.isEmpty() && player.inventory.hasItemStack(new ItemStack(ArcanaItems.SCRIBBLED_NOTES.get()))){
				// it switches it for a complete version,
				player.inventory.setInventorySlotContents(player.inventory.getSlotFor(new ItemStack(ArcanaItems.SCRIBBLED_NOTES.get())), new ItemStack(ArcanaItems.SCRIBBLED_NOTES_COMPLETE.get()));
				// and gives them a status message.
				ITextComponent status = new TranslationTextComponent("status.get_complete_note").applyTextStyles(TextFormatting.ITALIC, TextFormatting.LIGHT_PURPLE);
				serverPlayerEntity.sendStatusMessage(status, false);
			}
		}

		if (player instanceof ClientPlayerEntity && event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END) {
			ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)player;
			World world = clientPlayerEntity.world;
			BlockPos pos = RayTraceUtils.getTargetBlockPos(clientPlayerEntity,world,6);
			if (pos!=null)
				if (world.getTileEntity(pos) instanceof JarTileEntity) {
					if(world.isRemote()){
						GogglePriority priority = NodeHelper.getGogglePriority();
						JarTileEntity jte = (JarTileEntity)world.getTileEntity(pos);
						if(priority == GogglePriority.SHOW_NODE || priority == GogglePriority.SHOW_ASPECTS)
							if(jte.vis.getHolder(0).getContainedAspect()!= Aspect.EMPTY) {
								double srx = (-Math.sin(Math.toRadians(clientPlayerEntity.rotationYaw)));
								double crx = (Math.cos(Math.toRadians(clientPlayerEntity.rotationYaw)));
								world.addParticle(new AspectParticleData(Aspects.getAspectTextureLocation(jte.vis.getHolder(0).getContainedAspect()), ArcanaParticles.ASPECT_PARTICLE.get()),
										pos.getX()+0.5D+((-srx)/2), pos.getY()+0.8D, pos.getZ()+0.5D+((-crx)/2), 0, 0, 0);
							}
					}
				}
		}
	}
}
