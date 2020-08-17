package net.arcanamod.containers;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.tiles.AspectCrystallizerTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectCrystallizerContainer extends Container{
	
	public final IInventory inventory;
	public final PlayerInventory playerInventory;
	public AspectCrystallizerTileEntity te;
	
	public AspectCrystallizerContainer(int id, IInventory inventory, PlayerInventory playerInventory){
		super(ArcanaContainers.ASPECT_CRYSTALLIZER.get(), id);
		this.inventory = inventory;
		this.playerInventory = playerInventory;
		addOwnSlots(inventory);
		addPlayerSlots(playerInventory);
		if(inventory instanceof AspectCrystallizerTileEntity)
			te = (AspectCrystallizerTileEntity)inventory;
	}
	
	public boolean canInteractWith(PlayerEntity player){
		return inventory.isUsableByPlayer(player);
	}
	
	private void addPlayerSlots(IInventory playerInventory){
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 9; ++j)
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
		
		for(int k = 0; k < 9; ++k)
			addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
	}
	
	private void addOwnSlots(IInventory slots){
		// Crystal @ 108,35
		addSlot(new Slot(slots, 0, 108, 35){
			public boolean isItemValid(ItemStack stack){
				return false;
			}
		});
	}
	
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if(slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(index < 9){
				if(!mergeItemStack(itemstack1, 1, 37, true))
					return ItemStack.EMPTY;
			}else if(!mergeItemStack(itemstack1, 0, 1, false))
				return ItemStack.EMPTY;
			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
			if(itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;
			slot.onTake(playerIn, itemstack1);
		}
		
		return itemstack;
	}
}