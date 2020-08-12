package net.arcanamod.containers;

import net.arcanamod.Arcana;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcanaContainers{
	
	public static final DeferredRegister<ContainerType<?>> CON = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Arcana.MODID);
	
	public static final RegistryObject<ContainerType<ResearchTableContainer>> RESEARCH_TABLE = CON.register("research_table", () -> IForgeContainerType.create(ResearchTableContainer::new));
	public static final RegistryObject<ContainerType<ArcaneCraftingTableContainer>> ARCANE_CRAFTING_TABLE = CON.register("arcane_crafting_table", () -> IForgeContainerType.create((id, playerInventory, buffer) -> new ArcaneCraftingTableContainer(id, playerInventory)));
	public static final RegistryObject<ContainerType<AspectCrystallizerContainer>> ASPECT_CRYSTALLIZER = CON.register("aspect_crystallizer", () -> IForgeContainerType.create((id, inventory, buffer) -> new AspectCrystallizerContainer(id, (IInventory)inventory.player.world.getTileEntity(buffer.readBlockPos()), inventory)));
}