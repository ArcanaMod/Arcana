package net.arcanamod.util;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemStackHandlerAsInventory implements IInventory{
	
	private ItemStackHandler handler;
	public Runnable onDirty;
	
	public ItemStackHandlerAsInventory(@Nonnull ItemStackHandler handler, @Nullable Runnable onDirty){
		this.handler = handler;
		this.onDirty = onDirty;
	}
	
	public int getSizeInventory(){
		return handler.getSlots();
	}
	
	// todo: implement
	public boolean isEmpty(){
		return false;
	}
	
	public ItemStack getStackInSlot(int index){
		return handler.getStackInSlot(index);
	}
	
	public ItemStack decrStackSize(int index, int count){
		return handler.extractItem(index, count, false);
	}
	
	public ItemStack removeStackFromSlot(int index){
		return handler.extractItem(index, handler.getStackInSlot(index).getCount(), false);
	}
	
	public void setInventorySlotContents(int index, ItemStack stack){
		handler.setStackInSlot(index, stack);
	}
	
	public void markDirty(){
		if(onDirty != null)
			onDirty.run();
	}
	
	public boolean isUsableByPlayer(PlayerEntity player){
		return true;
	}
	
	public void clear(){
		for(int i = 0; i < handler.getSlots() - 1; i++)
			handler.getStackInSlot(i).setCount(0);
	}
}
