package net.arcanamod.containers.slots;

import net.arcanamod.items.GauntletItem;
import net.arcanamod.items.MagicDeviceItem;
import net.arcanamod.items.StaffItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WandSlot extends Slot {
	private final IWandSlotListener listener;
	public WandSlot(IWandSlotListener listener, IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.listener = listener;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof MagicDeviceItem && !(item instanceof GauntletItem) && !(item instanceof StaffItem);
	}

	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		listener.onWandSlotUpdate();
	}
}
