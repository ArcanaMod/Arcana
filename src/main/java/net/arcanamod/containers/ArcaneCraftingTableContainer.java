package net.arcanamod.containers;

import net.arcanamod.aspects.Aspects;
import net.arcanamod.containers.slots.ArcaneCraftingTableOutputSlot;
import net.arcanamod.containers.slots.CrystalSlot;
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
	//    C        ->    1
	// C *** C     -> 0 789 2
	//   ***    O  ->   !@#    6
	// C *** C     -> 4 $%^ 3
	//    C        ->    5

	public ArcaneCraftingTableContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory, IInventory inventory) {
		super(type, id);
		this.inventory = inventory;
		this.addSlot(new CrystalSlot(Aspects.FIRE, inventory, 0, 21, 39));
		this.addSlot(new CrystalSlot(Aspects.AIR, inventory, 1, 65, 14));
		this.addSlot(new CrystalSlot(Aspects.WATER, inventory, 2, 109, 39));
		this.addSlot(new CrystalSlot(Aspects.ORDER, inventory, 3, 109, 89));
		this.addSlot(new CrystalSlot(Aspects.EARTH, inventory, 4, 21, 89));
		this.addSlot(new CrystalSlot(Aspects.CHAOS, inventory, 5, 65, 114));
		this.addSlot(new ArcaneCraftingTableOutputSlot(inventory, 6, 160, 64));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(inventory,j + i * 3 + 7, 42 + j * 23, 41 + i * 23));
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
