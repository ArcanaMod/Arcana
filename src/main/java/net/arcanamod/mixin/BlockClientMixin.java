package net.arcanamod.mixin;

import net.arcanamod.blocks.bases.SolidVisibleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockClientMixin{
	
	// Fix Smokey Glass side rendering
	// Vanilla uses solid-ness for too many things
	@Inject(method = "shouldSideBeRendered",
	        at = @At("HEAD"),
	        cancellable = true)
	private static void fixSmokeyGlassSideRendering(BlockState adjState, IBlockReader state, BlockPos world, Direction face, CallbackInfoReturnable<Boolean> cir){
		BlockPos blockpos = world.offset(face);
		BlockState blockstate = state.getBlockState(blockpos);
		if(blockstate.getBlock() instanceof SolidVisibleBlock)
			cir.setReturnValue(adjState.getBlock() != blockstate.getBlock());
	}
}