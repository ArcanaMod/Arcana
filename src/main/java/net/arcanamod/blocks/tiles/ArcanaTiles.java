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
	
	public static final DeferredRegister<TileEntityType<?>> TES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Arcana.MODID);

	public static final RegistryObject<TileEntityType<JarTileEntity>> JAR_TE =
			TES.register("jar", () -> new TileEntityType<>(JarTileEntity::new, Sets.newHashSet(ArcanaBlocks.JAR.get(),ArcanaBlocks.SECURE_JAR.get(),ArcanaBlocks.VOID_JAR.get()), null));
	public static final RegistryObject<TileEntityType<AspectBookshelfTileEntity>> ASPECT_SHELF_TE =
			TES.register("aspect_shelf", () -> new TileEntityType<>(AspectBookshelfTileEntity::new, Sets.newHashSet(ArcanaBlocks.ASPECT_BOOKSHELF.get()), null));
	public static final RegistryObject<TileEntityType<ResearchTableTileEntity>> RESEARCH_TABLE_TE =
			TES.register("research_table", () -> new TileEntityType<>(ResearchTableTileEntity::new, Sets.newHashSet(ArcanaBlocks.RESEARCH_TABLE.get()), null));
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
	public static final RegistryObject<TileEntityType<ArcaneCraftingTableTileEntity>> ARCANE_WORKBENCH_TE =
			TES.register("arcane_crafting_table", () -> new TileEntityType<>(ArcaneCraftingTableTileEntity::new, Sets.newHashSet(ArcanaBlocks.ARCANE_CRAFTING_TABLE.get()), null));
}