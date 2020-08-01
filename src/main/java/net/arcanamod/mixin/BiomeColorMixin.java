package net.arcanamod.mixin;

import net.arcanamod.blocks.Taint;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.biome.BiomeColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

// Lets mixin into BiomeColors for taint "biome" water :)
@Mixin(BiomeColors.class)
public class BiomeColorMixin {
	// TODO: I think that is better way to do this. Currently chunk loading is very slow
	@Inject(method = "getWaterColor(Lnet/minecraft/world/ILightReader;Lnet/minecraft/util/math/BlockPos;)I", at = @At("HEAD"), remap = false, cancellable = true)
	private static void getWaterColor(ILightReader worldIn, BlockPos blockPosIn, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		if (worldIn.getBlockState(blockPosIn.up()).getBlock() != Blocks.WATER) {
			Iterator<BlockPos> i = BlockPos.getAllInBoxMutable(blockPosIn.add(2, 3, 2), blockPosIn.add(-2, -3, -2)).iterator();
			while (i.hasNext()) {
				try {
					if (Taint.isTainted(worldIn.getBlockState(i.next()).getBlock())) {
						callbackInfoReturnable.setReturnValue(0x6e298e);
						break;
					}
				} catch (ArrayIndexOutOfBoundsException ignore) {
				}
			}
		}
	}
}
