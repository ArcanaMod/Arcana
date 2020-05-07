package net.arcanamod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;

import static net.minecraft.state.properties.BlockStateProperties.LEVEL_0_8;

public class BlockAspectBookshelf extends Block
{
    public BlockAspectBookshelf(Properties properties)
    {
        super(properties);
        setDefaultState(stateContainer.getBaseState().with(LEVEL_0_8, 0));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(LEVEL_0_8);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(LEVEL_0_8, 0);
    }
}
