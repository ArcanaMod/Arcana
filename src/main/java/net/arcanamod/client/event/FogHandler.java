package net.arcanamod.client.event;

import net.arcanamod.blocks.Taint;
import net.arcanamod.capabilities.TaintTrackable;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.fluids.ArcanaFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FogHandler{
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void setFogColour(EntityViewRenderEvent.FogColors fog){
		Entity entity = fog.getInfo().getRenderViewEntity();
		World w = entity.getEntityWorld();
		BlockPos pos = fog.getInfo().getBlockPos();
		BlockState bs = w.getBlockState(pos);
		Block b = bs.getBlock();
		if(b.equals(ArcanaFluids.TAINT_FLUID_BLOCK.get())){
			float red = 78 / 255F, green = 44 / 255F, blue = 92 / 255F;
			fog.setRed(red);
			fog.setGreen(green);
			fog.setBlue(blue);
		}else if(entity instanceof LivingEntity && Taint.isAreaInTaintBiome(pos, w)){
			LivingEntity living = (LivingEntity)entity;
			int colour = 0x4E2C5C;
			float progress = Math.min(20, TaintTrackable.getFrom(living).getTimeInTaintBiome()) / 20f;
			int blended = UiUtil.blend(colour, ((int)(fog.getRed() * 255) << 16) | ((int)(fog.getGreen() * 255) << 8) | ((int)(fog.getBlue() * 255)), progress);
			
			fog.setRed(((blended & 0xff0000) >> 16) / 255f);
			fog.setGreen(((blended & 0xff00) >> 8) / 255f);
			fog.setBlue((blended & 0xff) / 255f);
		}
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void setFogDensity(EntityViewRenderEvent.FogDensity fog){
		Entity entity = fog.getInfo().getRenderViewEntity();
		World w = entity.getEntityWorld();
		BlockPos pos = fog.getInfo().getBlockPos();
		if(entity instanceof LivingEntity && Taint.isAreaInTaintBiome(pos, w)){
			LivingEntity living = (LivingEntity)entity;
			float progress = Math.min(20, TaintTrackable.getFrom(living).getTimeInTaintBiome()) / 20f;
			fog.setDensity(fog.getDensity() + progress * .3f);
		}
	}
}