package net.arcanamod.client.event;

import net.arcanamod.fluids.ArcanaFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FogColorHandler {

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void setFog(EntityViewRenderEvent.FogColors fog){
		World w = fog.getInfo().getRenderViewEntity().getEntityWorld();
		BlockPos pos = fog.getInfo().getBlockPos();
		BlockState bs = w.getBlockState(pos);
		Block b = bs.getBlock();
		if(b.equals(ArcanaFluids.TAINT_FLUID_BLOCK.get())){
			float red = 78/255F, green = 44/255F, blue = 92/255F;
			fog.setRed(red); fog.setGreen(green); fog.setBlue(blue);
		}
	}
}