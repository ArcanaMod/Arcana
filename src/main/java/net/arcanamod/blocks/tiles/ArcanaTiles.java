package net.arcanamod.blocks.tiles;

import com.google.common.collect.Sets;
import com.mojang.datafixers.types.Type;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcanaTiles{
	
	public static final DeferredRegister<TileEntityType<?>> TES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Arcana.MODID);
	
	public static final RegistryObject<TileEntityType<JarTileEntity>> JAR_TE = TES.register("jar", () -> new TileEntityType<>(JarTileEntity::new, Sets.newHashSet(ArcanaBlocks.JAR.get()), null));
	public static final RegistryObject<TileEntityType<AspectBookshelfTileEntity>> ASPECT_SHELF_TE = TES.register("aspect_shelf", () -> new TileEntityType<>(AspectBookshelfTileEntity::new, Sets.newHashSet(ArcanaBlocks.ASPECT_BOOKSHELF.get()), null));
	public static final RegistryObject<TileEntityType<ResearchTableTileEntity>> RESEARCH_TABLE_TE = TES.register("research_table", () -> new TileEntityType<>(ResearchTableTileEntity::new, Sets.newHashSet(ArcanaBlocks.RESEARCH_TABLE.get()), null));
}