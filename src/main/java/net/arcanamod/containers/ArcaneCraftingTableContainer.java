package net.arcanamod.containers;

import net.arcanamod.aspects.Aspects;
import net.arcanamod.containers.slots.ArcaneCraftingTableOutputSlot;
import net.arcanamod.containers.slots.WandSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public class ArcaneCraftingTableContainer extends Container {

	public final IInventory inventory;
	//    W        ->    0
	//   ***       ->   234
	//   ***    O  ->   567    1
	//   ***       ->   89!
	//             ->

	public ArcaneCraftingTableContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory, IInventory inventory) {
		super(type, id);
		this.inventory = inventory;
		this.addSlot(new WandSlot(inventory, 0, 65, 14));
		this.addSlot(new ArcaneCraftingTableOutputSlot(inventory, 1, 160, 64));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(inventory,j + i * 3 + 2, 42 + j * 23, 41 + i * 23));
			}
		}
		addPlayerSlots(playerInventory);
	}

	public ArcaneCraftingTableContainer(int id, PlayerInventory playerInventory, IInventory inventory) {
		this(ArcanaContainers.ARCANE_CRAFTING_TABLE.get(), id, playerInventory, inventory);
	}

	public ArcaneCraftingTableContainer(int i, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
		this(ArcanaContainers.ARCANE_CRAFTING_TABLE.get(), i, playerInventory, new Inventory(15));
	}

	/**
	 * Determines whether supplied player can use this container
	 *
	 * @param playerIn
	 */
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return this.inventory == null || this.inventory.isUsableByPlayer(playerIn);
	}

	private void addPlayerSlots(IInventory playerInventory){
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 16 + j * 18, 151 + i * 18));
			}
		}

		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 16 + k * 18, 209));
		}
	}
}
