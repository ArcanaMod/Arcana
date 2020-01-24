package net.kineticdevelopment.arcana.common.init;

import net.kineticdevelopment.arcana.common.objects.blocks.*;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
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
	public static final Block TAINTED_CRUST = new TaintedBlockBase("tainted_crust", Material.ROCK).setCreativeTab(Main.TAB_TAINTARCANA);
	public static final Block TAINTED_GRAVEL = new TaintedBlockBase("tainted_gravel", Material.GROUND).setCreativeTab(Main.TAB_TAINTARCANA);
	public static final Block TAINTED_ROCK = new TaintedBlockBase("tainted_rock", Material.ROCK).setCreativeTab(Main.TAB_TAINTARCANA);
	public static final Block TAINTED_SOIL = new TaintedBlockBase("tainted_soil", Material.GROUND).setCreativeTab(Main.TAB_TAINTARCANA);
	public static final Block TAINTED_GRASS = new TaintedBlockBase("tainted_grass_block", Material.GRASS).setCreativeTab(Main.TAB_TAINTARCANA);
	public static final Block TAINTED_ACACIA_PLANKS = new TaintedBlockBase("tainted_acacia_planks", Material.GRASS).setCreativeTab(Main.TAB_TAINTARCANA);
	public static final Block TAINTED_AMBER_ORE = new TaintedBlockBase("tainted_amber_ore", Material.ROCK).setCreativeTab(Main.TAB_TAINTARCANA);
	public static final Block TAINTED_ARCANIUM_BLOCK = new TaintedBlockBase("tainted_arcanium_block", Material.ROCK).setCreativeTab(Main.TAB_TAINTARCANA);
	public static final Block TAINTED_ARCANIUM_ORE = new TaintedBlockBase("tainted_arcanium_ore", Material.ROCK).setCreativeTab(Main.TAB_TAINTARCANA);

	//Tainted Logs
	public static final Block STRIPPED_TAINTED_GREATWOOD_LOG = new TaintedBlockBase("stripped_tainted_greatwood_log", Material.WOOD).setCreativeTab(Main.TAB_TAINTARCANA);
	public static final Block STRIPPED_TAINTED_HAWTHORN_LOG = new TaintedBlockBase("stripped_tainted_hawthorn_log", Material.WOOD).setCreativeTab(Main.TAB_TAINTARCANA);
	public static final Block STRIPPED_TAINTED_WILLOW_LOG = new TaintedBlockBase("stripped_tainted_willow_log", Material.WOOD).setCreativeTab(Main.TAB_TAINTARCANA);
	public static final Block TAINTED_ACACIA_LOG = new TaintedBlockBase("tainted_acacia_log", Material.WOOD).setCreativeTab(Main.TAB_TAINTARCANA);

	//Tainted Stairs
	public static final Block TAINTED_ACACIA_STAIRS = new StairsBase("tainted_acacia_stairs", TAINTED_ACACIA_PLANKS.getDefaultState()).setCreativeTab(Main.TAB_TAINTARCANA);

	//Metal Based Blocks
	public static final Block THAUMIUM_BLOCK = new BlockBase("thaumium_block", Material.IRON).setCreativeTab(Main.TAB_ARCANA);
	public static final Block ARCANIUM_BLOCK = new BlockBase("arcanium_block", Material.IRON).setCreativeTab(Main.TAB_ARCANA);

	//Stone Based Blocks
	public static final Block ARCANE_STONE = new BlockBase("arcane_stone", Material.ROCK).setCreativeTab(Main.TAB_ARCANA);
	public static final Block AMBER_ORE = new BlockBase("amber_ore", Material.ROCK).setCreativeTab(Main.TAB_ARCANA);
	public static final Block ARCANE_STONE_BRICKS = new BlockBase("arcane_stone_bricks", Material.ROCK).setCreativeTab(Main.TAB_ARCANA);
	public static final Block INFUSION_ARCANE_STONE = new BlockBase("infusion_arcane_stone", Material.ROCK).setCreativeTab(Main.TAB_ARCANA);

	//Grass
	public static final Block MAGICAL_GRASS = new BlockBase("magical_grass", Material.GRASS).setCreativeTab(Main.TAB_ARCANA);

	//Logs
	public static final Block DAIR_LOG = new LogsBase("dair_log", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);
	public static final Block DEAD_LOG = new LogsBase("dead_log", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);
	public static final Block GREATWOOD_LOG = new LogsBase("greatwood_log", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);
	public static final Block HAWTHORN_LOG = new LogsBase("hawthorn_log", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);
	public static final Block STRIPPED_DAIR_LOG = new LogsBase("stripped_dair_log", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);
	public static final Block STRIPPED_GREATWOOD_LOG = new LogsBase("stripped_greatwood_log", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);
	public static final Block STRIPPED_HAWTHORN_LOG = new LogsBase("stripped_hawthorn_log", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);
	public static final Block STRIPPED_WILLOW_LOG = new LogsBase("stripped_willow_log", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);


	//Wood
	public static final Block DAIR_PLANKS = new PlanksBase("dair_planks", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);
	public static final Block DEAD_PLANKS = new PlanksBase("dead_planks", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);
	public static final Block GREATWOOD_PLANKS = new PlanksBase("greatwood_planks", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);
	public static final Block HAWTHORN_PLANKS = new PlanksBase("hawthorn_planks", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);

	//Stairs
	public static final Block ARCANE_STONE_STAIRS = new StairsBase("arcane_stone_stairs", ARCANE_STONE.getDefaultState()).setCreativeTab(Main.TAB_ARCANA);
	public static final Block ARCANE_STONE_BRICKS_STAIRS = new StairsBase("arcane_stone_bricks_stairs", ARCANE_STONE_BRICKS.getDefaultState()).setCreativeTab(Main.TAB_ARCANA);
	public static final Block DAIR_STAIRS = new StairsBase("dair_stairs", DAIR_PLANKS.getDefaultState()).setCreativeTab(Main.TAB_ARCANA);
	public static final Block DEAD_STAIRS = new StairsBase("dead_stairs", DEAD_PLANKS.getDefaultState()).setCreativeTab(Main.TAB_ARCANA);
	public static final Block GREATWOOD_STAIRS = new StairsBase("greatwood_stairs", GREATWOOD_PLANKS.getDefaultState()).setCreativeTab(Main.TAB_ARCANA);
	public static final Block HAWTHORN_STAIRS = new StairsBase("hawthorn_stairs", HAWTHORN_PLANKS.getDefaultState()).setCreativeTab(Main.TAB_ARCANA);





	}

