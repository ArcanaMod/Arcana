package net.kineticdevelopment.arcana.common.worldgen.trees;

import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.kineticdevelopment.arcana.common.worldgen.GenerationUtilities;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Mozaran
 *
 * Used to generate hawthorn trees (WIP)
 */
public class HawthornGenerator extends WorldGenAbstractTree {
    private static final IBlockState DEFAULT_TRUNK = BlockInit.HAWTHORN_LOG.getDefaultState();
    private static final IBlockState DEFAULT_TAINTED_TRUNK = BlockInit.TAINTED_HAWTHORN_LOG.getDefaultState();
    private static final IBlockState DEFAULT_UNTAINTED_TRUNK = BlockInit.UNTAINTED_HAWTHORN_LOG.getDefaultState();
    private static final IBlockState DEFAULT_LEAVES = BlockInit.HAWTHORN_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    private static final IBlockState DEFAULT_TAINTED_LEAVES = BlockInit.TAINTED_HAWTHORN_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    private static final IBlockState DEFAULT_UNTAINTED_LEAVES = BlockInit.UNTAINTED_HAWTHORN_LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);

    private final IBlockState metaWood;
    private final IBlockState metaLeaves;

    private final int minTreeHeight = 7;

    public HawthornGenerator(boolean notify, boolean tainted) {
        this(notify, tainted, false);
    }

    public HawthornGenerator(boolean notify, boolean tainted, boolean untainted) {
        super(notify);
        if(tainted) {
            metaWood = untainted ? DEFAULT_UNTAINTED_TRUNK : DEFAULT_TAINTED_TRUNK;
            metaLeaves = untainted ? DEFAULT_UNTAINTED_LEAVES : DEFAULT_TAINTED_LEAVES;
        } else {
            metaWood = DEFAULT_TRUNK;
            metaLeaves = DEFAULT_LEAVES;
        }
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int minHeight = rand.nextInt(3) + minTreeHeight;

        // Check if tree fits in world
        if (position.getY() >= 1 && position.getY() + minHeight + 1 <= worldIn.getHeight())
        {
            if (!isSuitableLocation(worldIn, position, minHeight))
            {
                return false;
            }
            else
            {
                IBlockState state = worldIn.getBlockState(position.down());

                if (state.getBlock().canSustainPlant(state, worldIn, position.down(), EnumFacing.UP, (IPlantable) Blocks.SAPLING) && position.getY() < worldIn.getHeight() - minHeight - 1)
                {
                    state.getBlock().onPlantGrow(state, worldIn, position.down(), position);
                    generateLeaves(worldIn, position, minHeight, rand);
                    generateTrunk(worldIn, position, minHeight);
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }

    private void generateLeaves(World worldIn, BlockPos position, int height, Random parRandom)
    {
        // Test of spheres
        ArrayList<BlockPos> positions = GenerationUtilities.GenerateCircle(position.add(0,11,0), 3, GenerationUtilities.GenType.FULL);
        positions.addAll(GenerationUtilities.GenerateCircle(position.add(0,10,0),  5, GenerationUtilities.GenType.FULL));
        positions.addAll(GenerationUtilities.GenerateCircle(position.add(0,9,0),  7, GenerationUtilities.GenType.FULL));
        positions.addAll(GenerationUtilities.GenerateCircle(position.add(0,8,0),  9, GenerationUtilities.GenType.FULL));
        positions.addAll(GenerationUtilities.GenerateOval(position.add(0,15,0),  6,12, GenerationUtilities.GenType.FULL));
        positions.addAll(GenerationUtilities.GenerateCircle(position.add(0,20,0),  6, GenerationUtilities.GenType.FULL));
        positions.addAll(GenerationUtilities.GenerateCircle(position.add(0,19,0),  6, GenerationUtilities.GenType.THICK));
        positions.addAll(GenerationUtilities.GenerateOval(position.add(0,14,0),  6, 12, GenerationUtilities.GenType.THICK));
        positions.addAll(GenerationUtilities.GenerateOval(position.add(0,25,0),  12, 12, GenerationUtilities.GenType.THICK));


        for(BlockPos pos : positions) {
            setBlockAndNotifyAdequately(worldIn, pos, metaLeaves);
        }
    }

    private void generateTrunk(World worldIn, BlockPos position, int minHeight)
    {
        ArrayList<BlockPos> positions = GenerationUtilities.GenerateTrunk(position, position.add(3, 8, 0), 1);
        positions.addAll(GenerationUtilities.GenerateTrunk(position, position.add(-3, 8, 0), 1));
        positions.addAll(GenerationUtilities.GenerateTrunk(position, position.add(0, 8, 3), 1));
        positions.addAll(GenerationUtilities.GenerateTrunk(position, position.add(0, 8, -3), 1));

        for(BlockPos pos : positions) {
            setBlockAndNotifyAdequately(worldIn, pos, metaWood);
        }
    }

    private boolean isSuitableLocation(World worldIn, BlockPos position, int minHeight)
    {
        boolean isSuitableLocation = true;

        for (int checkY = position.getY(); checkY <= position.getY() + 1 + minHeight; ++checkY)
        {
            // Handle increasing space towards top of tree
            int extraSpaceNeeded = 1;
            // Handle base location
            if (checkY == position.getY())
            {
                extraSpaceNeeded = 0;
            }
            // Handle top location
            if (checkY >= position.getY() + 1 + minHeight - 2)
            {
                extraSpaceNeeded = 2;
            }

            BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

            for (int checkX = position.getX() - extraSpaceNeeded; checkX <= position.getX() + extraSpaceNeeded && isSuitableLocation; ++checkX)
            {
                for (int checkZ = position.getZ() - extraSpaceNeeded; checkZ <= position.getZ() + extraSpaceNeeded && isSuitableLocation; ++checkZ)
                {
                    isSuitableLocation = isReplaceable(worldIn,blockPos.setPos(checkX, checkY, checkZ));
                }
            }
        }

        return isSuitableLocation;
    }
}
