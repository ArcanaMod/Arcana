package net.arcanamod.event;

import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.blocks.Taint;
import net.arcanamod.blocks.tiles.AspectBookshelfTileEntity;
import net.arcanamod.blocks.tiles.JarTileEntity;
import net.arcanamod.capabilities.TaintTrackable;
import net.arcanamod.client.render.ArcanaParticles;
import net.arcanamod.client.render.AspectParticleData;
import net.arcanamod.client.render.NumberParticleData;
import net.arcanamod.effects.ArcanaEffects;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.GogglePriority;
import net.arcanamod.util.RayTraceUtils;
import net.arcanamod.world.AuraView;
import net.arcanamod.world.Node;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

@Mod.EventBusSubscriber
public class EntityTickHandler{

	@SubscribeEvent
	public static void tickPlayer(TickEvent.PlayerTickEvent event){
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
		
		if(player instanceof ClientPlayerEntity && event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END){
			ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)player;
			World world = clientPlayerEntity.world;
			double reach = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
			BlockPos pos = RayTraceUtils.getTargetBlockPos(clientPlayerEntity, world, (int)reach);
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof JarTileEntity){
				if(world.isRemote()){
					GogglePriority priority = GogglePriority.getClientGogglePriority();
					JarTileEntity jte = (JarTileEntity)te;
					if(priority == GogglePriority.SHOW_NODE || priority == GogglePriority.SHOW_ASPECTS)
						if(jte.vis.getHolder(0).getContainedAspect() != Aspects.EMPTY){
							double srx = (-Math.sin(Math.toRadians(clientPlayerEntity.rotationYaw)));
							double crx = (Math.cos(Math.toRadians(clientPlayerEntity.rotationYaw)));
							double y_srx = (-Math.sin(Math.toRadians(clientPlayerEntity.rotationPitch)));
							double y_crx = (Math.cos(Math.toRadians(clientPlayerEntity.rotationPitch)));
							world.addParticle(new AspectParticleData(new ResourceLocation(AspectUtils.getAspectTextureLocation(jte.vis.getHolder(0).getContainedAspect()).toString().replace("textures/","").replace(".png","")), ArcanaParticles.ASPECT_PARTICLE.get()),
									pos.getX() + 0.5D + ((-srx) / 2), pos.getY() + 0.8D, pos.getZ() + 0.5D + ((-crx) / 2), 0, 0, 0);
							int currVis = jte.vis.getHolder(0).getCurrentVis();
							int currVis_4th = Integer.parseInt(("" + currVis).substring(("" + currVis).length() - 1));
							world.addParticle(new NumberParticleData(currVis_4th, ArcanaParticles.NUMBER_PARTICLE.get()), // If you change Y, particle is no more good aligned with particle
									pos.getX() + 0.5D + ((-srx) / 2), pos.getY() + 0.8D, pos.getZ() + 0.5D + ((-crx) / 2), 0, 0, 0);
						}
				}
			}
			if(te instanceof AspectBookshelfTileEntity){
				if(world.isRemote()){
					GogglePriority priority = GogglePriority.getClientGogglePriority();
					AspectBookshelfTileEntity abte = (AspectBookshelfTileEntity)te;
					if(priority == GogglePriority.SHOW_NODE || priority == GogglePriority.SHOW_ASPECTS){
						IAspectHandler vis = IAspectHandler.getFrom(abte);
						if(vis != null)
							for(int i = 0; i < vis.getHoldersAmount(); i++){
								if(vis.getHolder(i).getContainedAspect() != Aspects.EMPTY){
									double srx = (-Math.sin(Math.toRadians(clientPlayerEntity.rotationYaw+(i*16)-72)));
									double crx = (Math.cos(Math.toRadians(clientPlayerEntity.rotationYaw+(i*16)-72)));
									double trx = (Math.cos(Math.toRadians(clientPlayerEntity.rotationPitch+(i*16)-72)));
									LogManager.getLogger("Arcana.EntityTickHandler").debug("trx: "+trx);
									world.addParticle(new AspectParticleData(new ResourceLocation(AspectUtils.getAspectTextureLocation(vis.getHolder(i).getContainedAspect()).toString().replace("textures/","").replace(".png","")), ArcanaParticles.ASPECT_PARTICLE.get()),
											pos.getX() + 0.5D + (((-srx) / 2)), pos.getY() + 0.8D, pos.getZ() + 0.5D + (((-crx) / 2)), 0, 0, 0);
									int currVis = vis.getHolder(i).getCurrentVis();
									int currVis_4th = Integer.parseInt(("" + currVis).substring(("" + currVis).length() - 1));
									world.addParticle(new NumberParticleData(currVis_4th, ArcanaParticles.NUMBER_PARTICLE.get()), // If you change Y, particle is no more good aligned with particle
											pos.getX() + 0.5D + (((-srx) / 2)), pos.getY() + 0.8D, pos.getZ() + 0.5D + (((-crx) / 2)), 0, 0, 0);
								}
							}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void tickEntities(LivingEvent.LivingUpdateEvent event){
		LivingEntity living = event.getEntityLiving();
		TaintTrackable trackable = TaintTrackable.getFrom(living);
		if(trackable != null && trackable.isTracking()){
			if(Taint.isAreaInTaintBiome(living.getPosition(), living.world)){
				trackable.setInTaintBiome(true);
				trackable.addTimeInTaintBiome(1);
				if(!Taint.isTainted(living.getType()) && trackable.getTimeInTaintBiome() > ArcanaConfig.TAINT_EFFECT_TIME.get())
					living.addPotionEffect(new EffectInstance(ArcanaEffects.TAINTED.get(), 5 * 20, 0, true, true));
			}else{
				trackable.setInTaintBiome(false);
				trackable.setTimeInTaintBiome(0);
				trackable.setTracking(false);
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static void renderNumberParticles(double baseX, double baseY, double baseZ, float rotation, int number, World world){
		String numberStr = String.valueOf(number);
		char[] array = numberStr.toCharArray();
		double rotOffsetX = -Math.cos(Math.toRadians(rotation));
		double rotOffsetZ = -Math.sin(Math.toRadians(rotation));
		double size = .1;
		double x = baseX - array.length * rotOffsetX * size;
		double z = baseZ - array.length * rotOffsetZ * size;
		for(int i = 0, length = array.length; i < length; i++){
			char c = array[i];
			world.addParticle(new NumberParticleData(Integer.parseInt(String.valueOf(c)), ArcanaParticles.NUMBER_PARTICLE.get()), false, x + rotOffsetX * i * size, baseY, z + rotOffsetZ * i * size, 0, 0, 0);
		}
	}
}