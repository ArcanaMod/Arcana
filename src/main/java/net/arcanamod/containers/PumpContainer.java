package net.arcanamod.containers;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.pipes.PumpTileEntity;
import net.arcanamod.items.CrystalItem;
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
public class PumpContainer extends Container{
	
	public PumpTileEntity te;
	
	public PumpContainer(int id, PumpTileEntity te, PlayerInventory playerInventory){
		super(ArcanaContainers.PUMP.get(), id);
		this.te = te;
		ItemStackHandlerAsInventory in = new ItemStackHandlerAsInventory(te.inventory, te::markDirty);
		// Filter @ 57,34
		addSlot(new Slot(in, 0, 57, 34){
			public boolean isItemValid(ItemStack stack){
				return stack.getItem() instanceof EnchantedFilterItem;
			}
		});
		// Crystal @ 103,34
		addSlot(new Slot(in, 1, 103, 34){
			public boolean isItemValid(ItemStack stack){
				return stack.getItem() instanceof CrystalItem;
			}
		});
		addPlayerSlots(playerInventory);
	}
	
	public boolean canInteractWith(PlayerEntity playerIn){
		return true;
	}
	
	private void addPlayerSlots(IInventory playerInventory){
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 9; ++j)
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
		
		for(int k = 0; k < 9; ++k)
			addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
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