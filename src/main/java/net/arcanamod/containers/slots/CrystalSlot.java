package net.arcanamod.containers.slots;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class CrystalSlot extends Slot {
	private final Aspect aspect;

	public CrystalSlot(Aspect aspect, IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.aspect = aspect;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return AspectUtils.aspectCrystalItems.contains(stack.getItem());
	}
}
