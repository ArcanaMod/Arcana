package net.arcanamod.blocks;

import net.arcanamod.Arcana;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.block.Block.Properties.create;
import static net.minecraft.block.material.Material.BARRIER;
import static net.minecraft.block.material.Material.ROCK;

/**
 * Initialize Blocks here
 *
 * @author Atlas, Mozaran, Tea
 * @see ArcanaEntities
 * @see ArcanaItems
 */
public class ArcanaBlocks{
	
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Arcana.MODID);
	
	// General
	// public static final Block ARCANE_STONE = new BlockBase("arcane_stone", create(ROCK))/*.setCreativeTab(Arcana.TAB_ARCANA)*/;
	public static final RegistryObject<Block> ARCANE_STONE = BLOCKS.register("arcane_stone", () -> new Block(create(ROCK)));
	//	public static final Block ARCANE_STONE_SLAB_DOUBLE = new DoubleSlabBase("arcane_stone_slab_double", ROCK);
	//	public static final Block ARCANE_STONE_SLAB_HALF = new HalfSlabBase("arcane_stone_slab", ROCK, ArcanaBlocks.ARCANE_STONE_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block ARCANE_STONE_STAIRS = new StairsBase("arcane_stone_stairs", ARCANE_STONE.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block ARCANE_STONE_BRICKS = new BlockBase("arcane_stone_bricks", ROCK).setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> ARCANE_STONE_BRICKS = BLOCKS.register("arcane_stone_bricks", () -> new Block(create(ROCK)));
	//	public static final Block ARCANE_STONE_BRICKS_SLAB_DOUBLE = new DoubleSlabBase("arcane_stone_bricks_slab_double", ROCK);
	//	public static final Block ARCANE_STONE_BRICKS_SLAB_HALF = new HalfSlabBase("arcane_stone_bricks_slab", ROCK, ArcanaBlocks.ARCANE_STONE_BRICKS_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block ARCANE_STONE_BRICKS_STAIRS = new StairsBase("arcane_stone_bricks_stairs", ARCANE_STONE_BRICKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block AMBER_ORE = new BlockAmberOre().setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> AMBER_ORE = BLOCKS.register("amber_ore", () -> new Block(create(ROCK).harvestLevel(1).hardnessAndResistance(3.0F,3.0F)));
	//	public static final Block INFUSION_ARCANE_STONE = new BlockBase("infusion_arcane_stone", ROCK).setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> INFUSION_ARCANE_STONE = BLOCKS.register("infusion_arcane_stone", () -> new Block(create(ROCK)));
	//	public static final Block MAGICAL_GRASS = new BlockBase("magical_grass", Material.GRASS).setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> MAGICAL_GRASS = BLOCKS.register("magical_grass", () -> new Block(create(Material.ORGANIC)));
	//	// public static final Block TABLE = new BlockBase("table", Material.WOOD).setCreativeTab(Main.TAB_ARCANA);
	//
	//	//Blocks with function
	//	public static final Block RESEARCH_TABLE = new BlockResearchTable();
	//	public static final Block NORMAL_NODE = new BlockNormalNode().setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> NORMAL_NODE = BLOCKS.register("normal_node", () -> new NormalNodeBlock(create(BARRIER)));
	//	public static final Block CRUCIBLE = new BlockCrucible("crucible").setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	// Logs
	//	public static final Block DAIR_LEAVES = new LeavesBase("dair_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DAIR_LOG = new LogBase("dair_log").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DAIR_PLANKS = new PlanksBase("dair_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DAIR_SAPLING = new DairSapling("dair_sapling", false, false).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DAIR_SLAB_DOUBLE = new DoubleSlabBase("dair_slab_double", Material.WOOD);
	//	public static final Block DAIR_STONE_SLAB_HALF = new HalfSlabBase("dair_slab", Material.WOOD, ArcanaBlocks.DAIR_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DAIR_STAIRS = new StairsBase("dair_stairs", DAIR_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DEAD_LOG = new LogBase("dead_log").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DEAD_PLANKS = new PlanksBase("dead_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DEAD_SLAB_DOUBLE = new DoubleSlabBase("dead_slab_double", Material.WOOD);
	//	public static final Block DEAD_STONE_SLAB_HALF = new HalfSlabBase("dead_slab", Material.WOOD, ArcanaBlocks.DEAD_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DEAD_STAIRS = new StairsBase("dead_stairs", DEAD_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block EUCALYPTUS_LEAVES = new LeavesBase("eucalyptus_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block EUCALYPTUS_LOG = new LogBase("eucalyptus_log").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block EUCALYPTUS_PLANKS = new PlanksBase("eucalyptus_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block EUCALYPTUS_SAPLING = new EucalyptusSapling("eucalyptus_sapling", false, false).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block EUCALYPTUS_SLAB_DOUBLE = new DoubleSlabBase("eucalyptus_slab_double", Material.WOOD);
	//	public static final Block EUCALYPTUS_STONE_SLAB_HALF = new HalfSlabBase("eucalyptus_slab", Material.WOOD, ArcanaBlocks.GREATWOOD_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block EUCALYPTUS_STAIRS = new StairsBase("eucalyptus_stairs", EUCALYPTUS_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block GREATWOOD_LEAVES = new LeavesBase("greatwood_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block GREATWOOD_LOG = new LogBase("greatwood_log").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block GREATWOOD_PLANKS = new PlanksBase("greatwood_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block GREATWOOD_SAPLING = new GreatwoodSapling("greatwood_sapling", false, false).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block GREATWOOD_SLAB_DOUBLE = new DoubleSlabBase("greatwood_slab_double", Material.WOOD);
	//	public static final Block GREATWOOD_STONE_SLAB_HALF = new HalfSlabBase("greatwood_slab", Material.WOOD, ArcanaBlocks.GREATWOOD_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block GREATWOOD_STAIRS = new StairsBase("greatwood_stairs", GREATWOOD_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block HAWTHORN_LEAVES = new LeavesBase("hawthorn_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block HAWTHORN_LOG = new LogBase("hawthorn_log").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block HAWTHORN_PLANKS = new PlanksBase("hawthorn_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block HAWTHORN_SAPLING = new HawthornSapling("hawthorn_sapling", false, false).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block HAWTHORN_SLAB_DOUBLE = new DoubleSlabBase("hawthorn_slab_double", Material.WOOD);
	//	public static final Block HAWTHORN_STONE_SLAB_HALF = new HalfSlabBase("hawthorn_slab", Material.WOOD, ArcanaBlocks.HAWTHORN_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block HAWTHORN_STAIRS = new StairsBase("hawthorn_stairs", HAWTHORN_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block SILVERWOOD_LEAVES = new LeavesBase("silverwood_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block SILVERWOOD_LOG = new LogBase("silverwood_log").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block SILVERWOOD_PLANKS = new PlanksBase("silverwood_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block SILVERWOOD_SAPLING = new SilverwoodSapling("silverwood_sapling").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block SILVERWOOD_SLAB_DOUBLE = new DoubleSlabBase("silverwood_slab_double", Material.WOOD);
	//	public static final Block SILVERWOOD_STONE_SLAB_HALF = new HalfSlabBase("silverwood_slab", Material.WOOD, ArcanaBlocks.HAWTHORN_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block SILVERWOOD_STAIRS = new StairsBase("silverwood_stairs", HAWTHORN_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block TRYPOPHOBIUS_LOG = new LogBase("trypophobius_log").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block TRYPOPHOBIUS_PLANKS = new PlanksBase("trypophobius_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block TRYPOPHOBIUS_SLAB_DOUBLE = new DoubleSlabBase("trypophobius_slab_double", Material.WOOD);
	//	public static final Block TRYPOPHOBIUS_STONE_SLAB_HALF = new HalfSlabBase("trypophobius_slab", Material.WOOD, ArcanaBlocks.TRYPOPHOBIUS_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block TRYPOPHOBIUS_STAIRS = new StairsBase("trypophobius_stairs", TRYPOPHOBIUS_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block WILLOW_LEAVES = new LeavesBase("willow_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block WILLOW_LOG = new LogBase("willow_log").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block WILLOW_PLANKS = new PlanksBase("willow_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block WILLOW_SAPLING = new WillowSapling("willow_sapling", false, false).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block WILLOW_SLAB_DOUBLE = new DoubleSlabBase("willow_slab_double", Material.WOOD);
	//	public static final Block WILLOW_STONE_SLAB_HALF = new HalfSlabBase("willow_slab", Material.WOOD, ArcanaBlocks.WILLOW_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block WILLOW_STAIRS = new StairsBase("willow_stairs", WILLOW_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	// Compressed Resources
	//	public static final Block ARCANIUM_BLOCK = new BlockBase("arcanium_block", Material.IRON).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block THAUMIUM_BLOCK = new BlockBase("thaumium_block", Material.IRON).setCreativeTab(Arcana.TAB_ARCANA);
}