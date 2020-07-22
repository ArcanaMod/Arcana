package net.arcanamod.items.recipes;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.blocks.tiles.CrucibleTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AlchemyInventory implements IInventory{
	
	ItemStack stack = ItemStack.EMPTY;
	CrucibleTileEntity crucible;
	PlayerEntity crafter;
	
	public AlchemyInventory(CrucibleTileEntity crucible, PlayerEntity crafter){
		this.crucible = crucible;
		this.crafter = crafter;
	}
	
	public int getSizeInventory(){
		return 1;
	}
	
	public boolean isEmpty(){
		return stack.isEmpty() && crucible.getAspectStackMap().isEmpty();
	}
	
	public ItemStack getStackInSlot(int index){
		return index == 0 ? stack : ItemStack.EMPTY;
	}
	
	public ItemStack decrStackSize(int index, int count){
		return index == 0 ? stack.split(count) : ItemStack.EMPTY;
	}
	
	public ItemStack removeStackFromSlot(int index){
		ItemStack result = ItemStack.EMPTY;
		if(index == 0){
			result = stack;
			stack = ItemStack.EMPTY;
		}
		return result;
	}
	
	public void setInventorySlotContents(int index, ItemStack stack){
		if(index == 0)
			this.stack = stack;
	}
	
	public void markDirty(){
		crucible.markDirty();
	}
	
	public boolean isUsableByPlayer(PlayerEntity player){
		return true;
	}
	
	public Map<Aspect, AspectStack> getAspectMap(){
		return crucible.getAspectStackMap();
	}
	
	public PlayerEntity getCrafter(){
		return crafter;
	}
	
	public void clear(){
		stack = ItemStack.EMPTY;
		// eww
		crucible.getAspectStackMap().clear();
	}
}
