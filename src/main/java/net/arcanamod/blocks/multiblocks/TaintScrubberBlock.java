package net.arcanamod.blocks.multiblocks;

import net.arcanamod.blocks.Taint;
import net.arcanamod.blocks.tiles.TaintScrubberTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class TaintScrubberBlock extends Block {
	public static final BooleanProperty SUPPORTED = BooleanProperty.create("supported"); // false by default

	public TaintScrubberBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TaintScrubberTileEntity();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	public BlockState getStateForPlacement(BlockItemUseContext context){
		return super.getStateForPlacement(context).with(SUPPORTED,false);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		super.fillStateContainer(builder);
		builder.add(SUPPORTED);
	}
}
