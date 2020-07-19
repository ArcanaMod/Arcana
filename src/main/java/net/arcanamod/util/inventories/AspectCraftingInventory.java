package net.arcanamod.util.inventories;

import net.arcanamod.aspects.IAspectHandler;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;

public class AspectCraftingInventory extends CraftingInventory {
	protected IAspectHandler handler;

	public AspectCraftingInventory(Container eventHandlerIn, IAspectHandler handler, int width, int height) {
		super(eventHandlerIn, width, height);
		this.handler = handler;
	}

	public IAspectHandler getAspectHandler() {
		return handler;
	}
}
