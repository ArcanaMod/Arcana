package net.kineticdevelopment.arcana.common.objects.blocks.bases.untainted;

import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;

public class UntaintedGrassBase extends UntaintedBlockBase implements IHasModel {
    /**
     * Basic Untainted Grass Block. All untainted grass blocks should use this or extend it
     *
     * @author Mozaran
     * @see UntaintedBlockBase
     */
    public UntaintedGrassBase(String name, Material material) {
        super(name, material);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        ArrayList<IPlantable> plants = new ArrayList<IPlantable>();
        plants.add(Blocks.TALLGRASS);
        plants.add(Blocks.RED_FLOWER);
        plants.add(Blocks.YELLOW_FLOWER);
        return plants.contains(plantable);
    }
}
