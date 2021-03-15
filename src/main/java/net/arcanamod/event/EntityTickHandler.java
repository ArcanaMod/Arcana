package net.arcanamod.event;

import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.tiles.AspectBookshelfTileEntity;
import net.arcanamod.blocks.tiles.JarTileEntity;
import net.arcanamod.capabilities.TaintTrackable;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.client.render.aspects.ArcanaParticles;
import net.arcanamod.client.render.aspects.AspectParticleData;
import net.arcanamod.client.render.aspects.NumberParticleData;
import net.arcanamod.effects.ArcanaEffects;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.systems.spell.Spell;
import net.arcanamod.systems.spell.casts.DelayedCast;
import net.arcanamod.systems.spell.casts.ToggleableCast;
import net.arcanamod.systems.taint.Taint;
import net.arcanamod.util.GogglePriority;
import net.arcanamod.util.LocalAxis;
import net.arcanamod.util.Pair;
import net.arcanamod.util.RayTraceUtils;
import net.arcanamod.world.AuraView;
import net.arcanamod.world.Node;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class EntityTickHandler{
	
	@SubscribeEvent
	public static void tickPlayer(TickEvent.PlayerTickEvent event){
		PlayerEntity player = event.player;
		
		// Give completed scribbled note when player is near node
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
			
			List<DelayedCast.Impl> spellsScheduledToDeletion = new ArrayList<>();
			DelayedCast.delayedCasts.forEach(delayedCast -> {
				if(delayedCast.ticks >= delayedCast.ticksPassed){
					delayedCast.spellEvent.accept(0);
					spellsScheduledToDeletion.add(delayedCast);
				}else
					delayedCast.ticksPassed++;
			});
			DelayedCast.delayedCasts.removeAll(spellsScheduledToDeletion);

			ToggleableCast.toggleableCasts.forEach(toggleableCast -> {
				if(toggleableCast.getSecond().ticks >= toggleableCast.getSecond().ticksPassed){
					toggleableCast.getSecond().spellEvent.accept(0);
					toggleableCast.getSecond().ticksPassed = 0;
				}else
					toggleableCast.getSecond().ticksPassed++;
			});
		}
		
		// Render aspect particles
		if(player instanceof ClientPlayerEntity && event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END){
			ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)player;
			World world = clientPlayerEntity.world;
			double reach = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
			BlockPos pos = RayTraceUtils.getTargetBlockPos(clientPlayerEntity, world, (int)reach);
			TileEntity te = world.getTileEntity(pos);
			GogglePriority priority = GogglePriority.getClientGogglePriority();
			
			// Render aspect particle around Node
			if(priority == GogglePriority.SHOW_ASPECTS){
				AuraView view = AuraView.SIDED_FACTORY.apply(player.world);
				Vec3d position = player.getEyePosition(Minecraft.getInstance().getRenderPartialTicks());
				view.raycast(position, reach, player).ifPresent(node -> {
					List<IAspectHolder> holders = node.getAspects().getHolders();
					ArrayList<Pair<Aspect, Integer>> stacks = new ArrayList<>();
					for(IAspectHolder holder : holders)
						stacks.add(Pair.of(holder.getContainedAspect(), holder.getCurrentVis()));
					
					for(int i = 0, size = stacks.size(); i < size; i++){
						Pair<Aspect, Integer> stack = stacks.get(i);
						// let's place them on X/Y positions on a local axis, then convert to world co-ords
						// then its as easy as picking positions on a circle
						float angle = (float)(Math.toRadians(360 * i) / size);
						// TODO: ease in/out (multiply by some fraction)
						Vec3d localPos = new Vec3d(MathHelper.sin(angle) * (size / 5f), MathHelper.cos(angle) * (size / 5f), 0);
						Vec3d wPos = LocalAxis.toAbsolutePos(localPos, player.getPitchYaw(), node.getPosition());
						// why?
						world.addParticle(new AspectParticleData(new ResourceLocation(AspectUtils.getAspectTextureLocation(stack.getFirst()).toString().replace("textures/", "").replace(".png", "")), ArcanaParticles.ASPECT_PARTICLE.get()), wPos.getX(), wPos.getY(), wPos.getZ(), 0, 0, 0);
						// TODO: client reference (UiUtil::tooltipColour)
						Vec3d numberPos = new Vec3d(MathHelper.sin(angle) * ((size / 5f) - .04), MathHelper.cos(angle) * ((size / 5f) - .04), -.1);
						wPos = LocalAxis.toAbsolutePos(numberPos, player.getPitchYaw(), node.getPosition());
						renderNumberParticles(wPos.x, wPos.y, wPos.z, player.getYaw(0), stack.getSecond(), UiUtil.tooltipColour(stack.getFirst()), world);
					}
				});
			}
			// Render aspect particle around Jar
			if(te instanceof JarTileEntity){
				if(world.isRemote()){
					JarTileEntity jte = (JarTileEntity)te;
					// If player has googles show particle
					if(priority == GogglePriority.SHOW_ASPECTS)
						if(jte.vis.getHolder(0).getContainedAspect() != Aspects.EMPTY){
							// track player head rotation
							double srx = (-Math.sin(Math.toRadians(clientPlayerEntity.rotationYaw)));
							double crx = (Math.cos(Math.toRadians(clientPlayerEntity.rotationYaw)));
							// Add Aspect Particle
							world.addParticle(new AspectParticleData(new ResourceLocation(AspectUtils.getAspectTextureLocation(jte.vis.getHolder(0).getContainedAspect()).toString().replace("textures/", "").replace(".png", "")), ArcanaParticles.ASPECT_PARTICLE.get()),
									pos.getX() + 0.5D + ((-srx) / 2), pos.getY() + 0.8D, pos.getZ() + 0.5D + ((-crx) / 2), 0, 0, 0);
							int currVis = jte.vis.getHolder(0).getCurrentVis();
							int color = UiUtil.invert(jte.vis.getHolder(0).getContainedAspect().getColorRange().get(2));
							// Add Number Particles
							// If you change Y, particle is no more good aligned with particle
							renderNumberParticles(pos.getX() + 0.5D + ((-srx * 1.01) / 2), pos.getY() + 0.8D, pos.getZ() + 0.5D + ((-crx * 1.01) / 2), clientPlayerEntity.rotationYaw, currVis, color, world);
						}
				}
			}
			// Render aspect particle around Phialshelf
			if(te instanceof AspectBookshelfTileEntity){
				if(world.isRemote()){
					AspectBookshelfTileEntity abte = (AspectBookshelfTileEntity)te;
					// If player has googles show particle
					if(priority == GogglePriority.SHOW_ASPECTS){
						IAspectHandler vis = IAspectHandler.getFrom(abte);
						if(vis != null){
							// Get all stacks from phialshelf
							ArrayList<AspectStack> stacks = new ArrayList<AspectStack>();
							for(int i = 0; i < vis.getHoldersAmount(); i++)
								if(vis.getHolder(i).getContainedAspect() != Aspects.EMPTY)
									stacks.add(vis.getHolder(i).getContainedAspectStack());
							// Squish aspect stacks in to reducedStacks
							List<AspectStack> reducedStacks = AspectUtils.squish(stacks);
							renderAspectAndNumberParticlesInCircle(world, new Vec3d(pos), clientPlayerEntity, reducedStacks.stream().map(stack -> Pair.of(stack.getAspect(), stack.getAmount())).collect(Collectors.toList()));
						}
					}
				}
			}
			
			//Spell.updateSpellStatusBar(player);
		}
	}
	
	protected static void renderAspectAndNumberParticlesInCircle(World world, Vec3d pos, ClientPlayerEntity clientPlayerEntity, List<Pair<Aspect, Integer>> aspectStacks){
		float[] v = spreadVertices(aspectStacks.size(), 32);
		for(int i = 0; i < aspectStacks.size(); i++){
			double centerSpread = v[i];
			// track player head rotation
			double srx = (-Math.sin(Math.toRadians(clientPlayerEntity.rotationYaw + centerSpread + 10)));
			double crx = (Math.cos(Math.toRadians(clientPlayerEntity.rotationYaw + centerSpread + 10)));
			// Add Aspect Particle
			world.addParticle(new AspectParticleData(new ResourceLocation(AspectUtils.getAspectTextureLocation(aspectStacks.get(i).getFirst()).toString().replace("textures/", "").replace(".png", "")), ArcanaParticles.ASPECT_PARTICLE.get()),
					pos.getX() + 0.5D + (((-srx) / 2)), pos.getY() + 0.8D, pos.getZ() + 0.5D + (((-crx) / 2)), 0, 0, 0);
			int currVis = aspectStacks.get(i).getSecond();
			int color = aspectStacks.get(i).getFirst().getColorRange().get(2);
			// Add Number Particles
			// If you change Y, particle is no more good aligned with particle
			renderNumberParticles(pos.getX() + 0.5D + ((-srx * 1.01) / 2), pos.getY() + 0.8D, pos.getZ() + 0.5D + ((-crx * 1.01) / 2), clientPlayerEntity.rotationYaw, currVis, color, world);
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
	private static void renderNumberParticles(double baseX, double baseY, double baseZ, float rotation, int number, int color, World world){
		String numberStr = String.valueOf(number);
		char[] array = numberStr.toCharArray();
		double rotOffsetX = -Math.cos(Math.toRadians(rotation));
		double rotOffsetZ = -Math.sin(Math.toRadians(rotation));
		double size = .1;
		double padding = .8;
		double center = .25;
		double x = baseX - array.length * rotOffsetX * size * center;
		double z = baseZ - array.length * rotOffsetZ * size * center;
		for(int i = 0, length = array.length; i < length; i++){
			char c = array[i];
			world.addParticle(new NumberParticleData(Integer.parseInt(String.valueOf(c)), color, ArcanaParticles.NUMBER_PARTICLE.get()), false, x + rotOffsetX * i * size * padding, baseY, z + rotOffsetZ * i * size * padding, 0, 0, 0);
		}
	}
	
	private static float[] spreadVertices(float amount, float padding){
		float[] r = new float[(int)amount];
		for(int i = 0; i < amount; i++){
			r[i] = i - (amount / 2);
			r[i] *= padding;
		}
		return r;
	}
}