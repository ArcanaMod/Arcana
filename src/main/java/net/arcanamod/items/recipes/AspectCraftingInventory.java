package net.arcanamod.items.recipes;

import net.arcanamod.containers.slots.WandSlot;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;

public class AspectCraftingInventory extends CraftingInventory {
	private WandSlot wandSlot;

	public AspectCraftingInventory(Container eventHandlerIn, WandSlot wandSlot, int width, int height) {
		super(eventHandlerIn, width, height);
		this.wandSlot = wandSlot;
	}

	public WandSlot getWandSlot() {
		return wandSlot;
	}
}
