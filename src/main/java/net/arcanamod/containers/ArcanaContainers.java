package net.arcanamod.containers;

import com.google.common.collect.Sets;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.NodeTileEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcanaContainers
{
	public static final DeferredRegister<ContainerType<?>> CON = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Arcana.MODID);

	public static final RegistryObject<ContainerType<ResearchTableContainer>> REASERCH_TABLE = CON.register("research_table", () -> IForgeContainerType.create(ResearchTableContainer::new));
}