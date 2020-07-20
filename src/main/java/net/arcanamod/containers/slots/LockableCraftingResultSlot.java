package net.arcanamod.containers.slots;

import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.util.inventories.AspectCraftingInventory;
import net.arcanamod.util.recipes.ArcaneCraftingShapedRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class LockableCraftingResultSlot extends CraftingResultSlot {
	private WandSlot slot;
	private AspectCraftingInventory craftingInventory;

	public LockableCraftingResultSlot(PlayerEntity player, AspectCraftingInventory craftingInventory, IInventory inventoryIn, WandSlot slot, int slotIndex, int xPosition, int yPosition) {
		super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
		this.slot = slot;
		this.craftingInventory = craftingInventory;
	}

	@Override
	public boolean canTakeStack(PlayerEntity playerIn) {
		return IAspectHandler.getFrom(slot.getStack()) != null;
	}
}
