package net.arcanamod.blocks.multiblocks;

import net.arcanamod.blocks.bases.GroupedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/* You may be wondering why Cores and Components don't share inheritance.
 * Well, even though they represent the same block, Cores resemble Cores
 * and Components resemble Components. To exercise this further, it would
 * be wise to create base classes meant for Cores and Components.
 * I didn't do this because I feared replacing the Waterloggable Block
 * inheritance in Research Table. It was completely arbitrary, trust me.
 */
public interface StaticComponent {

    boolean isCore(BlockPos pos, BlockState state);

    BlockPos getCorePos(BlockPos pos, BlockState state);
}
