package net.arcanamod.items.recipes;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.containers.slots.WandSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectCraftingInventory extends CraftingInventory{
	
	private final WandSlot wandSlot;
	private final IInventory deferred;
	private final Container eventHandler; // just store this twice

	public AspectCraftingInventory(Container eventHandler, WandSlot wandSlot, int width, int height, IInventory deferred){
		super(eventHandler, width, height);
		this.eventHandler = eventHandler;
		this.wandSlot = wandSlot;
		this.deferred = deferred;
	}

	public WandSlot getWandSlot() {
		return wandSlot;
	}
	
	public int getSizeInventory(){
		return deferred.getSizeInventory();
	}
	
	public ItemStack removeStackFromSlot(int index){
		return deferred.removeStackFromSlot(index);
	}
	
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = deferred.decrStackSize(index, count);
		if (!itemstack.isEmpty()) {
			this.eventHandler.onCraftMatrixChanged(this);
		}
		return itemstack;
	}
	
	public void setInventorySlotContents(int index, ItemStack stack){
		deferred.setInventorySlotContents(index, stack);
		this.eventHandler.onCraftMatrixChanged(this);
	}
	
	public void markDirty(){
		deferred.markDirty();
	}
	
	public void clear(){
		deferred.clear();
	}
	
	public int getInventoryStackLimit(){
		return deferred.getInventoryStackLimit();
	}
	
	public void openInventory(PlayerEntity player){}
	
	public void closeInventory(PlayerEntity player){}
	
	public boolean isItemValidForSlot(int index, ItemStack stack){
		return deferred.isItemValidForSlot(index, stack);
	}
	
	public int count(Item item){
		return deferred.count(item);
	}
	
	public boolean hasAny(Set<Item> set){
		return deferred.hasAny(set);
	}
	
	public boolean isEmpty(){
		return deferred.isEmpty();
	}
	
	public boolean isUsableByPlayer(PlayerEntity player){
		return deferred.isUsableByPlayer(player);
	}
	
	public ItemStack getStackInSlot(int index){
		return deferred.getStackInSlot(index);
	}
}