package net.arcanamod.blocks;

import net.arcanamod.blocks.tiles.WardenedBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.List;

public class WardenedBlock extends Block {
	protected WardenedBlockTileEntity t;

	public WardenedBlock(Block.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		t = new WardenedBlockTileEntity();
		return t;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (t != null) return t.getState().orElse(Blocks.AIR.getDefaultState()).getBlock().getDrops(state, builder); else return super.getDrops(state, builder);
	}

	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
		super.onPlayerDestroy(worldIn, pos, state);
		//worldIn.setBlockState(pos,t.getState().orElse(Blocks.AIR.getDefaultState()),3);
	}
}
