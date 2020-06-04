package net.arcanamod.blocks;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.bases.*;
import net.arcanamod.entities.ArcanaEntities;
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
 * @see ArcanaEntities
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
	
	// Functional
	public static final RegistryObject<Block> AMBER_ORE = BLOCKS.register("amber_ore", () -> new Block(create(ROCK).harvestLevel(1).hardnessAndResistance(3.0F,3.0F)));
	public static final RegistryObject<Block> SILVER_ORE = BLOCKS.register("silver_ore", () -> new Block(create(ROCK).hardnessAndResistance(3.0F,3.0F)));
	public static final RegistryObject<Block> INFUSION_ARCANE_STONE = BLOCKS.register("infusion_arcane_stone", () -> new Block(create(ROCK).hardnessAndResistance(2.0F, 6.0F)));
	public static final RegistryObject<Block> MAGICAL_GRASS = BLOCKS.register("magical_grass", () -> new Block(from(Blocks.GRASS_BLOCK)));
	public static final RegistryObject<Block> TABLE = BLOCKS.register("table", () -> new WaterloggableBlock(create(WOOD).hardnessAndResistance(2).notSolid()));
	
	// Functional Blocks
	public static final RegistryObject<Block> JAR = BLOCKS.register("jar", () -> new BlockJar(create(GLASS)));
	public static final RegistryObject<Block> ASPECT_BOOKSHELF = BLOCKS.register("aspect_bookshelf", () -> new BlockAspectBookshelf(create(WOOD).hardnessAndResistance(6)));
	public static final RegistryObject<Block> RESEARCH_TABLE = BLOCKS.register("research_table", () -> new ResearchTableBlock(create(WOOD).hardnessAndResistance(3).notSolid()));
	public static final RegistryObject<Block> ARCANE_CRAFTING_TABLE = BLOCKS.register("arcane_crafting_table", () -> new WaterloggableBlock(create(WOOD).hardnessAndResistance(2).notSolid()));
	public static final RegistryObject<Block> CRUCIBLE = BLOCKS.register("crucible", () -> new CrucibleBlock(create(IRON, MaterialColor.STONE).hardnessAndResistance(2).notSolid()));
	public static final RegistryObject<Block> INFUSION_PEDESTAL = BLOCKS.register("infusion_pedestal", () -> new PedestalBlock(create(ROCK).hardnessAndResistance(3).notSolid()));
	
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
	
	// Compressed Resources
	public static final RegistryObject<Block> ARCANIUM_BLOCK = BLOCKS.register("arcanium_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> THAUMIUM_BLOCK = BLOCKS.register("thaumium_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> VOID_METAL_BLOCK = BLOCKS.register("void_metal_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register("silver_block", () -> new Block(create(IRON).hardnessAndResistance(6).sound(SoundType.METAL)));
}