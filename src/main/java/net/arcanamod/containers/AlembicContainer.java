package net.arcanamod.containers;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.tiles.AlembicTileEntity;
import net.arcanamod.items.EnchantedFilterItem;
import net.arcanamod.util.ItemStackHandlerAsInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlembicContainer extends Container{
	
	public AlembicTileEntity te;
	
	public AlembicContainer(int id, AlembicTileEntity te, PlayerInventory playerInventory){
		super(ArcanaContainers.ALEMBIC.get(), id);
		this.te = te;
		ItemStackHandlerAsInventory in = new ItemStackHandlerAsInventory(te.inventory, te::markDirty);
		// Filter @ 14,14
		addSlot(new Slot(in, 0, 14, 14){
			public boolean isItemValid(ItemStack stack){
				return stack.getItem() instanceof EnchantedFilterItem;
			}
		});
		// Fuel @ 14,101
		addSlot(new Slot(in, 1, 14, 101){
			public boolean isItemValid(ItemStack stack){
				return /*stack.getItem() instanceof EnchantedFilterItem*/true;
			}
		});
		addPlayerSlots(playerInventory);
	}
	
	public boolean canInteractWith(PlayerEntity player){
		return true;
	}
	
	private void addPlayerSlots(IInventory playerInventory){
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 9; ++j)
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
		
		for(int k = 0; k < 9; ++k)
			addSlot(new Slot(playerInventory, k, 8 + k * 18, 198));
	}
	
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if(slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(index < 9){
				if(!mergeItemStack(itemstack1, 2, 37, true))
					return ItemStack.EMPTY;
			}else if(!mergeItemStack(itemstack1, 0, 2, false))
				return ItemStack.EMPTY;
			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
			if(itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;
			slot.onTake(player, itemstack1);
		}
		
		return itemstack;
	}
}