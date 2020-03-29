package net.kineticdevelopment.arcana.common.objects.containers;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResearchTableContainer extends Container{
	
	private ResearchTableTileEntity te;
	
	public ResearchTableContainer(IInventory playerInventory, ResearchTableTileEntity te){
		this.te = te;
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}
	
	private void addPlayerSlots(IInventory playerInventory){
		// Slots for the main inventory
		for(int row = 0; row < 3; ++row){
			for(int col = 0; col < 9; ++col){
				int x = 9 + col * 18;
				int y = row * 18 + 70;
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
			}
		}
		
		// Slots for the hotbar
		for(int row = 0; row < 9; ++row){
			int x = 9 + row * 18;
			int y = 58 + 70;
			this.addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}
	
	private void addOwnSlots(){
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int x = 9;
		int y = 6;
		
		// TODO: move the two slots to appropriate positions & give icons
		int slotIndex = 0;
		for(int i = 0; i < itemHandler.getSlots(); i++){
			addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
			slotIndex++;
			x += 18;
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
		// TODO: this is a copypaste impl, replace with something more specific
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index < 2){
				if(!this.mergeItemStack(itemstack1, 2, inventorySlots.size(), true))
					return ItemStack.EMPTY;
			}else if(!this.mergeItemStack(itemstack1, 0, 2, false))
				return ItemStack.EMPTY;
			
			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
		}
		
		return itemstack;
	}
	
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
}
