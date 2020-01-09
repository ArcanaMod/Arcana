package net.kineticdevelopment.arcana.common.objects.blocks;

import java.util.Random;

import net.kineticdevelopment.arcana.common.init.BlockStateInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TaintedBlockBase extends BlockBase implements IHasModel {
	public static final PropertyBool FULLYTAINTED = BlockStateInit.FULLYTAINTED;

	public TaintedBlockBase(String name, Material material) {
		super(name, material);
		this.setDefaultState(this.getDefaultState().withProperty(BlockStateInit.FULLYTAINTED, false));
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        
    }
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
