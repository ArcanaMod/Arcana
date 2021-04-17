package net.arcanamod.blocks.tiles;

import com.google.common.collect.Sets;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("ConstantConditions")
public class ArcanaTiles{
	
	public static final DeferredRegister<TileEntityType<?>> TES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Arcana.MODID);

	public static final RegistryObject<TileEntityType<JarTileEntity>> JAR_TE =
			TES.register("jar", () -> new TileEntityType<>(JarTileEntity::new, Sets.newHashSet(ArcanaBlocks.JAR.get(), ArcanaBlocks.SECURE_JAR.get(), ArcanaBlocks.VOID_JAR.get(), ArcanaBlocks.VACUUM_JAR.get(), ArcanaBlocks.PRESSURE_JAR.get()), null));
	public static final RegistryObject<TileEntityType<AspectBookshelfTileEntity>> ASPECT_SHELF_TE =
			TES.register("aspect_shelf", () -> new TileEntityType<>(AspectBookshelfTileEntity::new, Sets.newHashSet(ArcanaBlocks.ASPECT_BOOKSHELF.get(), ArcanaBlocks.ASPECT_BOOKSHELF_BLOCK.get()), null));
	public static final RegistryObject<TileEntityType<ResearchTableTileEntity>> RESEARCH_TABLE_TE =
			TES.register("research_table", () -> new TileEntityType<>(ResearchTableTileEntity::new, Sets.newHashSet(ArcanaBlocks.RESEARCH_TABLE.get()), null));
	public static final RegistryObject<TileEntityType<FociForgeTileEntity>> FOCI_FORGE_TE =
			TES.register("foci_forge", () -> new TileEntityType<>(FociForgeTileEntity::new, Sets.newHashSet(ArcanaBlocks.FOCI_FORGE.get()), null));
	public static final RegistryObject<TileEntityType<AspectTesterTileEntity>> ASPECT_TESTER =
			TES.register("aspect_tester", () -> new TileEntityType<>(AspectTesterTileEntity::new, Sets.newHashSet(ArcanaBlocks.ASPECT_TESTER.get()), null));
	public static final RegistryObject<TileEntityType<TaintScrubberTileEntity>> TAINT_SCRUBBER_TE =
			TES.register("taint_scrubber", () -> new TileEntityType<>(TaintScrubberTileEntity::new, Sets.newHashSet(ArcanaBlocks.TAINT_SCRUBBER_MK1.get()), null));
	public static final RegistryObject<TileEntityType<AspectTubeTileEntity>> ASPECT_TUBE_TE =
			TES.register("essentia_tube", () -> new TileEntityType<>(AspectTubeTileEntity::new, Sets.newHashSet(ArcanaBlocks.ASPECT_TUBE.get()), null));
	public static final RegistryObject<TileEntityType<AspectValveTileEntity>> ASPECT_VALVE_TE =
			TES.register("essentia_valve", () -> new TileEntityType<>(AspectValveTileEntity::new, Sets.newHashSet(ArcanaBlocks.ASPECT_VALVE.get()), null));
	public static final RegistryObject<TileEntityType<AspectWindowTileEntity>> ASPECT_WINDOW_TE =
			TES.register("essentia_window", () -> new TileEntityType<>(AspectWindowTileEntity::new, Sets.newHashSet(ArcanaBlocks.ASPECT_WINDOW.get()), null));
	public static final RegistryObject<TileEntityType<PedestalTileEntity>> PEDESTAL_TE =
			TES.register("pedestal", () -> new TileEntityType<>(PedestalTileEntity::new, Sets.newHashSet(ArcanaBlocks.PEDESTAL.get()), null));
	public static final RegistryObject<TileEntityType<AlembicTileEntity>> ALEMBIC_TE =
			TES.register("alembic", () -> new TileEntityType<>(AlembicTileEntity::new, Sets.newHashSet(ArcanaBlocks.ALEMBIC.get()), null));
	public static final RegistryObject<TileEntityType<CrucibleTileEntity>> CRUCIBLE_TE =
			TES.register("crucible", () -> new TileEntityType<>(CrucibleTileEntity::new, Sets.newHashSet(ArcanaBlocks.CRUCIBLE.get()), null));
	public static final RegistryObject<TileEntityType<ArcaneCraftingTableTileEntity>> ARCANE_WORKBENCH_TE =
			TES.register("arcane_crafting_table", () -> new TileEntityType<>(ArcaneCraftingTableTileEntity::new, Sets.newHashSet(ArcanaBlocks.ARCANE_CRAFTING_TABLE.get()), null));
	public static final RegistryObject<TileEntityType<AspectCrystallizerTileEntity>> ASPECT_CRYSTALLIZER_TE =
			TES.register("aspect_crystallizer", () -> new TileEntityType<>(AspectCrystallizerTileEntity::new, Sets.newHashSet(ArcanaBlocks.ASPECT_CRYSTALLIZER.get()), null));
	public static final RegistryObject<TileEntityType<VacuumTileEntity>> VACUUM_TE =
			TES.register("vacuum", () -> new TileEntityType<>(VacuumTileEntity::new, Sets.newHashSet(ArcanaBlocks.VACUUM_BLOCK.get()), null));
	public static final RegistryObject<TileEntityType<WardenedBlockTileEntity>> WARDENED_BLOCK_TE =
			TES.register("wardened_block", () -> new TileEntityType<>(WardenedBlockTileEntity::new, Sets.newHashSet(ArcanaBlocks.WARDENED_BLOCK.get()), null));
}