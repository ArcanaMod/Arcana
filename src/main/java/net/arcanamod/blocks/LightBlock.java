package net.arcanamod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class LightBlock extends Block {
	public LightBlock(Block.Properties properties) {
		super(properties);
	}

	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.animateTick(stateIn, worldIn, pos, rand);
		
		if (rand.nextInt(2) == 0)
			worldIn.addParticle(ParticleTypes.FLAME, pos.getX(), pos.getY(), pos.getZ(), 0.0D, 0.0D, 0.0D);
	}
}
