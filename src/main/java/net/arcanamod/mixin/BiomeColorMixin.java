package net.arcanamod.mixin;

import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.Taint;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.util.MixinUtil;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
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
	// TODO: I think that is better way to do this. Currently chunk loading is slower than normal
	@Inject(method = "getWaterColor(Lnet/minecraft/world/ILightReader;Lnet/minecraft/util/math/BlockPos;)I", at = @At("HEAD"), remap = false, cancellable = true)
	private static void getWaterColor(ILightReader worldIn, BlockPos blockPosIn, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		if (blockPosIn.getX()==191&&blockPosIn.getY()==62&&blockPosIn.getZ()==-103)
			System.out.println("E");
		if (worldIn.getBlockState(blockPosIn.up()).getBlock() != Blocks.WATER) {
			Iterator<BlockPos> im1 = BlockPos.getAllInBoxMutable(blockPosIn.add(4, 2, 4), blockPosIn.add(-4, -2, -4)).iterator();
			while (im1.hasNext()) {
				try {
					BlockPos b = im1.next();
					if (Taint.isTainted(worldIn.getBlockState(b).getBlock())) {
						callbackInfoReturnable.setReturnValue(UiUtil.blend(0x6e298e, MixinUtil.func_228359_a_(worldIn, blockPosIn, WATER_COLOR), 0.25f));
						break;
					}
				} catch (ArrayIndexOutOfBoundsException ignore) {
				}
			}
			Iterator<BlockPos> i0 = BlockPos.getAllInBoxMutable(blockPosIn.add(3, 2, 3), blockPosIn.add(-3, -2, -3)).iterator();
			while (i0.hasNext()) {
				try {
					BlockPos b = i0.next();
					if (Taint.isTainted(worldIn.getBlockState(b).getBlock())) {
						if (worldIn.getBlockState(b).getBlock() != ArcanaBlocks.TAINTED_GRAVEL.get()) {
							callbackInfoReturnable.setReturnValue(UiUtil.blend(0x6e298e, MixinUtil.func_228359_a_(worldIn, blockPosIn, WATER_COLOR), 0.50f));
							break;
						}
					}
				} catch (ArrayIndexOutOfBoundsException ignore) {
				}
			}
			Iterator<BlockPos> i1 = BlockPos.getAllInBoxMutable(blockPosIn.add(2, 3, 2), blockPosIn.add(-2, -3, -2)).iterator();
			while (i1.hasNext()) {
				try {
					if (Taint.isTainted(worldIn.getBlockState(i1.next()).getBlock())) {
						callbackInfoReturnable.setReturnValue(UiUtil.blend(0x6e298e, MixinUtil.func_228359_a_(worldIn, blockPosIn, WATER_COLOR), 0.75f));
						break;
					}
				} catch (ArrayIndexOutOfBoundsException ignore) {
				}
			}
			int i = 1;//for (int i = 0; i < Minecraft.getInstance().gameSettings.biomeBlendRadius; i++) {
				Iterator<BlockPos> i2 = BlockPos.getAllInBoxMutable(blockPosIn.add(i, 3, i), blockPosIn.add(-i, -3, -i)).iterator();
				while (i2.hasNext()) {
					try {
						if (Taint.isTainted(worldIn.getBlockState(i2.next()).getBlock())) {
							callbackInfoReturnable.setReturnValue(UiUtil.blend(0x6e298e,MixinUtil.func_228359_a_(worldIn, blockPosIn, WATER_COLOR),/*Minecraft.getInstance().gameSettings.biomeBlendRadius/((float)100)*i)*/1f));
							break;
						}
					} catch (ArrayIndexOutOfBoundsException ignore) {
					}
				}
			//}
		}
	}
}
