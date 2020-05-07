package net.arcanamod.blocks;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.bases.ArcanaSaplingBlock;
import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.worldgen.DummyTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.block.Block.Properties.create;
import static net.minecraft.block.Block.Properties.from;
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
	public static final RegistryObject<Block> ARCANE_STONE_BRICKS = BLOCKS.register("arcane_stone_bricks", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> ARCANE_STONE_BRICKS_SLAB = BLOCKS.register("arcane_stone_bricks_slab", () -> new SlabBlock(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> ARCANE_STONE_BRICKS_STAIRS = BLOCKS.register("arcane_stone_bricks_stairs", () -> new StairsBlock(() -> ARCANE_STONE_BRICKS.get().getDefaultState(), create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> AMBER_ORE = BLOCKS.register("amber_ore", () -> new Block(create(ROCK).harvestLevel(1).hardnessAndResistance(3.0F,3.0F)));
	public static final RegistryObject<Block> INFUSION_ARCANE_STONE = BLOCKS.register("infusion_arcane_stone", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> MAGICAL_GRASS = BLOCKS.register("magical_grass", () -> new Block(from(Blocks.GRASS_BLOCK)));
	public static final RegistryObject<Block> TABLE = BLOCKS.register("table", () -> new WaterloggableBlock(create(WOOD).hardnessAndResistance(2).notSolid()));
	
	// Blocks with function
	public static final RegistryObject<Block> JAR = BLOCKS.register("jar", () -> new BlockJar(create(GLASS)));
	// public static final Block RESEARCH_TABLE = new BlockResearchTable();
	public static final RegistryObject<Block> NORMAL_NODE = BLOCKS.register("normal_node", () -> new NormalNodeBlock(create(BARRIER)));
	// public static final Block CRUCIBLE = new BlockCrucible("crucible").setCreativeTab(Arcana.TAB_ARCANA);
	
	// Woods
	// Dair
	public static final RegistryObject<Block> DAIR_LEAVES = BLOCKS.register("dair_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> DAIR_LOG = BLOCKS.register("dair_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DAIR_PLANKS = BLOCKS.register("dair_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DAIR_SAPLING = BLOCKS.register("dair_sapling", () -> new ArcanaSaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> DAIR_SLAB = BLOCKS.register("dair_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DAIR_STAIRS = BLOCKS.register("dair_stairs", () -> new StairsBlock(() -> DAIR_PLANKS.get().getDefaultState(), create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	// Dead wood
	public static final RegistryObject<Block> DEAD_LOG = BLOCKS.register("dead_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_PLANKS = BLOCKS.register("dead_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_SLAB = BLOCKS.register("dead_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_STAIRS = BLOCKS.register("dead_stairs", () -> new StairsBlock(() -> DEAD_PLANKS.get().getDefaultState(), create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	// Eucalyptus
	public static final RegistryObject<Block> EUCALYPTUS_LEAVES = BLOCKS.register("eucalyptus_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> EUCALYPTUS_LOG = BLOCKS.register("eucalyptus_log", () -> new LogBlock(MaterialColor.PINK, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> EUCALYPTUS_PLANKS = BLOCKS.register("eucalyptus_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> EUCALYPTUS_SAPLING = BLOCKS.register("eucalyptus_sapling", () -> new ArcanaSaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> EUCALYPTUS_SLAB = BLOCKS.register("eucalyptus_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> EUCALYPTUS_STAIRS = BLOCKS.register("eucalyptus_stairs", () -> new StairsBlock(() -> EUCALYPTUS_PLANKS.get().getDefaultState(), create(WOOD, MaterialColor.PINK).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	// Greatwood
	public static final RegistryObject<Block> GREATWOOD_LEAVES = BLOCKS.register("greatwood_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> GREATWOOD_LOG = BLOCKS.register("greatwood_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GREATWOOD_PLANKS = BLOCKS.register("greatwood_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GREATWOOD_SAPLING = BLOCKS.register("greatwood_sapling", () -> new ArcanaSaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> GREATWOOD_SLAB = BLOCKS.register("greatwood_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GREATWOOD_STAIRS = BLOCKS.register("greatwood_stairs", () -> new StairsBlock(() -> GREATWOOD_PLANKS.get().getDefaultState(), create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	// Hawthorn
	public static final RegistryObject<Block> HAWTHORN_LEAVES = BLOCKS.register("hawthorn_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> HAWTHORN_LOG = BLOCKS.register("hawthorn_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HAWTHORN_PLANKS = BLOCKS.register("hawthorn_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HAWTHORN_SAPLING = BLOCKS.register("hawthorn_sapling", () -> new ArcanaSaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> HAWTHORN_SLAB = BLOCKS.register("hawthorn_slab", () -> new SlabBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HAWTHORN_STAIRS = BLOCKS.register("hawthorn_stairs", () -> new StairsBlock(() -> HAWTHORN_PLANKS.get().getDefaultState(), create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	// Silverwood
	public static final RegistryObject<Block> SILVERWOOD_LEAVES = BLOCKS.register("silverwood_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> SILVERWOOD_LOG = BLOCKS.register("silverwood_log", () -> new LogBlock(SAND, create(WOOD, SAND).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SILVERWOOD_PLANKS = BLOCKS.register("silverwood_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SILVERWOOD_SAPLING = BLOCKS.register("silverwood_sapling", () -> new ArcanaSaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> SILVERWOOD_SLAB = BLOCKS.register("silverwood_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SILVERWOOD_STAIRS = BLOCKS.register("silverwood_stairs", () -> new StairsBlock(() -> SILVERWOOD_PLANKS.get().getDefaultState(), create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	// Trypophobius Wood
	public static final RegistryObject<Block> TRYPOPHOBIUS_LOG = BLOCKS.register("trypophobius_log", () -> new LogBlock(BLACK, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TRYPOPHOBIUS_PLANKS = BLOCKS.register("trypophobius_planks", () -> new Block(create(WOOD, BLACK).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TRYPOPHOBIUS_SLAB = BLOCKS.register("trypophobius_slab", () -> new SlabBlock(create(WOOD, BLACK).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TRYPOPHOBIUS_STAIRS = BLOCKS.register("trypophobius_stairs", () -> new StairsBlock(() -> TRYPOPHOBIUS_PLANKS.get().getDefaultState(), create(WOOD, BLACK).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	// Willow
	public static final RegistryObject<Block> WILLOW_LEAVES = BLOCKS.register("willow_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> WILLOW_LOG = BLOCKS.register("willow_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> WILLOW_PLANKS = BLOCKS.register("willow_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> WILLOW_SAPLING = BLOCKS.register("willow_sapling", () -> new ArcanaSaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> WILLOW_SLAB = BLOCKS.register("willow_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> WILLOW_STAIRS = BLOCKS.register("willow_stairs", () -> new StairsBlock(() -> WILLOW_PLANKS.get().getDefaultState(), create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	// Compressed Resources
	public static final RegistryObject<Block> ARCANIUM_BLOCK = BLOCKS.register("arcanium_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> THAUMIUM_BLOCK = BLOCKS.register("thaumium_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
}