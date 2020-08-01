package net.arcanamod.mixin;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.biome.BiomeColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

// Lets mixin into BiomeColors for taint "biome" water :)
@Mixin(BiomeColors.class)
public class BiomeColorMixin {
	@Inject(method = "getWaterColor(Lnet/minecraft/world/ILightReader;Lnet/minecraft/util/math/BlockPos;)I", at = @At("HEAD"), remap = false, cancellable = true)
	private static void getWaterColor(ILightReader worldIn, BlockPos blockPosIn, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		BlockPos.getAllInBox(blockPosIn.add(3,3,3),blockPosIn.add(-3,-3,-3)).forEach(blockPos -> {
			try{
				if (worldIn.getBlockState(blockPos).getBlock() == ArcanaBlocks.TAINTED_SOIL.get())
					callbackInfoReturnable.setReturnValue(Color.decode("#6e298e").getRGB());
			}catch(ArrayIndexOutOfBoundsException ignore){}
		});
	}
}
