package net.arcanamod.containers.slots;

import net.arcanamod.items.WandItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class WandSlot extends Slot {
	private final IWandSlotListener listener;
	public WandSlot(IWandSlotListener listener, IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.listener = listener;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof WandItem;
	}

	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		listener.onWandSlotUpdate();
	}
}
