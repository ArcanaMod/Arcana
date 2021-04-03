package net.arcanamod.client.event;

import com.google.common.collect.ImmutableList;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.tiles.AspectBookshelfTileEntity;
import net.arcanamod.blocks.tiles.JarTileEntity;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.client.render.aspects.ArcanaParticles;
import net.arcanamod.client.render.aspects.AspectParticleData;
import net.arcanamod.client.render.aspects.NumberParticleData;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.mixin.AccessorMinecraft;
import net.arcanamod.util.GogglePriority;
import net.arcanamod.util.LocalAxis;
import net.arcanamod.util.Pair;
import net.arcanamod.util.RayTraceUtils;
import net.arcanamod.world.AuraView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientTickHandler {
	private ClientTickHandler() {}

	public static int ticksWithLexicaOpen = 0;
	public static int pageFlipTicks = 0;
	public static int ticksInGame = 0;
	public static float partialTicks = 0;
	public static float delta = 0;
	public static float total = 0;

	private static void calcDelta() {
		float oldTotal = total;
		total = ticksInGame + partialTicks;
		delta = total - oldTotal;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void renderTick(TickEvent.RenderTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (event.phase == TickEvent.Phase.START) {
			partialTicks = event.renderTickTime;

			if (mc.isGamePaused()) {
				// If game is paused, need to use the saved value. The event is always fired with the "true" value which
				// keeps updating when paused. See RenderTickEvent fire site for details, remove when MinecraftForge#6991 is resolved
				partialTicks = ((AccessorMinecraft) mc).getRenderPartialTicksPaused();
			}
		} else {
			calcDelta();
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void clientTickEnd(TickEvent.ClientTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		World world = mc.world;
		ClientPlayerEntity player = mc.player;

		if (player != null && world != null) {

			if (event.phase == TickEvent.Phase.END) {
				if (!Minecraft.getInstance().isGamePaused()) {
					ticksInGame++;
					partialTicks = 0;

					if (player != null) {
						int ticksToOpen = 10;

						Hand hand = null;
						if (player.getHeldItem(Hand.MAIN_HAND).getItem() == ArcanaItems.ARCANUM.get())
							hand = Hand.MAIN_HAND;
						if (player.getHeldItem(Hand.OFF_HAND).getItem() == ArcanaItems.ARCANUM.get())
							hand = Hand.OFF_HAND;

						if (hand != null)
							if (player.getHeldItem(hand).getOrCreateTag().getBoolean("open")) {
								if (ticksWithLexicaOpen < 0) {
									ticksWithLexicaOpen = 0;
								}
								if (ticksWithLexicaOpen < ticksToOpen) {
									ticksWithLexicaOpen++;
								}
								if (pageFlipTicks > 0) {
									pageFlipTicks--;
								}
							} else {
								pageFlipTicks = 0;
								if (ticksWithLexicaOpen > 0) {
									if (ticksWithLexicaOpen > ticksToOpen) {
										ticksWithLexicaOpen = ticksToOpen;
									}
									ticksWithLexicaOpen--;
								}
							}
					}
				}

				double reach = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
				BlockPos pos = RayTraceUtils.getTargetBlockPos(player, world, (int)reach);
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
								double srx = (-Math.sin(Math.toRadians(player.rotationYaw)));
								double crx = (Math.cos(Math.toRadians(player.rotationYaw)));
								// Add Aspect Particle
								world.addParticle(new AspectParticleData(new ResourceLocation(AspectUtils.getAspectTextureLocation(jte.vis.getHolder(0).getContainedAspect()).toString().replace("textures/", "").replace(".png", "")), ArcanaParticles.ASPECT_PARTICLE.get()),
										pos.getX() + 0.5D + ((-srx) / 2), pos.getY() + 0.8D, pos.getZ() + 0.5D + ((-crx) / 2), 0, 0, 0);
								int currVis = jte.vis.getHolder(0).getCurrentVis();
								int color = UiUtil.invert(jte.vis.getHolder(0).getContainedAspect().getColorRange().get(2));
								// Add Number Particles
								// If you change Y, particle is no more good aligned with particle
								renderNumberParticles(pos.getX() + 0.5D + ((-srx * 1.01) / 2), pos.getY() + 0.8D, pos.getZ() + 0.5D + ((-crx * 1.01) / 2), player.rotationYaw, currVis, color, world);
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
								renderAspectAndNumberParticlesInCircle(world, new Vec3d(pos), player, reducedStacks.stream().map(stack -> Pair.of(stack.getAspect(), stack.getAmount())).collect(Collectors.toList()));
							}
						}
					}
				}

				//Spell.updateSpellStatusBar(player);

				calcDelta();
			}
		}
	}

	public static void notifyPageChange() {
		if (pageFlipTicks == 0) {
			pageFlipTicks = 5;
		}
	}

	protected static void renderAspectAndNumberParticlesInCircle(World world, Vec3d pos, ClientPlayerEntity player, List<Pair<Aspect, Integer>> aspectStacks){
		float[] v = spreadVertices(aspectStacks.size(), 32);
		for(int i = 0; i < aspectStacks.size(); i++){
			double centerSpread = v[i];
			// track player head rotation
			double srx = (-Math.sin(Math.toRadians(player.rotationYaw + centerSpread + 10)));
			double crx = (Math.cos(Math.toRadians(player.rotationYaw + centerSpread + 10)));
			// Add Aspect Particle
			world.addParticle(new AspectParticleData(new ResourceLocation(AspectUtils.getAspectTextureLocation(aspectStacks.get(i).getFirst()).toString().replace("textures/", "").replace(".png", "")), ArcanaParticles.ASPECT_PARTICLE.get()),
					pos.getX() + 0.5D + (((-srx) / 2)), pos.getY() + 0.8D, pos.getZ() + 0.5D + (((-crx) / 2)), 0, 0, 0);
			int currVis = aspectStacks.get(i).getSecond();
			int color = aspectStacks.get(i).getFirst().getColorRange().get(2);
			// Add Number Particles
			// If you change Y, particle is no more good aligned with particle
			renderNumberParticles(pos.getX() + 0.5D + ((-srx * 1.01) / 2), pos.getY() + 0.8D, pos.getZ() + 0.5D + ((-crx * 1.01) / 2), player.rotationYaw, currVis, color, world);
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
		double center = -.66;
		double x = baseX - array.length * rotOffsetX * size * center;
		double z = baseZ - array.length * rotOffsetZ * size * center;
		for(int i = 0, length = array.length; i < length; i++){
			char c = array[i];
			world.addParticle(new NumberParticleData(Integer.parseInt(String.valueOf(c)), color, ArcanaParticles.NUMBER_PARTICLE.get()), false, x + rotOffsetX * i * size * padding, baseY - .12, z + rotOffsetZ * i * size * padding, 0, 0, 0);
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
