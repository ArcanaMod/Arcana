package net.arcanamod.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.level.ColorResolver;

public class MixinUtil {
	public static int func_228359_a_(ILightReader worldIn, BlockPos blockPosIn, ColorResolver colorResolverIn) {
		return worldIn.getBlockColor(blockPosIn, colorResolverIn);
	}
}
