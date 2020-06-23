package net.arcanamod.blocks;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.bases.*;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.worldgen.DummyTree;
import net.minecraft.block.*;
import net.minecraft.block.PressurePlateBlock.Sensitivity;
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
 * @see ArcanaItems
 */
@SuppressWarnings("unused")
public class ArcanaBlocks{
	
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Arcana.MODID);
	
	// General
	// Arcane Stone
	public static final RegistryObject<Block> ARCANE_STONE = BLOCKS.register("arcane_stone", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<SlabBlock> ARCANE_STONE_SLAB = BLOCKS.register("arcane_stone_slab", () -> new SlabBlock(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<StairsBlock> ARCANE_STONE_STAIRS = BLOCKS.register("arcane_stone_stairs", () -> new StairsBlock(() -> ARCANE_STONE.get().getDefaultState(), create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<PressurePlateBlock> ARCANE_STONE_PRESSURE_PLATE = BLOCKS.register("arcane_stone_pressure_plate", () -> new APressurePlateBlock(Sensitivity.MOBS, create(ROCK).hardnessAndResistance(.5f).doesNotBlockMovement()));
	public static final RegistryObject<WallBlock> ARCANE_STONE_WALL = BLOCKS.register("arcane_stone_wall", () -> new WallBlock(from(ARCANE_STONE.get())));
	
	// Arcane Stone Bricks
	public static final RegistryObject<Block> ARCANE_STONE_BRICKS = BLOCKS.register("arcane_stone_bricks", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<SlabBlock> ARCANE_STONE_BRICKS_SLAB = BLOCKS.register("arcane_stone_bricks_slab", () -> new SlabBlock(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<StairsBlock> ARCANE_STONE_BRICKS_STAIRS = BLOCKS.register("arcane_stone_bricks_stairs", () -> new StairsBlock(() -> ARCANE_STONE_BRICKS.get().getDefaultState(), create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<PressurePlateBlock> ARCANE_STONE_BRICKS_PRESSURE_PLATE = BLOCKS.register("arcane_stone_bricks_pressure_plate", () -> new APressurePlateBlock(Sensitivity.MOBS, create(ROCK).hardnessAndResistance(.5f).doesNotBlockMovement()));
	public static final RegistryObject<WallBlock> ARCANE_STONE_BRICKS_WALL = BLOCKS.register("arcane_stone_bricks_wall", () -> new WallBlock(from(ARCANE_STONE_BRICKS.get())));
	
	// Dungeon Bricks
	public static final RegistryObject<Block> DUNGEON_BRICKS = BLOCKS.register("dungeon_bricks", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<SlabBlock> DUNGEON_BRICKS_SLAB = BLOCKS.register("dungeon_bricks_slab", () -> new SlabBlock(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<StairsBlock> DUNGEON_BRICKS_STAIRS = BLOCKS.register("dungeon_bricks_stairs", () -> new StairsBlock(() -> DUNGEON_BRICKS.get().getDefaultState(), create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<PressurePlateBlock> DUNGEON_BRICKS_PRESSURE_PLATE = BLOCKS.register("dungeon_bricks_pressure_plate", () -> new APressurePlateBlock(Sensitivity.MOBS, create(ROCK).hardnessAndResistance(.5f).doesNotBlockMovement()));
	public static final RegistryObject<WallBlock> DUNGEON_BRICKS_WALL = BLOCKS.register("dungeon_bricks_wall", () -> new WallBlock(from(DUNGEON_BRICKS.get())));
	
	// Cracked Dungeon Bricks
	public static final RegistryObject<Block> CRACKED_DUNGEON_BRICKS = BLOCKS.register("cracked_dungeon_bricks", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<SlabBlock> CRACKED_DUNGEON_BRICKS_SLAB = BLOCKS.register("cracked_dungeon_bricks_slab", () -> new SlabBlock(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<StairsBlock> CRACKED_DUNGEON_BRICKS_STAIRS = BLOCKS.register("cracked_dungeon_bricks_stairs", () -> new StairsBlock(() -> CRACKED_DUNGEON_BRICKS.get().getDefaultState(), create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<PressurePlateBlock> CRACKED_DUNGEON_BRICKS_PRESSURE_PLATE = BLOCKS.register("cracked_dungeon_bricks_pressure_plate", () -> new APressurePlateBlock(Sensitivity.MOBS, create(ROCK).hardnessAndResistance(.5f).doesNotBlockMovement()));
	public static final RegistryObject<WallBlock> CRACKED_DUNGEON_BRICKS_WALL = BLOCKS.register("cracked_dungeon_bricks_wall", () -> new WallBlock(from(CRACKED_DUNGEON_BRICKS.get())));
	
	// Mossy Dungeon Bricks
	public static final RegistryObject<Block> MOSSY_DUNGEON_BRICKS = BLOCKS.register("mossy_dungeon_bricks", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<SlabBlock> MOSSY_DUNGEON_BRICKS_SLAB = BLOCKS.register("mossy_dungeon_bricks_slab", () -> new SlabBlock(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<StairsBlock> MOSSY_DUNGEON_BRICKS_STAIRS = BLOCKS.register("mossy_dungeon_bricks_stairs", () -> new StairsBlock(() -> MOSSY_DUNGEON_BRICKS.get().getDefaultState(), create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<PressurePlateBlock> MOSSY_DUNGEON_BRICKS_PRESSURE_PLATE = BLOCKS.register("mossy_dungeon_bricks_pressure_plate", () -> new APressurePlateBlock(Sensitivity.MOBS, create(ROCK).hardnessAndResistance(.5f).doesNotBlockMovement()));
	public static final RegistryObject<WallBlock> MOSSY_DUNGEON_BRICKS_WALL = BLOCKS.register("mossy_dungeon_bricks_wall", () -> new WallBlock(from(MOSSY_DUNGEON_BRICKS.get())));

	//Pridestone
	public static final RegistryObject<Block> PRIDESTONE_BRICKS = BLOCKS.register("pridestone_bricks", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> PRIDESTONE_SMALL_BRICKS = BLOCKS.register("pridestone_small_bricks", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> PRIDESTONE_PILLAR = BLOCKS.register("pridestone_pillar", () -> new PillarBlock(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> PRIDESTONE_PILLAR_COAL = BLOCKS.register("pridestone_pillar_coal", () -> new PillarBlock(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> PRIDESTONE = BLOCKS.register("pridestone", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> SMOOTH_PRIDESTONE = BLOCKS.register("smooth_pridestone", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> PRIDESTONE_TILE = BLOCKS.register("pridestone_tile", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> SMOOTH_PRIDESTONE_TILE = BLOCKS.register("smooth_pridestone_tile", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> WET_PRIDESTONE = BLOCKS.register("wet_pridestone", () -> new Block(create(ROCK).sound(SoundType.WET_GRASS).hardnessAndResistance(0.4F, 2.0F)));
	public static final RegistryObject<Block> WET_SMOOTH_PRIDESTONE = BLOCKS.register("wet_smooth_pridestone", () -> new Block(create(ROCK).sound(SoundType.WET_GRASS).hardnessAndResistance(0.4F, 2.0F)));

	//Prideful Things
	public static final RegistryObject<Block> PRIDEFUL_GOLD_PILLAR = BLOCKS.register("prideful_gold_pillar", () -> new PillarBlock(create(IRON).sound(SoundType.METAL).hardnessAndResistance(4.0F, 8.0F)));
	public static final RegistryObject<Block> PRIDECLAY = BLOCKS.register("prideclay", () -> new Block(create(CLAY).sound(SoundType.SAND).hardnessAndResistance(4.0F, 8.0F)));
	public static final RegistryObject<Block> GILDED_PRIDECLAY = BLOCKS.register("gilded_prideclay", () -> new Block(create(IRON).sound(SoundType.METAL).hardnessAndResistance(4.0F, 8.0F)));
	public static final RegistryObject<Block> CARVED_PRIDEFUL_GOLD_BLOCK = BLOCKS.register("carved_prideful_gold_block", () -> new Block(create(IRON).sound(SoundType.METAL).hardnessAndResistance(4.0F, 8.0F)));
	public static final RegistryObject<Block> CHISELED_PRIDEFUL_GOLD_BLOCK = BLOCKS.register("chiseled_prideful_gold_block", () -> new Block(create(IRON).sound(SoundType.METAL).hardnessAndResistance(4.0F, 8.0F)));
	public static final RegistryObject<Block> PRIDEFUL_GOLD_BLOCK = BLOCKS.register("prideful_gold_block", () -> new Block(create(IRON).sound(SoundType.METAL).hardnessAndResistance(4.0F, 8.0F)));
	public static final RegistryObject<Block> PRIDEFUL_GOLD_TILE = BLOCKS.register("prideful_gold_tile", () -> new Block(create(IRON).sound(SoundType.METAL).hardnessAndResistance(4.0F, 8.0F)));

	//Limestone
	public static final RegistryObject<Block> LIMESTONE_TILE = BLOCKS.register("limestone_tile", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> ROUGH_LIMESTONE = BLOCKS.register("rough_limestone", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> SMOOTH_LIMESTONE = BLOCKS.register("smooth_limestone", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));

	// Functional
	public static final RegistryObject<Block> AMBER_ORE = BLOCKS.register("amber_ore", () -> new Block(create(ROCK).harvestLevel(1).hardnessAndResistance(3.0F,3.0F)));
	public static final RegistryObject<Block> SILVER_ORE = BLOCKS.register("silver_ore", () -> new Block(create(ROCK).hardnessAndResistance(3.0F,3.0F)));
	public static final RegistryObject<Block> INFUSION_ARCANE_STONE = BLOCKS.register("infusion_arcane_stone", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> MAGICAL_GRASS = BLOCKS.register("magical_grass", () -> new Block(from(Blocks.GRASS_BLOCK)));
	public static final RegistryObject<Block> TABLE = BLOCKS.register("table", () -> new WaterloggableBlock(create(WOOD).hardnessAndResistance(2).notSolid()));

	public static final RegistryObject<Block> TAINTED_AMBER_ORE = BLOCKS.register("tainted_amber_ore", () -> new TaintedBlock(AMBER_ORE.get()));
	public static final RegistryObject<Block> TAINTED_SILVER_ORE = BLOCKS.register("tainted_silver_ore", () -> new TaintedBlock(SILVER_ORE.get()));

	// Functional Blocks
	public static final RegistryObject<Block> JAR = BLOCKS.register("jar", () -> new BlockJar(create(GLASS)));
	public static final RegistryObject<Block> ASPECT_BOOKSHELF = BLOCKS.register("aspect_bookshelf", () -> new BlockAspectBookshelf(create(WOOD).hardnessAndResistance(6)));
	public static final RegistryObject<Block> RESEARCH_TABLE = BLOCKS.register("research_table", () -> new ResearchTableBlock(create(WOOD).hardnessAndResistance(3).notSolid()));
	public static final RegistryObject<Block> ARCANE_CRAFTING_TABLE = BLOCKS.register("arcane_crafting_table", () -> new WaterloggableBlock(create(WOOD).hardnessAndResistance(2).notSolid()));
	public static final RegistryObject<Block> CRUCIBLE = BLOCKS.register("crucible", () -> new CrucibleBlock(create(IRON, MaterialColor.STONE).hardnessAndResistance(2).notSolid()));
	public static final RegistryObject<Block> INFUSION_PEDESTAL = BLOCKS.register("infusion_pedestal", () -> new PedestalBlock(create(ROCK).hardnessAndResistance(3).notSolid()));
	public static final RegistryObject<Block> ASPECT_TESTER = BLOCKS.register("aspect_tester", () -> new AspectTesterBlock(create(ROCK).hardnessAndResistance(3).notSolid()));
	
	public static final RegistryObject<Block> SEE_NO_EVIL_STATUE = BLOCKS.register("see_no_evil_statue", () -> new StatueBlock(create(WOOD).hardnessAndResistance(4).notSolid()));
	public static final RegistryObject<Block> HEAR_NO_EVIL_STATUE = BLOCKS.register("hear_no_evil_statue", () -> new StatueBlock(create(WOOD).hardnessAndResistance(4).notSolid()));
	public static final RegistryObject<Block> SPEAK_NO_EVIL_STATUE = BLOCKS.register("speak_no_evil_statue", () -> new StatueBlock(create(WOOD).hardnessAndResistance(4).notSolid()));
	
	// Woods
	// Dair Wood
	public static final RegistryObject<Block> DAIR_LEAVES = BLOCKS.register("dair_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> DAIR_LOG = BLOCKS.register("dair_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DAIR_PLANKS = BLOCKS.register("dair_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DAIR_DOOR = BLOCKS.register("dair_door", () -> new ADoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> DAIR_TRAPDOOR = BLOCKS.register("dair_trapdoor", () -> new ATrapDoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> DAIR_PRESSURE_PLATE = BLOCKS.register("dair_pressure_plate", () -> new APressurePlateBlock(Sensitivity.EVERYTHING, create(WOOD).hardnessAndResistance(.5f).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<Block> DAIR_SAPLING = BLOCKS.register("dair_sapling", () -> new ASaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> DAIR_SLAB = BLOCKS.register("dair_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DAIR_STAIRS = BLOCKS.register("dair_stairs", () -> new StairsBlock(() -> DAIR_PLANKS.get().getDefaultState(), create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DAIR_BUTTON = BLOCKS.register("dair_button", () -> new AWoodButtonBlock(create(MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceBlock> DAIR_FENCE = BLOCKS.register("dair_fence", () -> new FenceBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceGateBlock> DAIR_FENCE_GATE = BLOCKS.register("dair_fence_gate", () -> new FenceGateBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final RegistryObject<Block> TAINTED_DAIR_LEAVES = BLOCKS.register("tainted_dair_leaves", () -> new TaintedBlock(ArcanaBlocks.DAIR_LEAVES.get()));
	public static final RegistryObject<Block> TAINTED_DAIR_LOG = BLOCKS.register("tainted_dair_log", () -> new TaintedBlock(ArcanaBlocks.DAIR_LOG.get()));
	public static final RegistryObject<Block> TAINTED_DAIR_PLANKS = BLOCKS.register("tainted_dair_planks", () -> new TaintedBlock(ArcanaBlocks.DAIR_PLANKS.get()));
	public static final RegistryObject<Block> TAINTED_DAIR_SAPLING = BLOCKS.register("tainted_dair_sapling", () -> new TaintedBlock(ArcanaBlocks.DAIR_SAPLING.get()));
	public static final RegistryObject<Block> TAINTED_DAIR_SLAB = BLOCKS.register("tainted_dair_slab", () -> new TaintedBlock(ArcanaBlocks.DAIR_SLAB.get()));
	public static final RegistryObject<Block> TAINTED_DAIR_STAIRS = BLOCKS.register("tainted_dair_stairs", () -> new TaintedBlock(ArcanaBlocks.DAIR_STAIRS.get()));

	// Dead wood
	public static final RegistryObject<Block> DEAD_LOG = BLOCKS.register("dead_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_PLANKS = BLOCKS.register("dead_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_PRESSURE_PLATE = BLOCKS.register("dead_pressure_plate", () -> new APressurePlateBlock(Sensitivity.EVERYTHING, create(WOOD).hardnessAndResistance(.5f).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<Block> DEAD_SLAB = BLOCKS.register("dead_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_STAIRS = BLOCKS.register("dead_stairs", () -> new StairsBlock(() -> DEAD_PLANKS.get().getDefaultState(), create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_BUTTON = BLOCKS.register("dead_button", () -> new AWoodButtonBlock(create(MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceBlock> DEAD_FENCE = BLOCKS.register("dead_fence", () -> new FenceBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceGateBlock> DEAD_FENCE_GATE = BLOCKS.register("dead_fence_gate", () -> new FenceGateBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	// Eucalyptus Wood
	public static final RegistryObject<Block> EUCALYPTUS_LEAVES = BLOCKS.register("eucalyptus_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> EUCALYPTUS_LOG = BLOCKS.register("eucalyptus_log", () -> new LogBlock(MaterialColor.PINK, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> EUCALYPTUS_PLANKS = BLOCKS.register("eucalyptus_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> EUCALYPTUS_DOOR = BLOCKS.register("eucalyptus_door", () -> new ADoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> EUCALYPTUS_TRAPDOOR = BLOCKS.register("eucalyptus_trapdoor", () -> new ATrapDoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> EUCALYPTUS_PRESSURE_PLATE = BLOCKS.register("eucalyptus_pressure_plate", () -> new APressurePlateBlock(Sensitivity.EVERYTHING, create(WOOD).hardnessAndResistance(.5f).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<Block> EUCALYPTUS_SAPLING = BLOCKS.register("eucalyptus_sapling", () -> new ASaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> EUCALYPTUS_SLAB = BLOCKS.register("eucalyptus_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> EUCALYPTUS_STAIRS = BLOCKS.register("eucalyptus_stairs", () -> new StairsBlock(() -> EUCALYPTUS_PLANKS.get().getDefaultState(), create(WOOD, MaterialColor.PINK).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> EUCALYPTUS_BUTTON = BLOCKS.register("eucalyptus_button", () -> new AWoodButtonBlock(create(MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceBlock> EUCALYPTUS_FENCE = BLOCKS.register("eucalyptus_fence", () -> new FenceBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceGateBlock> EUCALYPTUS_FENCE_GATE = BLOCKS.register("eucalyptus_fence_gate", () -> new FenceGateBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));

	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_LEAVES = BLOCKS.register("tainted_eucalyptus_leaves", () -> new TaintedBlock(ArcanaBlocks.EUCALYPTUS_LEAVES.get()));
	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_LOG = BLOCKS.register("tainted_eucalyptus_log", () -> new TaintedBlock(ArcanaBlocks.EUCALYPTUS_LOG.get()));
	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_PLANKS = BLOCKS.register("tainted_eucalyptus_planks", () -> new TaintedBlock(ArcanaBlocks.EUCALYPTUS_PLANKS.get()));
	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_SAPLING = BLOCKS.register("tainted_eucalyptus_sapling", () -> new TaintedBlock(ArcanaBlocks.EUCALYPTUS_SAPLING.get()));
	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_SLAB = BLOCKS.register("tainted_eucalyptus_slab", () -> new TaintedBlock(ArcanaBlocks.EUCALYPTUS_SLAB.get()));
	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_STAIRS = BLOCKS.register("tainted_eucalyptus_stairs", () -> new TaintedBlock(ArcanaBlocks.EUCALYPTUS_STAIRS.get()));
	
	// Greatwood
	public static final RegistryObject<Block> GREATWOOD_LEAVES = BLOCKS.register("greatwood_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> GREATWOOD_LOG = BLOCKS.register("greatwood_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GREATWOOD_PLANKS = BLOCKS.register("greatwood_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GREATWOOD_DOOR = BLOCKS.register("greatwood_door", () -> new ADoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> GREATWOOD_TRAPDOOR = BLOCKS.register("greatwood_trapdoor", () -> new ATrapDoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> GREATWOOD_PRESSURE_PLATE = BLOCKS.register("greatwood_pressure_plate", () -> new APressurePlateBlock(Sensitivity.EVERYTHING, create(WOOD).hardnessAndResistance(.5f).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<Block> GREATWOOD_SAPLING = BLOCKS.register("greatwood_sapling", () -> new ASaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> GREATWOOD_SLAB = BLOCKS.register("greatwood_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GREATWOOD_STAIRS = BLOCKS.register("greatwood_stairs", () -> new StairsBlock(() -> GREATWOOD_PLANKS.get().getDefaultState(), create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GREATWOOD_BUTTON = BLOCKS.register("greatwood_button", () -> new AWoodButtonBlock(create(MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceBlock> GREATWOOD_FENCE = BLOCKS.register("greatwood_fence", () -> new FenceBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceGateBlock> GREATWOOD_FENCE_GATE = BLOCKS.register("greatwood_fence_gate", () -> new FenceGateBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));

	public static final RegistryObject<Block> TAINTED_GREATWOOD_LEAVES = BLOCKS.register("tainted_greatwood_leaves", () -> new TaintedBlock(ArcanaBlocks.GREATWOOD_LEAVES.get()));
	public static final RegistryObject<Block> TAINTED_GREATWOOD_LOG = BLOCKS.register("tainted_greatwood_log", () -> new TaintedBlock(ArcanaBlocks.GREATWOOD_LOG.get()));
	public static final RegistryObject<Block> TAINTED_GREATWOOD_PLANKS = BLOCKS.register("tainted_greatwood_planks", () -> new TaintedBlock(ArcanaBlocks.GREATWOOD_PLANKS.get()));
	public static final RegistryObject<Block> TAINTED_GREATWOOD_SAPLING = BLOCKS.register("tainted_greatwood_sapling", () -> new TaintedBlock(ArcanaBlocks.GREATWOOD_SAPLING.get()));
	public static final RegistryObject<Block> TAINTED_GREATWOOD_SLAB = BLOCKS.register("tainted_greatwood_slab", () -> new TaintedBlock(ArcanaBlocks.GREATWOOD_SLAB.get()));
	public static final RegistryObject<Block> TAINTED_GREATWOOD_STAIRS = BLOCKS.register("tainted_greatwood_stairs", () -> new TaintedBlock(ArcanaBlocks.GREATWOOD_STAIRS.get()));
	
	// Hawthorn Wood
	public static final RegistryObject<Block> HAWTHORN_LEAVES = BLOCKS.register("hawthorn_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> HAWTHORN_LOG = BLOCKS.register("hawthorn_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HAWTHORN_PLANKS = BLOCKS.register("hawthorn_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HAWTHORN_DOOR = BLOCKS.register("hawthorn_door", () -> new ADoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> HAWTHORN_TRAPDOOR = BLOCKS.register("hawthorn_trapdoor", () -> new ATrapDoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> HAWTHORN_PRESSURE_PLATE = BLOCKS.register("hawthorn_pressure_plate", () -> new APressurePlateBlock(Sensitivity.EVERYTHING, create(WOOD).hardnessAndResistance(.5f).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<Block> HAWTHORN_SAPLING = BLOCKS.register("hawthorn_sapling", () -> new ASaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> HAWTHORN_SLAB = BLOCKS.register("hawthorn_slab", () -> new SlabBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HAWTHORN_STAIRS = BLOCKS.register("hawthorn_stairs", () -> new StairsBlock(() -> HAWTHORN_PLANKS.get().getDefaultState(), create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HAWTHORN_BUTTON = BLOCKS.register("hawthorn_button", () -> new AWoodButtonBlock(create(MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceBlock> HAWTHORN_FENCE = BLOCKS.register("hawthorn_fence", () -> new FenceBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceGateBlock> HAWTHORN_FENCE_GATE = BLOCKS.register("hawthorn_fence_gate", () -> new FenceGateBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));

	public static final RegistryObject<Block> TAINTED_HAWTHORN_LEAVES = BLOCKS.register("tainted_hawthorn_leaves", () -> new TaintedBlock(ArcanaBlocks.HAWTHORN_LEAVES.get()));
	public static final RegistryObject<Block> TAINTED_HAWTHORN_LOG = BLOCKS.register("tainted_hawthorn_log", () -> new TaintedBlock(ArcanaBlocks.HAWTHORN_LOG.get()));
	public static final RegistryObject<Block> TAINTED_HAWTHORN_PLANKS = BLOCKS.register("tainted_hawthorn_planks", () -> new TaintedBlock(ArcanaBlocks.HAWTHORN_PLANKS.get()));
	public static final RegistryObject<Block> TAINTED_HAWTHORN_SAPLING = BLOCKS.register("tainted_hawthorn_sapling", () -> new TaintedBlock(ArcanaBlocks.HAWTHORN_SAPLING.get()));
	public static final RegistryObject<Block> TAINTED_HAWTHORN_SLAB = BLOCKS.register("tainted_hawthorn_slab", () -> new TaintedBlock(ArcanaBlocks.HAWTHORN_SLAB.get()));
	public static final RegistryObject<Block> TAINTED_HAWTHORN_STAIRS = BLOCKS.register("tainted_hawthorn_stairs", () -> new TaintedBlock(ArcanaBlocks.HAWTHORN_STAIRS.get()));
	
	// Silverwood
	public static final RegistryObject<Block> SILVERWOOD_LEAVES = BLOCKS.register("silverwood_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> SILVERWOOD_LOG = BLOCKS.register("silverwood_log", () -> new LogBlock(SAND, create(WOOD, SAND).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SILVERWOOD_PLANKS = BLOCKS.register("silverwood_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SILVERWOOD_DOOR = BLOCKS.register("silverwood_door", () -> new ADoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> SILVERWOOD_TRAPDOOR = BLOCKS.register("silverwood_trapdoor", () -> new ATrapDoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> SILVERWOOD_PRESSURE_PLATE = BLOCKS.register("silverwood_pressure_plate", () -> new APressurePlateBlock(Sensitivity.EVERYTHING, create(WOOD).hardnessAndResistance(.5f).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<Block> SILVERWOOD_SAPLING = BLOCKS.register("silverwood_sapling", () -> new ASaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> SILVERWOOD_SLAB = BLOCKS.register("silverwood_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SILVERWOOD_STAIRS = BLOCKS.register("silverwood_stairs", () -> new StairsBlock(() -> SILVERWOOD_PLANKS.get().getDefaultState(), create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SILVERWOOD_BUTTON = BLOCKS.register("silverwood_button", () -> new AWoodButtonBlock(create(MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceBlock> SILVERWOOD_FENCE = BLOCKS.register("silverwood_fence", () -> new FenceBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceGateBlock> SILVERWOOD_FENCE_GATE = BLOCKS.register("silverwood_fence_gate", () -> new FenceGateBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	// Trypophobius Wood
	public static final RegistryObject<Block> TRYPOPHOBIUS_LOG = BLOCKS.register("trypophobius_log", () -> new LogBlock(BLACK, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TRYPOPHOBIUS_PLANKS = BLOCKS.register("trypophobius_planks", () -> new Block(create(WOOD, BLACK).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TRYPOPHOBIUS_PRESSURE_PLATE = BLOCKS.register("trypophobius_pressure_plate", () -> new APressurePlateBlock(Sensitivity.EVERYTHING, create(WOOD).hardnessAndResistance(.5f).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<Block> TRYPOPHOBIUS_SLAB = BLOCKS.register("trypophobius_slab", () -> new SlabBlock(create(WOOD, BLACK).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TRYPOPHOBIUS_STAIRS = BLOCKS.register("trypophobius_stairs", () -> new StairsBlock(() -> TRYPOPHOBIUS_PLANKS.get().getDefaultState(), create(WOOD, BLACK).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TRYPOPHOBIUS_BUTTON = BLOCKS.register("trypophobius_button", () -> new AWoodButtonBlock(create(MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceBlock> TRYPOPHOBIUS_FENCE = BLOCKS.register("trypophobius_fence", () -> new FenceBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceGateBlock> TRYPOPHOBIUS_FENCE_GATE = BLOCKS.register("trypophobius_fence_gate", () -> new FenceGateBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	// Willow Wood
	public static final RegistryObject<Block> WILLOW_LEAVES = BLOCKS.register("willow_leaves", () -> new LeavesBlock(create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
	public static final RegistryObject<Block> WILLOW_LOG = BLOCKS.register("willow_log", () -> new LogBlock(MaterialColor.BROWN, create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> WILLOW_PLANKS = BLOCKS.register("willow_planks", () -> new Block(create(WOOD).hardnessAndResistance(2).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> WILLOW_DOOR = BLOCKS.register("willow_door", () -> new ADoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> WILLOW_TRAPDOOR = BLOCKS.register("willow_trapdoor", () -> new ATrapDoorBlock(create(WOOD).hardnessAndResistance(3).sound(SoundType.WOOD).notSolid()));
	public static final RegistryObject<Block> WILLOW_PRESSURE_PLATE = BLOCKS.register("willow_pressure_plate", () -> new APressurePlateBlock(Sensitivity.EVERYTHING, create(WOOD).hardnessAndResistance(.5f).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<Block> WILLOW_SAPLING = BLOCKS.register("willow_sapling", () -> new ASaplingBlock(new DummyTree(), create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> WILLOW_SLAB = BLOCKS.register("willow_slab", () -> new SlabBlock(create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> WILLOW_STAIRS = BLOCKS.register("willow_stairs", () -> new StairsBlock(() -> WILLOW_PLANKS.get().getDefaultState(), create(WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> WILLOW_BUTTON = BLOCKS.register("willow_button", () -> new AWoodButtonBlock(create(MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceBlock> WILLOW_FENCE = BLOCKS.register("willow_fence", () -> new FenceBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<FenceGateBlock> WILLOW_FENCE_GATE = BLOCKS.register("willow_fence_gate", () -> new FenceGateBlock(create(WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));

	public static final RegistryObject<Block> TAINTED_WILLOW_LEAVES = BLOCKS.register("tainted_willow_leaves", () -> new TaintedBlock(ArcanaBlocks.WILLOW_LEAVES.get()));
	public static final RegistryObject<Block> TAINTED_WILLOW_LOG = BLOCKS.register("tainted_willow_log", () -> new TaintedBlock(ArcanaBlocks.WILLOW_LOG.get()));
	public static final RegistryObject<Block> TAINTED_WILLOW_PLANKS = BLOCKS.register("tainted_willow_planks", () -> new TaintedBlock(ArcanaBlocks.WILLOW_PLANKS.get()));
	public static final RegistryObject<Block> TAINTED_WILLOW_SAPLING = BLOCKS.register("tainted_willow_sapling", () -> new TaintedBlock(ArcanaBlocks.WILLOW_SAPLING.get()));
	public static final RegistryObject<Block> TAINTED_WILLOW_SLAB = BLOCKS.register("tainted_willow_slab", () -> new TaintedBlock(ArcanaBlocks.WILLOW_SLAB.get()));
	public static final RegistryObject<Block> TAINTED_WILLOW_STAIRS = BLOCKS.register("tainted_willow_stairs", () -> new TaintedBlock(ArcanaBlocks.WILLOW_STAIRS.get()));
	
	// Compressed Resources
	public static final RegistryObject<Block> ARCANIUM_BLOCK = BLOCKS.register("arcanium_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> THAUMIUM_BLOCK = BLOCKS.register("thaumium_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> VOID_METAL_BLOCK = BLOCKS.register("void_metal_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register("silver_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));

	public static final RegistryObject<Block> TAINTED_ARCANIUM_BLOCK = BLOCKS.register("tainted_arcanium_block", () -> new TaintedBlock(ArcanaBlocks.ARCANIUM_BLOCK.get()));
	public static final RegistryObject<Block> TAINTED_THAUMIUM_BLOCK = BLOCKS.register("tainted_thaumium_block", () -> new TaintedBlock(ArcanaBlocks.THAUMIUM_BLOCK.get()));

	//Misc Tainted Blocks
	//public static final RegistryObject<Block> TAINTED_DESTROYED_ORE = BLOCKS.register("tainted_destroyed_ore", () -> new TaintedBlock(Blocks.STONE_BRICKS));


	//Tainted vanilla blocks
	public static final RegistryObject<Block> TAINTED_CRUST = BLOCKS.register("tainted_crust", () -> new TaintedBlock(Blocks.COBBLESTONE));
	public static final RegistryObject<Block> TAINTED_CRUST_SLAB = BLOCKS.register("tainted_crust_slab", () -> new TaintedBlock(Blocks.COBBLESTONE_SLAB));
	public static final RegistryObject<Block> TAINTED_GRAVEL = BLOCKS.register("tainted_gravel", () -> new TaintedBlock(Blocks.GRAVEL));
	public static final RegistryObject<Block> TAINTED_SAND = BLOCKS.register("tainted_sand", () -> new TaintedBlock(Blocks.SAND));

	public static final RegistryObject<Block> TAINTED_ANDESITE = BLOCKS.register("tainted_andesite", () -> new TaintedBlock(Blocks.ANDESITE));
	public static final RegistryObject<Block> TAINTED_DIORITE = BLOCKS.register("tainted_diorite", () -> new TaintedBlock(Blocks.DIORITE));
	public static final RegistryObject<Block> TAINTED_GRANITE = BLOCKS.register("tainted_granite", () -> new TaintedBlock(Blocks.GRANITE));
	public static final RegistryObject<Block> TAINTED_ROCK = BLOCKS.register("tainted_rock", () -> new TaintedBlock(Blocks.STONE));
	public static final RegistryObject<Block> TAINTED_ROCK_SLAB = BLOCKS.register("tainted_rock_slab", () -> new TaintedBlock(Blocks.STONE_SLAB));

	public static final RegistryObject<Block> TAINTED_SOIL = BLOCKS.register("tainted_soil", () -> new TaintedBlock(Blocks.DIRT));
	public static final RegistryObject<Block> TAINTED_GRASS_BLOCK = BLOCKS.register("tainted_grass_block", () -> new TaintedBlock(Blocks.GRASS_BLOCK));
	public static final RegistryObject<Block> TAINTED_FARMLAND = BLOCKS.register("tainted_farmland", () -> new TaintedBlock(Blocks.FARMLAND));
	public static final RegistryObject<Block> TAINTED_PATH = BLOCKS.register("tainted_path", () -> new TaintedBlock(Blocks.GRASS_PATH));

	public static final RegistryObject<Block> TAINTED_COAL_BLOCK = BLOCKS.register("tainted_coal_block", () -> new TaintedBlock(Blocks.COAL_BLOCK));
	public static final RegistryObject<Block> TAINTED_EMERALD_BLOCK = BLOCKS.register("tainted_emerald_block", () -> new TaintedBlock(Blocks.EMERALD_BLOCK));
	public static final RegistryObject<Block> TAINTED_DIAMOND_BLOCK = BLOCKS.register("tainted_diamond_block", () -> new TaintedBlock(Blocks.DIAMOND_BLOCK));
	public static final RegistryObject<Block> TAINTED_GOLD_BLOCK = BLOCKS.register("tainted_gold_block", () -> new TaintedBlock(Blocks.GOLD_BLOCK));
	public static final RegistryObject<Block> TAINTED_IRON_BLOCK = BLOCKS.register("tainted_iron_block", () -> new TaintedBlock(Blocks.IRON_BLOCK));
	public static final RegistryObject<Block> TAINTED_LAPIS_BLOCK = BLOCKS.register("tainted_lapis_block", () -> new TaintedBlock(Blocks.LAPIS_BLOCK));
	public static final RegistryObject<Block> TAINTED_REDSTONE_BLOCK = BLOCKS.register("tainted_redstone_block", () -> new TaintedBlock(Blocks.REDSTONE_BLOCK));

	public static final RegistryObject<Block> TAINTED_BUSH = BLOCKS.register("tainted_bush", () -> new TaintedBlock(Blocks.GRASS));
	public static final RegistryObject<Block> TAINTED_FLOWER = BLOCKS.register("tainted_flower", () -> new TaintedBlock(Blocks.CORNFLOWER));
	public static final RegistryObject<Block> TAINTED_CARVED_PUMPKIN = BLOCKS.register("tainted_carved_pumpkin", () -> new TaintedBlock(Blocks.CARVED_PUMPKIN));
	public static final RegistryObject<Block> TAINTED_JACK_OLANTERN = BLOCKS.register("tainted_jack_olantern", () -> new TaintedBlock(Blocks.JACK_O_LANTERN));
	public static final RegistryObject<Block> TAINTED_MELON = BLOCKS.register("tainted_melon", () -> new TaintedBlock(Blocks.MELON));
	public static final RegistryObject<Block> TAINTED_MUSHROOM = BLOCKS.register("tainted_mushroom", () -> new TaintedBlock(Blocks.BROWN_MUSHROOM));
	public static final RegistryObject<Block> TAINTED_PUMPKIN = BLOCKS.register("tainted_pumpkin", () -> new TaintedBlock(Blocks.PUMPKIN));

	public static final RegistryObject<Block> TAINTED_COAL_ORE = BLOCKS.register("tainted_coal_ore", () -> new TaintedBlock(Blocks.COAL_ORE));
	public static final RegistryObject<Block> TAINTED_IRON_ORE = BLOCKS.register("tainted_iron_ore", () -> new TaintedBlock(Blocks.IRON_ORE));
	public static final RegistryObject<Block> TAINTED_GOLD_ORE = BLOCKS.register("tainted_gold_ore", () -> new TaintedBlock(Blocks.GOLD_ORE));
	public static final RegistryObject<Block> TAINTED_DIAMOND_ORE = BLOCKS.register("tainted_diamond_ore", () -> new TaintedBlock(Blocks.DIAMOND_ORE));
	public static final RegistryObject<Block> TAINTED_LAPIS_ORE = BLOCKS.register("tainted_lapis_ore", () -> new TaintedBlock(Blocks.LAPIS_ORE));
	public static final RegistryObject<Block> TAINTED_EMERALD_ORE = BLOCKS.register("tainted_emerald_ore", () -> new TaintedBlock(Blocks.EMERALD_ORE));
	public static final RegistryObject<Block> TAINTED_REDSTONE_ORE = BLOCKS.register("tainted_redstone_ore", () -> new TaintedBlock(Blocks.REDSTONE_ORE));

	public static final RegistryObject<Block> TAINTED_ACACIA_LEAVES = BLOCKS.register("tainted_acacia_leaves", () -> new TaintedBlock(Blocks.ACACIA_LEAVES));
	public static final RegistryObject<Block> TAINTED_ACACIA_LOG = BLOCKS.register("tainted_acacia_log", () -> new TaintedBlock(Blocks.ACACIA_LOG));
	public static final RegistryObject<Block> TAINTED_ACACIA_PLANKS = BLOCKS.register("tainted_acacia_planks", () -> new TaintedBlock(Blocks.ACACIA_PLANKS));
	public static final RegistryObject<Block> TAINTED_ACACIA_SAPLING = BLOCKS.register("tainted_acacia_sapling", () -> new TaintedBlock(Blocks.ACACIA_SAPLING));
	public static final RegistryObject<Block> TAINTED_ACACIA_SLAB = BLOCKS.register("tainted_acacia_slab", () -> new TaintedBlock(Blocks.ACACIA_SLAB));
	public static final RegistryObject<Block> TAINTED_ACACIA_STAIRS = BLOCKS.register("tainted_acacia_stairs", () -> new TaintedBlock(Blocks.ACACIA_STAIRS));

	public static final RegistryObject<Block> TAINTED_BIRCH_LEAVES = BLOCKS.register("tainted_birch_leaves", () -> new TaintedBlock(Blocks.BIRCH_LEAVES));
	public static final RegistryObject<Block> TAINTED_BIRCH_LOG = BLOCKS.register("tainted_birch_log", () -> new TaintedBlock(Blocks.BIRCH_LOG));
	public static final RegistryObject<Block> TAINTED_BIRCH_PLANKS = BLOCKS.register("tainted_birch_planks", () -> new TaintedBlock(Blocks.BIRCH_PLANKS));
	public static final RegistryObject<Block> TAINTED_BIRCH_SAPLING = BLOCKS.register("tainted_birch_sapling", () -> new TaintedBlock(Blocks.BIRCH_SAPLING));
	public static final RegistryObject<Block> TAINTED_BIRCH_SLAB = BLOCKS.register("tainted_birch_slab", () -> new TaintedBlock(Blocks.BIRCH_SLAB));
	public static final RegistryObject<Block> TAINTED_BIRCH_STAIRS = BLOCKS.register("tainted_birch_stairs", () -> new TaintedBlock(Blocks.BIRCH_STAIRS));

	public static final RegistryObject<Block> TAINTED_DARKOAK_LEAVES = BLOCKS.register("tainted_darkoak_leaves", () -> new TaintedBlock(Blocks.DARK_OAK_LEAVES));
	public static final RegistryObject<Block> TAINTED_DARKOAK_LOG = BLOCKS.register("tainted_darkoak_log", () -> new TaintedBlock(Blocks.DARK_OAK_LOG));
	public static final RegistryObject<Block> TAINTED_DARKOAK_PLANKS = BLOCKS.register("tainted_darkoak_planks", () -> new TaintedBlock(Blocks.DARK_OAK_PLANKS));
	public static final RegistryObject<Block> TAINTED_DARKOAK_SAPLING = BLOCKS.register("tainted_darkoak_sapling", () -> new TaintedBlock(Blocks.DARK_OAK_SAPLING));
	public static final RegistryObject<Block> TAINTED_DARKOAK_SLAB = BLOCKS.register("tainted_darkoak_slab", () -> new TaintedBlock(Blocks.DARK_OAK_SLAB));
	public static final RegistryObject<Block> TAINTED_DARKOAK_STAIRS = BLOCKS.register("tainted_darkoak_stairs", () -> new TaintedBlock(Blocks.DARK_OAK_STAIRS));

	public static final RegistryObject<Block> TAINTED_JUNGLE_LEAVES = BLOCKS.register("tainted_jungle_leaves", () -> new TaintedBlock(Blocks.JUNGLE_LEAVES));
	public static final RegistryObject<Block> TAINTED_JUNGLE_LOG = BLOCKS.register("tainted_jungle_log", () -> new TaintedBlock(Blocks.JUNGLE_LOG));
	public static final RegistryObject<Block> TAINTED_JUNGLE_PLANKS = BLOCKS.register("tainted_jungle_planks", () -> new TaintedBlock(Blocks.JUNGLE_PLANKS));
	public static final RegistryObject<Block> TAINTED_JUNGLE_SAPLING = BLOCKS.register("tainted_jungle_sapling", () -> new TaintedBlock(Blocks.JUNGLE_SAPLING));
	public static final RegistryObject<Block> TAINTED_JUNGLE_SLAB = BLOCKS.register("tainted_jungle_slab", () -> new TaintedBlock(Blocks.JUNGLE_SLAB));
	public static final RegistryObject<Block> TAINTED_JUNGLE_STAIRS = BLOCKS.register("tainted_jungle_stairs", () -> new TaintedBlock(Blocks.JUNGLE_STAIRS));

	public static final RegistryObject<Block> TAINTED_OAK_LEAVES = BLOCKS.register("tainted_oak_leaves", () -> new TaintedBlock(Blocks.OAK_LEAVES));
	public static final RegistryObject<Block> TAINTED_OAK_LOG = BLOCKS.register("tainted_oak_log", () -> new TaintedBlock(Blocks.OAK_LOG));
	public static final RegistryObject<Block> TAINTED_OAK_PLANKS = BLOCKS.register("tainted_oak_planks", () -> new TaintedBlock(Blocks.OAK_PLANKS));
	public static final RegistryObject<Block> TAINTED_OAK_SAPLING = BLOCKS.register("tainted_oak_sapling", () -> new TaintedBlock(Blocks.OAK_SAPLING));
	public static final RegistryObject<Block> TAINTED_OAK_SLAB = BLOCKS.register("tainted_oak_slab", () -> new TaintedBlock(Blocks.OAK_SLAB));
	public static final RegistryObject<Block> TAINTED_OAK_STAIRS = BLOCKS.register("tainted_oak_stairs", () -> new TaintedBlock(Blocks.OAK_STAIRS));

	public static final RegistryObject<Block> TAINTED_SPRUCE_LEAVES = BLOCKS.register("tainted_spruce_leaves", () -> new TaintedBlock(Blocks.SPRUCE_LEAVES));
	public static final RegistryObject<Block> TAINTED_SPRUCE_LOG = BLOCKS.register("tainted_spruce_log", () -> new TaintedBlock(Blocks.SPRUCE_LOG));
	public static final RegistryObject<Block> TAINTED_SPRUCE_PLANKS = BLOCKS.register("tainted_spruce_planks", () -> new TaintedBlock(Blocks.SPRUCE_PLANKS));
	public static final RegistryObject<Block> TAINTED_SPRUCE_SAPLING = BLOCKS.register("tainted_spruce_sapling", () -> new TaintedBlock(Blocks.SPRUCE_SAPLING));
	public static final RegistryObject<Block> TAINTED_SPRUCE_SLAB = BLOCKS.register("tainted_spruce_slab", () -> new TaintedBlock(Blocks.SPRUCE_SLAB));
	public static final RegistryObject<Block> TAINTED_SPRUCE_STAIRS = BLOCKS.register("tainted_spruce_stairs", () -> new TaintedBlock(Blocks.SPRUCE_STAIRS));
}