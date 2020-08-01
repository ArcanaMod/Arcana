package net.arcanamod.mixin;

import net.arcanamod.blocks.Taint;
import net.arcanamod.util.MixinUtil;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraft.world.level.ColorResolver;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.Iterator;

import static net.minecraft.world.biome.BiomeColors.WATER_COLOR;

// Lets mixin into BiomeColors for taint "biome" water :)
@OnlyIn(Dist.CLIENT)
@Mixin(BiomeColors.class)
public class BiomeColorMixin {
	// TODO: I think that is better way to do this. Currently chunk loading is very slow
	@Inject(method = "getWaterColor(Lnet/minecraft/world/ILightReader;Lnet/minecraft/util/math/BlockPos;)I", at = @At("HEAD"), remap = false, cancellable = true)
	private static void getWaterColor(ILightReader worldIn, BlockPos blockPosIn, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		if (worldIn.getBlockState(blockPosIn.up()).getBlock() != Blocks.WATER) {
			Iterator<BlockPos> i0 = BlockPos.getAllInBoxMutable(blockPosIn.add(3, 3, 3), blockPosIn.add(-3, -3, -3)).iterator();
			while (i0.hasNext()) {
				try {
					if (Taint.isTainted(worldIn.getBlockState(i0.next()).getBlock())) {
						callbackInfoReturnable.setReturnValue(MixinUtil.blend(0x6e298e, MixinUtil.func_228359_a_(worldIn, blockPosIn, WATER_COLOR), 0.66f));
						break;
					}
				} catch (ArrayIndexOutOfBoundsException ignore) {
				}
			}
			Iterator<BlockPos> i1 = BlockPos.getAllInBoxMutable(blockPosIn.add(2, 3, 2), blockPosIn.add(-2, -3, -2)).iterator();
			while (i1.hasNext()) {
				try {
					if (Taint.isTainted(worldIn.getBlockState(i1.next()).getBlock())) {
						callbackInfoReturnable.setReturnValue(MixinUtil.blend(0x6e298e, MixinUtil.func_228359_a_(worldIn, blockPosIn, WATER_COLOR), 0.33f));
						break;
					}
				} catch (ArrayIndexOutOfBoundsException ignore) {
				}
			}
			Iterator<BlockPos> i = BlockPos.getAllInBoxMutable(blockPosIn.add(1, 3, 1), blockPosIn.add(-1, -3, -1)).iterator();
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
