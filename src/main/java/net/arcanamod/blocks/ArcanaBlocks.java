package net.arcanamod.blocks;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.block.Block.Properties.create;
import static net.minecraft.block.material.Material.*;
import static net.minecraft.block.material.MaterialColor.BLACK;
import static net.minecraft.block.material.MaterialColor.SAND;

/**
 * Initialize Blocks here
 *
 * @author Atlas, Mozaran, Tea
 * @see ArcanaEntities
 * @see ArcanaItems
 */
@SuppressWarnings("unused")
public class ArcanaBlocks{
	
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Arcana.MODID);
	
	// General
	public static final RegistryObject<Block> ARCANE_STONE = BLOCKS.register("arcane_stone", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> ARCANE_STONE_SLAB = BLOCKS.register("arcane_stone_slab", () -> new SlabBlock(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> ARCANE_STONE_STAIRS = BLOCKS.register("arcane_stone_stairs", () -> new StairsBlock(() -> ARCANE_STONE.get().getDefaultState(), create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> ARCANE_STONE_BRICKS = BLOCKS.register("arcane_stone_bricks", () -> new Block(create(ROCK)));
	public static final RegistryObject<Block> ARCANE_STONE_BRICKS_SLAB = BLOCKS.register("arcane_stone_bricks_slab", () -> new SlabBlock(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> ARCANE_STONE_BRICKS_STAIRS = BLOCKS.register("arcane_stone_bricks_stairs", () -> new StairsBlock(() -> ARCANE_STONE_BRICKS.get().getDefaultState(), create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> AMBER_ORE = BLOCKS.register("amber_ore", () -> new Block(create(ROCK).harvestLevel(1).hardnessAndResistance(3.0F,3.0F)));
	public static final RegistryObject<Block> INFUSION_ARCANE_STONE = BLOCKS.register("infusion_arcane_stone", () -> new Block(create(ROCK)));
	public static final RegistryObject<Block> MAGICAL_GRASS = BLOCKS.register("magical_grass", () -> new Block(create(Material.ORGANIC)));
	public static final RegistryObject<Block> TABLE = BLOCKS.register("table", () -> new WaterloggableBlock(create(Material.WOOD).notSolid()));
	
	// Blocks with function
	// public static final Block RESEARCH_TABLE = new BlockResearchTable();
	public static final RegistryObject<Block> NORMAL_NODE = BLOCKS.register("normal_node", () -> new NormalNodeBlock(create(BARRIER)));
	// public static final Block CRUCIBLE = new BlockCrucible("crucible").setCreativeTab(Arcana.TAB_ARCANA);
	
	// Logs
	//	public static final Block DAIR_LEAVES = new LeavesBase("dair_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> DAIR_LOG = BLOCKS.register("dair_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	//	public static final Block DAIR_PLANKS = new PlanksBase("dair_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DAIR_SAPLING = new DairSapling("dair_sapling", false, false).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DAIR_SLAB_DOUBLE = new DoubleSlabBase("dair_slab_double", Material.WOOD);
	//	public static final Block DAIR_STONE_SLAB_HALF = new HalfSlabBase("dair_slab", Material.WOOD, ArcanaBlocks.DAIR_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DAIR_STAIRS = new StairsBase("dair_stairs", DAIR_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> DEAD_LOG = BLOCKS.register("dead_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	//	public static final Block DEAD_PLANKS = new PlanksBase("dead_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DEAD_SLAB_DOUBLE = new DoubleSlabBase("dead_slab_double", Material.WOOD);
	//	public static final Block DEAD_STONE_SLAB_HALF = new HalfSlabBase("dead_slab", Material.WOOD, ArcanaBlocks.DEAD_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block DEAD_STAIRS = new StairsBase("dead_stairs", DEAD_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block EUCALYPTUS_LEAVES = new LeavesBase("eucalyptus_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> EUCALYPTUS_LOG = BLOCKS.register("eucalyptus_log", () -> new LogBlock(MaterialColor.PINK, create(WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	//	public static final Block EUCALYPTUS_PLANKS = new PlanksBase("eucalyptus_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block EUCALYPTUS_SAPLING = new EucalyptusSapling("eucalyptus_sapling", false, false).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block EUCALYPTUS_SLAB_DOUBLE = new DoubleSlabBase("eucalyptus_slab_double", Material.WOOD);
	//	public static final Block EUCALYPTUS_STONE_SLAB_HALF = new HalfSlabBase("eucalyptus_slab", Material.WOOD, ArcanaBlocks.GREATWOOD_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block EUCALYPTUS_STAIRS = new StairsBase("eucalyptus_stairs", EUCALYPTUS_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block GREATWOOD_LEAVES = new LeavesBase("greatwood_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> GREATWOOD_LOG = BLOCKS.register("greatwood_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	//	public static final Block GREATWOOD_PLANKS = new PlanksBase("greatwood_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block GREATWOOD_SAPLING = new GreatwoodSapling("greatwood_sapling", false, false).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block GREATWOOD_SLAB_DOUBLE = new DoubleSlabBase("greatwood_slab_double", Material.WOOD);
	//	public static final Block GREATWOOD_STONE_SLAB_HALF = new HalfSlabBase("greatwood_slab", Material.WOOD, ArcanaBlocks.GREATWOOD_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block GREATWOOD_STAIRS = new StairsBase("greatwood_stairs", GREATWOOD_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block HAWTHORN_LEAVES = new LeavesBase("hawthorn_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> HAWTHORN_LOG = BLOCKS.register("hawthorn_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	//	public static final Block HAWTHORN_PLANKS = new PlanksBase("hawthorn_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block HAWTHORN_SAPLING = new HawthornSapling("hawthorn_sapling", false, false).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block HAWTHORN_SLAB_DOUBLE = new DoubleSlabBase("hawthorn_slab_double", Material.WOOD);
	//	public static final Block HAWTHORN_STONE_SLAB_HALF = new HalfSlabBase("hawthorn_slab", Material.WOOD, ArcanaBlocks.HAWTHORN_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block HAWTHORN_STAIRS = new StairsBase("hawthorn_stairs", HAWTHORN_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block SILVERWOOD_LEAVES = new LeavesBase("silverwood_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> SILVERWOOD_LOG = BLOCKS.register("silverwood_log", () -> new LogBlock(SAND, create(WOOD, SAND).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	//	public static final Block SILVERWOOD_PLANKS = new PlanksBase("silverwood_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block SILVERWOOD_SAPLING = new SilverwoodSapling("silverwood_sapling").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block SILVERWOOD_SLAB_DOUBLE = new DoubleSlabBase("silverwood_slab_double", Material.WOOD);
	//	public static final Block SILVERWOOD_STONE_SLAB_HALF = new HalfSlabBase("silverwood_slab", Material.WOOD, ArcanaBlocks.HAWTHORN_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block SILVERWOOD_STAIRS = new StairsBase("silverwood_stairs", HAWTHORN_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> TRYPOPHOBIUS = BLOCKS.register("trypophobius_log", () -> new LogBlock(BLACK, create(WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	//	public static final Block TRYPOPHOBIUS_PLANKS = new PlanksBase("trypophobius_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block TRYPOPHOBIUS_SLAB_DOUBLE = new DoubleSlabBase("trypophobius_slab_double", Material.WOOD);
	//	public static final Block TRYPOPHOBIUS_STONE_SLAB_HALF = new HalfSlabBase("trypophobius_slab", Material.WOOD, ArcanaBlocks.TRYPOPHOBIUS_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block TRYPOPHOBIUS_STAIRS = new StairsBase("trypophobius_stairs", TRYPOPHOBIUS_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block WILLOW_LEAVES = new LeavesBase("willow_leaves").setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Block> WILLOW_LOG = BLOCKS.register("willow_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	//	public static final Block WILLOW_PLANKS = new PlanksBase("willow_planks", Material.WOOD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block WILLOW_SAPLING = new WillowSapling("willow_sapling", false, false).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block WILLOW_SLAB_DOUBLE = new DoubleSlabBase("willow_slab_double", Material.WOOD);
	//	public static final Block WILLOW_STONE_SLAB_HALF = new HalfSlabBase("willow_slab", Material.WOOD, ArcanaBlocks.WILLOW_SLAB_DOUBLE).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static final Block WILLOW_STAIRS = new StairsBase("willow_stairs", WILLOW_PLANKS.getDefaultState()).setCreativeTab(Arcana.TAB_ARCANA);
	
	// Compressed Resources
	public static final RegistryObject<Block> ARCANIUM_BLOCK = BLOCKS.register("arcanium_block", () -> new Block(create(IRON).hardnessAndResistance(6.0F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> THAUMIUM_BLOCK = BLOCKS.register("thaumium_block", () -> new Block(create(IRON).hardnessAndResistance(6.0F).sound(SoundType.METAL)));
}