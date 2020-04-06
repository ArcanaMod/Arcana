package net.kineticdevelopment.arcana.common.containers;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.client.gui.ResearchTableGUI;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.kineticdevelopment.arcana.core.aspects.AspectHandlerCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
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
		int baseX = 139, baseY = ResearchTableGUI.HEIGHT - 61;
		// Slots for the main inventory
		for(int row = 0; row < 3; row++)
			for(int col = 0; col < 9; col++){
				int x = baseX + col * 18;
				int y = row * 18 + baseY;
				addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
			}
		
		for(int row = 0; row < 3; ++row)
			for(int col = 0; col < 3; ++col){
				int x = 79 + col * 18;
				int y = row * 18 + baseY;
				addSlotToContainer(new Slot(playerInventory, col + row * 3, x, y));
			}
	}
	
	private void addOwnSlots(){
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		// 9, 10
		addSlotToContainer(new SlotItemHandler(itemHandler, 0, 9, 10){
			public boolean isItemValid(@Nonnull ItemStack stack){
				// only vis storages
				return super.isItemValid(stack) && stack.hasCapability(AspectHandlerCapability.ASPECT_HANDLER, null);
			}
		});
		// 137, 11
		addSlotToContainer(new SlotItemHandler(itemHandler, 1, 137, 11){
			public boolean isItemValid(@Nonnull ItemStack stack){
				// only ink
				return super.isItemValid(stack) && stack.getItem() == ItemInit.INK;
			}
		});
		// 155, 11
		addSlotToContainer(new SlotItemHandler(itemHandler, 2, 155, 11){
			public boolean isItemValid(@Nonnull ItemStack stack){
				// only notes
				return super.isItemValid(stack) && stack.getItem() == ItemInit.RESEARCH_NOTE || stack.getItem() == ItemInit.RESEARCH_NOTE_COMPLETE;
			}
		});
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index < 3){
				if(!this.mergeItemStack(itemstack1, 3, inventorySlots.size(), true))
					return ItemStack.EMPTY;
			}else if(!this.mergeItemStack(itemstack1, 0, 3, false))
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
