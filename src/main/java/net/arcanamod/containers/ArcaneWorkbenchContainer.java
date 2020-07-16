package net.arcanamod.containers;

import net.arcanamod.blocks.tiles.ArcaneCraftingTableTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public class ArcaneWorkbenchContainer extends Container {

	ArcaneCraftingTableTileEntity origin;

	public ArcaneWorkbenchContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory, ArcaneCraftingTableTileEntity origin) {
		super(type, id);
		this.origin = origin;
	}

	public ArcaneWorkbenchContainer(int id, PlayerInventory inventory, ArcaneCraftingTableTileEntity origin) {
		this(ArcanaContainers.ARCANE_WORKBENCH.get(), id, inventory, origin);
	}

	public ArcaneWorkbenchContainer(int i, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
		this(ArcanaContainers.ARCANE_WORKBENCH.get(), i, playerInventory, null);
	}

	/**
	 * Determines whether supplied player can use this container
	 *
	 * @param playerIn
	 */
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return this.origin == null || this.origin.isUsableByPlayer(playerIn);
	}
}
