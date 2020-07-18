package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.containers.ArcaneCraftingTableContainer;
import net.arcanamod.util.recipes.ArcanaRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ArcaneCraftingTableTileEntity extends LockableTileEntity implements ISidedInventory {

	protected NonNullList<ItemStack> items = NonNullList.withSize(11, ItemStack.EMPTY);

	public ArcaneCraftingTableTileEntity() {
		super(ArcanaTiles.ARCANE_WORKBENCH_TE.get());
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.arcane_workbench");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new ArcaneCraftingTableContainer(id,player,this);
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		return this.items.get(index);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.items, index, count);
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.items.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 *
	 * @param player
	 */
	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {

	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return new int[0];
	}

	/**
	 * Returns true if automation can insert the given item in the given slot from the given side.
	 *
	 * @param index
	 * @param itemStackIn
	 * @param direction
	 */
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
		return false;
	}

	/**
	 * Returns true if automation can extract the given item in the given slot from the given side.
	 *
	 * @param index
	 * @param stack
	 * @param direction
	 */
	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		return false;
	}
}