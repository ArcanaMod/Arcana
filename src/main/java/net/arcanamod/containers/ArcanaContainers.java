package net.arcanamod.containers;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.pipes.PumpTileEntity;
import net.arcanamod.blocks.tiles.AlembicTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcanaContainers{
	
	public static final DeferredRegister<ContainerType<?>> CON = DeferredRegister.create(ForgeRegistries.CONTAINERS, Arcana.MODID);

	public static final RegistryObject<ContainerType<FociForgeContainer>> FOCI_FORGE = CON.register("foci_forge", () -> IForgeContainerType.create(FociForgeContainer::new));
	public static final RegistryObject<ContainerType<ResearchTableContainer>> RESEARCH_TABLE = CON.register("research_table", () -> IForgeContainerType.create(ResearchTableContainer::new));
	public static final RegistryObject<ContainerType<ArcaneCraftingTableContainer>> ARCANE_CRAFTING_TABLE = CON.register("arcane_crafting_table", () -> IForgeContainerType.create((id, inventory, buffer) -> new ArcaneCraftingTableContainer(id, inventory, (IInventory)inventory.player.world.getTileEntity(buffer.readBlockPos()))));
	public static final RegistryObject<ContainerType<AspectCrystallizerContainer>> ASPECT_CRYSTALLIZER = CON.register("aspect_crystallizer", () -> IForgeContainerType.create((id, inventory, buffer) -> new AspectCrystallizerContainer(id, (IInventory)inventory.player.world.getTileEntity(buffer.readBlockPos()), inventory)));
	public static final RegistryObject<ContainerType<AlembicContainer>> ALEMBIC = CON.register("alembic", () -> IForgeContainerType.create((id, inventory, buffer) -> new AlembicContainer(id, (AlembicTileEntity)inventory.player.world.getTileEntity(buffer.readBlockPos()), inventory)));
	public static final RegistryObject<ContainerType<PumpContainer>> PUMP = CON.register("essentia_pump", () -> IForgeContainerType.create((id, inventory, buffer) -> new PumpContainer(id, (PumpTileEntity)inventory.player.world.getTileEntity(buffer.readBlockPos()), inventory)));
}