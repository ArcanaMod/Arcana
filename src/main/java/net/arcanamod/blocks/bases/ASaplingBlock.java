package net.arcanamod.blocks.bases;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.*;
import net.minecraft.block.trees.Tree;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * SaplingBlock's constructor is protected, for some reason.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ASaplingBlock extends SaplingBlock{
	
	public ASaplingBlock(Tree tree, Block.Properties properties){
		super(tree, properties);
	}
}