package net.kineticdevelopment.arcana.common.init;

import net.kineticdevelopment.arcana.common.objects.blocks.BlockBase;
import net.kineticdevelopment.arcana.common.objects.blocks.TaintedBlockBase;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;
/**
 * Initialize Blocks here
 * @author Atlas
 * @see BlockStateInit
 * @see EntityInit
 * @see ItemInit
 */
public class BlockInit {
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	//Tainted
	public static final Block TAINTED_CRUST = new TaintedBlockBase("tainted_crust", Material.ROCK);
	public static final Block TAINTED_GRAVEL = new TaintedBlockBase("tainted_gravel", Material.GROUND);
	public static final Block TAINTED_ROCK = new TaintedBlockBase("tainted_rock", Material.ROCK);
	public static final Block TAINTED_SOIL = new TaintedBlockBase("tainted_soil", Material.GROUND);
	public static final Block TAINTED_GRASS = new TaintedBlockBase("tainted_grass_block", Material.GRASS);

	public static final Block THAUMIUM_BLOCK = new BlockBase("thaumium_block", Material.IRON).setCreativeTab(Main.TAB_ARCANA);
	public static final Block ARCANIUM_BLOCK = new BlockBase("arcanium_block", Material.IRON).setCreativeTab(Main.TAB_ARCANA);

}
