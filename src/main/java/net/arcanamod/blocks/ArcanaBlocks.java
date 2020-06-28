package net.arcanamod.blocks;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.bases.*;
import net.arcanamod.blocks.multiblocks.TaintScrubberBlock;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.worldgen.DummyTree;
import net.minecraft.block.*;
import net.minecraft.block.PressurePlateBlock.Sensitivity;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.blocks.Taint.taintedOf;
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
	public static final RegistryObject<Block> AMBER_ORE = BLOCKS.register("amber_ore", () -> new Block(create(ROCK).harvestLevel(1).hardnessAndResistance(3.0F, 3.0F)));
	public static final RegistryObject<Block> SILVER_ORE = BLOCKS.register("silver_ore", () -> new Block(create(ROCK).hardnessAndResistance(3.0F, 3.0F)));
	public static final RegistryObject<Block> INFUSION_ARCANE_STONE = BLOCKS.register("infusion_arcane_stone", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> MAGICAL_GRASS = BLOCKS.register("magical_grass", () -> new Block(from(Blocks.GRASS_BLOCK)));
	public static final RegistryObject<Block> TABLE = BLOCKS.register("table", () -> new WaterloggableBlock(create(WOOD).hardnessAndResistance(2).notSolid()));

	public static final RegistryObject<Block> TAINTED_AMBER_ORE = BLOCKS.register("tainted_amber_ore", () -> taintedOf(AMBER_ORE.get()));
	public static final RegistryObject<Block> TAINTED_SILVER_ORE = BLOCKS.register("tainted_silver_ore", () -> taintedOf(SILVER_ORE.get()));

	// Functional Blocks
	public static final RegistryObject<Block> JAR = BLOCKS.register("jar", () -> new BlockJar(create(GLASS)));
	public static final RegistryObject<Block> ASPECT_BOOKSHELF = BLOCKS.register("aspect_bookshelf", () -> new BlockAspectBookshelf(create(WOOD).hardnessAndResistance(6)));
	public static final RegistryObject<Block> RESEARCH_TABLE = BLOCKS.register("research_table", () -> new ResearchTableBlock(create(WOOD).hardnessAndResistance(3).notSolid()));
	public static final RegistryObject<Block> ARCANE_CRAFTING_TABLE = BLOCKS.register("arcane_crafting_table", () -> new WaterloggableBlock(create(WOOD).hardnessAndResistance(2).notSolid()));
	public static final RegistryObject<Block> CRUCIBLE = BLOCKS.register("crucible", () -> new CrucibleBlock(create(IRON, MaterialColor.STONE).hardnessAndResistance(2).notSolid()));
	public static final RegistryObject<Block> INFUSION_PEDESTAL = BLOCKS.register("infusion_pedestal", () -> new PedestalBlock(create(ROCK).hardnessAndResistance(3).notSolid()));
	public static final RegistryObject<Block> ASPECT_TESTER = BLOCKS.register("aspect_tester", () -> new AspectTesterBlock(create(ROCK).hardnessAndResistance(3).notSolid()));

	//Taint Scrubber //What is proper material for taint scrubber?
	public static final RegistryObject<Block> TAINT_SCRUBBER_SUPPORT = BLOCKS.register("taint_scrubber_support", () -> new Block(create(IRON).hardnessAndResistance(3).notSolid()));
	public static final RegistryObject<Block> TAINT_SCRUBBER_MK1 = BLOCKS.register("taint_scrubber_mk1", () -> new TaintScrubberBlock(create(IRON).hardnessAndResistance(3).notSolid(), TaintScrubberBlock.Level.MK1));
	public static final RegistryObject<Block> TAINT_SCRUBBER_MK2 = BLOCKS.register("taint_scrubber_mk2", () -> new TaintScrubberBlock(create(IRON).hardnessAndResistance(3).notSolid(), TaintScrubberBlock.Level.MK2));
	public static final RegistryObject<Block> TAINT_BOOSTER = BLOCKS.register("taint_booster", () -> new TaintScrubberBlock(create(IRON).hardnessAndResistance(3).notSolid(), TaintScrubberBlock.Level.MK2));
	public static final RegistryObject<Block> TAINT_SUCKER = BLOCKS.register("taint_sucker", () -> new TaintScrubberBlock(create(IRON).hardnessAndResistance(3).notSolid(), TaintScrubberBlock.Level.MK2));
	public static final RegistryObject<Block> TAINT_BORE = BLOCKS.register("taint_bore", () -> new TaintScrubberBlock(create(IRON).hardnessAndResistance(3).notSolid(), TaintScrubberBlock.Level.MK2));

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
	
	public static final RegistryObject<Block> TAINTED_DAIR_LEAVES = BLOCKS.register("tainted_dair_leaves", () -> taintedOf(ArcanaBlocks.DAIR_LEAVES.get()));
	public static final RegistryObject<Block> TAINTED_DAIR_LOG = BLOCKS.register("tainted_dair_log", () -> taintedOf(ArcanaBlocks.DAIR_LOG.get()));
	public static final RegistryObject<Block> TAINTED_DAIR_PLANKS = BLOCKS.register("tainted_dair_planks", () -> taintedOf(ArcanaBlocks.DAIR_PLANKS.get()));
	public static final RegistryObject<Block> TAINTED_DAIR_SAPLING = BLOCKS.register("tainted_dair_sapling", () -> taintedOf(ArcanaBlocks.DAIR_SAPLING.get()));
	public static final RegistryObject<Block> TAINTED_DAIR_SLAB = BLOCKS.register("tainted_dair_slab", () -> taintedOf(ArcanaBlocks.DAIR_SLAB.get()));
	public static final RegistryObject<Block> TAINTED_DAIR_STAIRS = BLOCKS.register("tainted_dair_stairs", () -> taintedOf(ArcanaBlocks.DAIR_STAIRS.get()));

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

	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_LEAVES = BLOCKS.register("tainted_eucalyptus_leaves", () -> taintedOf(ArcanaBlocks.EUCALYPTUS_LEAVES.get()));
	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_LOG = BLOCKS.register("tainted_eucalyptus_log", () -> taintedOf(ArcanaBlocks.EUCALYPTUS_LOG.get()));
	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_PLANKS = BLOCKS.register("tainted_eucalyptus_planks", () -> taintedOf(ArcanaBlocks.EUCALYPTUS_PLANKS.get()));
	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_SAPLING = BLOCKS.register("tainted_eucalyptus_sapling", () -> taintedOf(ArcanaBlocks.EUCALYPTUS_SAPLING.get()));
	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_SLAB = BLOCKS.register("tainted_eucalyptus_slab", () -> taintedOf(ArcanaBlocks.EUCALYPTUS_SLAB.get()));
	public static final RegistryObject<Block> TAINTED_EUCALYPTUS_STAIRS = BLOCKS.register("tainted_eucalyptus_stairs", () -> taintedOf(ArcanaBlocks.EUCALYPTUS_STAIRS.get()));
	
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

	public static final RegistryObject<Block> TAINTED_GREATWOOD_LEAVES = BLOCKS.register("tainted_greatwood_leaves", () -> taintedOf(ArcanaBlocks.GREATWOOD_LEAVES.get()));
	public static final RegistryObject<Block> TAINTED_GREATWOOD_LOG = BLOCKS.register("tainted_greatwood_log", () -> taintedOf(ArcanaBlocks.GREATWOOD_LOG.get()));
	public static final RegistryObject<Block> TAINTED_GREATWOOD_PLANKS = BLOCKS.register("tainted_greatwood_planks", () -> taintedOf(ArcanaBlocks.GREATWOOD_PLANKS.get()));
	public static final RegistryObject<Block> TAINTED_GREATWOOD_SAPLING = BLOCKS.register("tainted_greatwood_sapling", () -> taintedOf(ArcanaBlocks.GREATWOOD_SAPLING.get()));
	public static final RegistryObject<Block> TAINTED_GREATWOOD_SLAB = BLOCKS.register("tainted_greatwood_slab", () -> taintedOf(ArcanaBlocks.GREATWOOD_SLAB.get()));
	public static final RegistryObject<Block> TAINTED_GREATWOOD_STAIRS = BLOCKS.register("tainted_greatwood_stairs", () -> taintedOf(ArcanaBlocks.GREATWOOD_STAIRS.get()));
	
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

	public static final RegistryObject<Block> TAINTED_HAWTHORN_LEAVES = BLOCKS.register("tainted_hawthorn_leaves", () -> taintedOf(ArcanaBlocks.HAWTHORN_LEAVES.get()));
	public static final RegistryObject<Block> TAINTED_HAWTHORN_LOG = BLOCKS.register("tainted_hawthorn_log", () -> taintedOf(ArcanaBlocks.HAWTHORN_LOG.get()));
	public static final RegistryObject<Block> TAINTED_HAWTHORN_PLANKS = BLOCKS.register("tainted_hawthorn_planks", () -> taintedOf(ArcanaBlocks.HAWTHORN_PLANKS.get()));
	public static final RegistryObject<Block> TAINTED_HAWTHORN_SAPLING = BLOCKS.register("tainted_hawthorn_sapling", () -> taintedOf(ArcanaBlocks.HAWTHORN_SAPLING.get()));
	public static final RegistryObject<Block> TAINTED_HAWTHORN_SLAB = BLOCKS.register("tainted_hawthorn_slab", () -> taintedOf(ArcanaBlocks.HAWTHORN_SLAB.get()));
	public static final RegistryObject<Block> TAINTED_HAWTHORN_STAIRS = BLOCKS.register("tainted_hawthorn_stairs", () -> taintedOf(ArcanaBlocks.HAWTHORN_STAIRS.get()));
	
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

	public static final RegistryObject<Block> TAINTED_WILLOW_LEAVES = BLOCKS.register("tainted_willow_leaves", () -> taintedOf(ArcanaBlocks.WILLOW_LEAVES.get()));
	public static final RegistryObject<Block> TAINTED_WILLOW_LOG = BLOCKS.register("tainted_willow_log", () -> taintedOf(ArcanaBlocks.WILLOW_LOG.get()));
	public static final RegistryObject<Block> TAINTED_WILLOW_PLANKS = BLOCKS.register("tainted_willow_planks", () -> taintedOf(ArcanaBlocks.WILLOW_PLANKS.get()));
	public static final RegistryObject<Block> TAINTED_WILLOW_SAPLING = BLOCKS.register("tainted_willow_sapling", () -> taintedOf(ArcanaBlocks.WILLOW_SAPLING.get()));
	public static final RegistryObject<Block> TAINTED_WILLOW_SLAB = BLOCKS.register("tainted_willow_slab", () -> taintedOf(ArcanaBlocks.WILLOW_SLAB.get()));
	public static final RegistryObject<Block> TAINTED_WILLOW_STAIRS = BLOCKS.register("tainted_willow_stairs", () -> taintedOf(ArcanaBlocks.WILLOW_STAIRS.get()));
	
	// Compressed Resources
	public static final RegistryObject<Block> ARCANIUM_BLOCK = BLOCKS.register("arcanium_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> THAUMIUM_BLOCK = BLOCKS.register("thaumium_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> VOID_METAL_BLOCK = BLOCKS.register("void_metal_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register("silver_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));

	public static final RegistryObject<Block> TAINTED_ARCANIUM_BLOCK = BLOCKS.register("tainted_arcanium_block", () -> taintedOf(ArcanaBlocks.ARCANIUM_BLOCK.get()));
	public static final RegistryObject<Block> TAINTED_THAUMIUM_BLOCK = BLOCKS.register("tainted_thaumium_block", () -> taintedOf(ArcanaBlocks.THAUMIUM_BLOCK.get()));

	//Misc Tainted Blocks
	//public static final RegistryObject<Block> TAINTED_DESTROYED_ORE = BLOCKS.register("tainted_destroyed_ore", () -> Taint.taintedOf(Blocks.STONE_BRICKS));

	//Tainted vanilla blocks
	public static final RegistryObject<Block> TAINTED_CRUST = BLOCKS.register("tainted_crust", () -> taintedOf(Blocks.COBBLESTONE));
	public static final RegistryObject<Block> TAINTED_CRUST_SLAB = BLOCKS.register("tainted_crust_slab", () -> taintedOf(Blocks.COBBLESTONE_SLAB));
	public static final RegistryObject<Block> TAINTED_GRAVEL = BLOCKS.register("tainted_gravel", () -> taintedOf(Blocks.GRAVEL));
	public static final RegistryObject<Block> TAINTED_SAND = BLOCKS.register("tainted_sand", () -> taintedOf(Blocks.SAND));

	public static final RegistryObject<Block> TAINTED_ANDESITE = BLOCKS.register("tainted_andesite", () -> taintedOf(Blocks.ANDESITE));
	public static final RegistryObject<Block> TAINTED_DIORITE = BLOCKS.register("tainted_diorite", () -> taintedOf(Blocks.DIORITE));
	public static final RegistryObject<Block> TAINTED_GRANITE = BLOCKS.register("tainted_granite", () -> taintedOf(Blocks.GRANITE));
	public static final RegistryObject<Block> TAINTED_ROCK = BLOCKS.register("tainted_rock", () -> taintedOf(Blocks.STONE));
	public static final RegistryObject<Block> TAINTED_ROCK_SLAB = BLOCKS.register("tainted_rock_slab", () -> taintedOf(Blocks.STONE_SLAB));

	public static final RegistryObject<Block> TAINTED_SOIL = BLOCKS.register("tainted_soil", () -> taintedOf(Blocks.DIRT));
	public static final RegistryObject<Block> TAINTED_GRASS_BLOCK = BLOCKS.register("tainted_grass_block", () -> taintedOf(Blocks.GRASS_BLOCK));
	public static final RegistryObject<Block> TAINTED_FARMLAND = BLOCKS.register("tainted_farmland", () -> taintedOf(Blocks.FARMLAND));
	public static final RegistryObject<Block> TAINTED_PATH = BLOCKS.register("tainted_path", () -> taintedOf(Blocks.GRASS_PATH));

	public static final RegistryObject<Block> TAINTED_COAL_BLOCK = BLOCKS.register("tainted_coal_block", () -> taintedOf(Blocks.COAL_BLOCK));
	public static final RegistryObject<Block> TAINTED_EMERALD_BLOCK = BLOCKS.register("tainted_emerald_block", () -> taintedOf(Blocks.EMERALD_BLOCK));
	public static final RegistryObject<Block> TAINTED_DIAMOND_BLOCK = BLOCKS.register("tainted_diamond_block", () -> taintedOf(Blocks.DIAMOND_BLOCK));
	public static final RegistryObject<Block> TAINTED_GOLD_BLOCK = BLOCKS.register("tainted_gold_block", () -> taintedOf(Blocks.GOLD_BLOCK));
	public static final RegistryObject<Block> TAINTED_IRON_BLOCK = BLOCKS.register("tainted_iron_block", () -> taintedOf(Blocks.IRON_BLOCK));
	public static final RegistryObject<Block> TAINTED_LAPIS_BLOCK = BLOCKS.register("tainted_lapis_block", () -> taintedOf(Blocks.LAPIS_BLOCK));
	public static final RegistryObject<Block> TAINTED_REDSTONE_BLOCK = BLOCKS.register("tainted_redstone_block", () -> taintedOf(Blocks.REDSTONE_BLOCK));

	public static final RegistryObject<Block> TAINTED_BUSH = BLOCKS.register("tainted_bush", () -> taintedOf(Blocks.GRASS));
	public static final RegistryObject<Block> TAINTED_FLOWER = BLOCKS.register("tainted_flower", () -> taintedOf(Blocks.CORNFLOWER));
	public static final RegistryObject<Block> TAINTED_CARVED_PUMPKIN = BLOCKS.register("tainted_carved_pumpkin", () -> taintedOf(Blocks.CARVED_PUMPKIN));
	public static final RegistryObject<Block> TAINTED_JACK_OLANTERN = BLOCKS.register("tainted_jack_olantern", () -> taintedOf(Blocks.JACK_O_LANTERN));
	public static final RegistryObject<Block> TAINTED_MELON = BLOCKS.register("tainted_melon", () -> taintedOf(Blocks.MELON));
	public static final RegistryObject<Block> TAINTED_MUSHROOM = BLOCKS.register("tainted_mushroom", () -> taintedOf(Blocks.BROWN_MUSHROOM));
	public static final RegistryObject<Block> TAINTED_PUMPKIN = BLOCKS.register("tainted_pumpkin", () -> taintedOf(Blocks.PUMPKIN));

	public static final RegistryObject<Block> TAINTED_COAL_ORE = BLOCKS.register("tainted_coal_ore", () -> taintedOf(Blocks.COAL_ORE));
	public static final RegistryObject<Block> TAINTED_IRON_ORE = BLOCKS.register("tainted_iron_ore", () -> taintedOf(Blocks.IRON_ORE));
	public static final RegistryObject<Block> TAINTED_GOLD_ORE = BLOCKS.register("tainted_gold_ore", () -> taintedOf(Blocks.GOLD_ORE));
	public static final RegistryObject<Block> TAINTED_DIAMOND_ORE = BLOCKS.register("tainted_diamond_ore", () -> taintedOf(Blocks.DIAMOND_ORE));
	public static final RegistryObject<Block> TAINTED_LAPIS_ORE = BLOCKS.register("tainted_lapis_ore", () -> taintedOf(Blocks.LAPIS_ORE));
	public static final RegistryObject<Block> TAINTED_EMERALD_ORE = BLOCKS.register("tainted_emerald_ore", () -> taintedOf(Blocks.EMERALD_ORE));
	public static final RegistryObject<Block> TAINTED_REDSTONE_ORE = BLOCKS.register("tainted_redstone_ore", () -> taintedOf(Blocks.REDSTONE_ORE));

	public static final RegistryObject<Block> TAINTED_ACACIA_LEAVES = BLOCKS.register("tainted_acacia_leaves", () -> taintedOf(Blocks.ACACIA_LEAVES));
	public static final RegistryObject<Block> TAINTED_ACACIA_LOG = BLOCKS.register("tainted_acacia_log", () -> taintedOf(Blocks.ACACIA_LOG));
	public static final RegistryObject<Block> TAINTED_ACACIA_PLANKS = BLOCKS.register("tainted_acacia_planks", () -> taintedOf(Blocks.ACACIA_PLANKS));
	public static final RegistryObject<Block> TAINTED_ACACIA_SAPLING = BLOCKS.register("tainted_acacia_sapling", () -> taintedOf(Blocks.ACACIA_SAPLING));
	public static final RegistryObject<Block> TAINTED_ACACIA_SLAB = BLOCKS.register("tainted_acacia_slab", () -> taintedOf(Blocks.ACACIA_SLAB));
	public static final RegistryObject<Block> TAINTED_ACACIA_STAIRS = BLOCKS.register("tainted_acacia_stairs", () -> taintedOf(Blocks.ACACIA_STAIRS));

	public static final RegistryObject<Block> TAINTED_BIRCH_LEAVES = BLOCKS.register("tainted_birch_leaves", () -> taintedOf(Blocks.BIRCH_LEAVES));
	public static final RegistryObject<Block> TAINTED_BIRCH_LOG = BLOCKS.register("tainted_birch_log", () -> taintedOf(Blocks.BIRCH_LOG));
	public static final RegistryObject<Block> TAINTED_BIRCH_PLANKS = BLOCKS.register("tainted_birch_planks", () -> taintedOf(Blocks.BIRCH_PLANKS));
	public static final RegistryObject<Block> TAINTED_BIRCH_SAPLING = BLOCKS.register("tainted_birch_sapling", () -> taintedOf(Blocks.BIRCH_SAPLING));
	public static final RegistryObject<Block> TAINTED_BIRCH_SLAB = BLOCKS.register("tainted_birch_slab", () -> taintedOf(Blocks.BIRCH_SLAB));
	public static final RegistryObject<Block> TAINTED_BIRCH_STAIRS = BLOCKS.register("tainted_birch_stairs", () -> taintedOf(Blocks.BIRCH_STAIRS));

	public static final RegistryObject<Block> TAINTED_DARKOAK_LEAVES = BLOCKS.register("tainted_darkoak_leaves", () -> taintedOf(Blocks.DARK_OAK_LEAVES));
	public static final RegistryObject<Block> TAINTED_DARKOAK_LOG = BLOCKS.register("tainted_darkoak_log", () -> taintedOf(Blocks.DARK_OAK_LOG));
	public static final RegistryObject<Block> TAINTED_DARKOAK_PLANKS = BLOCKS.register("tainted_darkoak_planks", () -> taintedOf(Blocks.DARK_OAK_PLANKS));
	public static final RegistryObject<Block> TAINTED_DARKOAK_SAPLING = BLOCKS.register("tainted_darkoak_sapling", () -> taintedOf(Blocks.DARK_OAK_SAPLING));
	public static final RegistryObject<Block> TAINTED_DARKOAK_SLAB = BLOCKS.register("tainted_darkoak_slab", () -> taintedOf(Blocks.DARK_OAK_SLAB));
	public static final RegistryObject<Block> TAINTED_DARKOAK_STAIRS = BLOCKS.register("tainted_darkoak_stairs", () -> taintedOf(Blocks.DARK_OAK_STAIRS));

	public static final RegistryObject<Block> TAINTED_JUNGLE_LEAVES = BLOCKS.register("tainted_jungle_leaves", () -> taintedOf(Blocks.JUNGLE_LEAVES));
	public static final RegistryObject<Block> TAINTED_JUNGLE_LOG = BLOCKS.register("tainted_jungle_log", () -> taintedOf(Blocks.JUNGLE_LOG));
	public static final RegistryObject<Block> TAINTED_JUNGLE_PLANKS = BLOCKS.register("tainted_jungle_planks", () -> taintedOf(Blocks.JUNGLE_PLANKS));
	public static final RegistryObject<Block> TAINTED_JUNGLE_SAPLING = BLOCKS.register("tainted_jungle_sapling", () -> taintedOf(Blocks.JUNGLE_SAPLING));
	public static final RegistryObject<Block> TAINTED_JUNGLE_SLAB = BLOCKS.register("tainted_jungle_slab", () -> taintedOf(Blocks.JUNGLE_SLAB));
	public static final RegistryObject<Block> TAINTED_JUNGLE_STAIRS = BLOCKS.register("tainted_jungle_stairs", () -> taintedOf(Blocks.JUNGLE_STAIRS));

	public static final RegistryObject<Block> TAINTED_OAK_LEAVES = BLOCKS.register("tainted_oak_leaves", () -> taintedOf(Blocks.OAK_LEAVES));
	public static final RegistryObject<Block> TAINTED_OAK_LOG = BLOCKS.register("tainted_oak_log", () -> taintedOf(Blocks.OAK_LOG));
	public static final RegistryObject<Block> TAINTED_OAK_PLANKS = BLOCKS.register("tainted_oak_planks", () -> taintedOf(Blocks.OAK_PLANKS));
	public static final RegistryObject<Block> TAINTED_OAK_SAPLING = BLOCKS.register("tainted_oak_sapling", () -> taintedOf(Blocks.OAK_SAPLING));
	public static final RegistryObject<Block> TAINTED_OAK_SLAB = BLOCKS.register("tainted_oak_slab", () -> taintedOf(Blocks.OAK_SLAB));
	public static final RegistryObject<Block> TAINTED_OAK_STAIRS = BLOCKS.register("tainted_oak_stairs", () -> taintedOf(Blocks.OAK_STAIRS));

	public static final RegistryObject<Block> TAINTED_SPRUCE_LEAVES = BLOCKS.register("tainted_spruce_leaves", () -> taintedOf(Blocks.SPRUCE_LEAVES));
	public static final RegistryObject<Block> TAINTED_SPRUCE_LOG = BLOCKS.register("tainted_spruce_log", () -> taintedOf(Blocks.SPRUCE_LOG));
	public static final RegistryObject<Block> TAINTED_SPRUCE_PLANKS = BLOCKS.register("tainted_spruce_planks", () -> taintedOf(Blocks.SPRUCE_PLANKS));
	public static final RegistryObject<Block> TAINTED_SPRUCE_SAPLING = BLOCKS.register("tainted_spruce_sapling", () -> taintedOf(Blocks.SPRUCE_SAPLING));
	public static final RegistryObject<Block> TAINTED_SPRUCE_SLAB = BLOCKS.register("tainted_spruce_slab", () -> taintedOf(Blocks.SPRUCE_SLAB));
	public static final RegistryObject<Block> TAINTED_SPRUCE_STAIRS = BLOCKS.register("tainted_spruce_stairs", () -> taintedOf(Blocks.SPRUCE_STAIRS));

	public static final RegistryObject<Block> TAINTED_POLISHED_GRANITE = BLOCKS.register("tainted_polished_granite", () -> taintedOf(Blocks.POLISHED_GRANITE));
	public static final RegistryObject<Block> TAINTED_POLISHED_GRANITE_SLAB = BLOCKS.register("tainted_polished_granite_slab", () -> taintedOf(Blocks.POLISHED_GRANITE_SLAB));
	public static final RegistryObject<Block> TAINTED_POLISHED_GRANITE_STAIRS = BLOCKS.register("tainted_polished_granite_stairs", () -> taintedOf(Blocks.POLISHED_GRANITE_STAIRS));

	public static final RegistryObject<Block> TAINTED_POLISHED_DIORITE = BLOCKS.register("tainted_polished_diorite", () -> taintedOf(Blocks.POLISHED_DIORITE));
	public static final RegistryObject<Block> TAINTED_POLISHED_DIORITE_SLAB = BLOCKS.register("tainted_polished_diorite_slab", () -> taintedOf(Blocks.POLISHED_DIORITE_SLAB));
	public static final RegistryObject<Block> TAINTED_POLISHED_DIORITE_STAIRS = BLOCKS.register("tainted_polished_diorite_stairs", () -> taintedOf(Blocks.POLISHED_DIORITE_STAIRS));

	public static final RegistryObject<Block> TAINTED_POLISHED_ANDESITE = BLOCKS.register("tainted_polished_andesite", () -> taintedOf(Blocks.POLISHED_ANDESITE));
	public static final RegistryObject<Block> TAINTED_POLISHED_ANDESITE_SLAB = BLOCKS.register("tainted_polished_andesite_slab", () -> taintedOf(Blocks.POLISHED_ANDESITE_SLAB));
	public static final RegistryObject<Block> TAINTED_POLISHED_ANDESITE_STAIRS = BLOCKS.register("tainted_polished_andesite_stairs", () -> taintedOf(Blocks.POLISHED_ANDESITE_STAIRS));

	public static final RegistryObject<Block> TAINTED_ANDESITE_SLAB = BLOCKS.register("tainted_andesite_slab", () -> taintedOf(Blocks.POLISHED_ANDESITE_SLAB));
	public static final RegistryObject<Block> TAINTED_ANDESITE_STAIRS = BLOCKS.register("tainted_andesite_stairs", () -> taintedOf(Blocks.POLISHED_ANDESITE_STAIRS));

	public static final RegistryObject<Block> TAINTED_DIORITE_SLAB = BLOCKS.register("tainted_diorite_slab", () -> taintedOf(Blocks.POLISHED_DIORITE_SLAB));
	public static final RegistryObject<Block> TAINTED_DIORITE_STAIRS = BLOCKS.register("tainted_diorite_stairs", () -> taintedOf(Blocks.POLISHED_DIORITE_STAIRS));

	public static final RegistryObject<Block> TAINTED_GRANITE_SLAB = BLOCKS.register("tainted_granite_slab", () -> taintedOf(Blocks.POLISHED_GRANITE_SLAB));
	public static final RegistryObject<Block> TAINTED_GRANITE_STAIRS = BLOCKS.register("tainted_granite_stairs", () -> taintedOf(Blocks.POLISHED_GRANITE_STAIRS));
}