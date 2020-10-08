package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FociForgeTileEntity extends LockableTileEntity{
	
	protected FociForgeTileEntity(TileEntityType<?> typeIn){
		super(typeIn);
	}
	
	protected ItemStackHandler items = new ItemStackHandler(2){
		protected void onContentsChanged(int slot){
			super.onContentsChanged(slot);
			markDirty();
		}
	};
	
	@Override
	protected ITextComponent getDefaultName(){
		return new TranslationTextComponent("container.arcana.foci_forge");
	}
	
	@Override
	protected Container createMenu(int id, PlayerInventory player){
		return null;
	}
	
	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory(){
		return items.getSlots();
	}
	
	@Override
	public boolean isEmpty(){
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index){
		return this.items.getStackInSlot(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count){
		ItemStack stack0 = items.getStackInSlot(index);
		ItemStack stack1 = items.getStackInSlot(index).copy();
		stack0.shrink(count);
		stack1.setCount(count);
		return stack1; //TODO: Check of works fine (custom impl)
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index){
		ItemStack stack = this.items.getStackInSlot(index).copy();
		items.getStackInSlot(index).setCount(0);
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		items.setStackInSlot(index, stack);
		if(stack.getCount() > getInventoryStackLimit()){
			stack.setCount(getInventoryStackLimit());
		}
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity player){
		if(world.getTileEntity(pos) != this){
			return false;
		}else{
			return player.getDistanceSq(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5) <= 64;
		}
	}
	
	@Override
	public void clear(){
		for(int i = 0; i < items.getSlots() - 1; i++){
			items.getStackInSlot(i).setCount(0);
		}
	}
}
