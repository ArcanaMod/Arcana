package net.arcanamod.blocks.bases.tainted;

import net.arcanamod.util.IHasModel;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;

/**
 * Basic Tainted Grass Block. All tainted grass blocks should use this or extend it
 *
 * @author Mozaran
 * @see TaintedBlockBase
 */
public class TaintedGrassBase extends TaintedBlockBase implements IHasModel{
	public TaintedGrassBase(String name, Material material){
		super(name, material);
	}
	
	@Override
	public boolean canSustainPlant(BlockState state, IBlockAccess world, BlockPos pos, Direction direction, IPlantable plantable){
		ArrayList<IPlantable> plants = new ArrayList<IPlantable>();
		plants.add(Blocks.TALLGRASS);
		plants.add(Blocks.RED_FLOWER);
		plants.add(Blocks.YELLOW_FLOWER);
		return plants.contains(plantable);
	}
}
