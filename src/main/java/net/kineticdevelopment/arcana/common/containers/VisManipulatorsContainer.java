package net.kineticdevelopment.arcana.common.containers;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.aspects.AspectHandler;
import net.kineticdevelopment.arcana.core.aspects.AspectHandlerCapability;
import net.kineticdevelopment.arcana.core.aspects.VisBattery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VisManipulatorsContainer extends AspectContainer{
	
	// Inventory w/ two slots
	public IInventory manipulatorInv = new InventoryBasic("", false, 2){
		public void markDirty(){
			super.markDirty();
			VisManipulatorsContainer.this.onCraftMatrixChanged(this);
		}
	};
	
	public VisBattery leftSlotStorage = new VisBattery(), rightSlotStorage = new VisBattery();
	public AspectSlot leftStoreSlot, rightStoreSlot;
	public List<AspectSlot> leftScrollableSlots = new ArrayList<>();
	public List<AspectSlot> rightScrollableSlots = new ArrayList<>();
	
	public ScrollRefreshListener scroller;
	
	public VisManipulatorsContainer(IInventory playerInventory){
		// My slots
		// 48, 17
		addSlotToContainer(new Slot(manipulatorInv, 0, 48, 17){
			public boolean isItemValid(@Nonnull ItemStack stack){
				// only vis storages
				return super.isItemValid(stack) && stack.hasCapability(AspectHandlerCapability.ASPECT_HANDLER, null);
			}
		});
		// 112, 17
		addSlotToContainer(new Slot(manipulatorInv, 1, 112, 17){
			public boolean isItemValid(@Nonnull ItemStack stack){
				return super.isItemValid(stack) && stack.hasCapability(AspectHandlerCapability.ASPECT_HANDLER, null);
			}
		});
		
		// Slots for the main inventory
		int baseX = 8, baseY = 174;
		
		for(int row = 0; row < 3; row++)
			for(int col = 0; col < 9; col++){
				int x = baseX + col * 18;
				int y = row * 18 + baseY;
				addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
			}
		
		for(int col = 0; col < 9; col++)
			addSlotToContainer(new Slot(playerInventory, col, baseX + col * 18, baseY + 18 * 3 + 4));
		
		addAspectSlots();
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index < 2){
				if(!mergeItemStack(itemstack1, 2, inventorySlots.size(), true))
					return ItemStack.EMPTY;
			}else if(!mergeItemStack(itemstack1, 0, 2, false))
				return ItemStack.EMPTY;
			
			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
		}
		
		return itemstack;
	}
	
	public void onContainerClosed(EntityPlayer player){
		super.onContainerClosed(player);
		
		if(!player.world.isRemote)
			clearContainer(player, player.world, manipulatorInv);
	}
	
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
	
	public void onCraftMatrixChanged(IInventory inventory){
		super.onCraftMatrixChanged(inventory);
		refreshAspectSlots();
	}
	
	protected void addAspectSlots(){
		aspectSlots.add(leftStoreSlot = new AspectSlot(null, () -> leftSlotStorage, 44, 147, true));
		aspectSlots.add(rightStoreSlot = new AspectSlot(null, () -> rightSlotStorage, 116, 147, true));
		aspectSlots.add(new CombinatorAspectSlot(leftStoreSlot, rightStoreSlot, 80, 146));
	}
	
	protected void refreshAspectSlots(){
		if(aspectSlots.size() > 3)
			aspectSlots.subList(3, aspectSlots.size()).clear();
		leftScrollableSlots.clear();
		rightScrollableSlots.clear();
		
		Supplier<AspectHandler> left = () -> AspectHandler.getFrom(getSlot(0).getStack());
		if(left.get() != null){
			Aspect[] values = left.get().getAllowedAspects().toArray(new Aspect[0]);
			for(int i = 0; i < values.length; i++){
				Aspect aspect = values[i];
				int yy = i / 3;
				int xx = i % 3;
				int x = 11 + 20 * xx;
				int y = 46 + 21 * yy;
				AspectSlot slot = new AspectSlot(aspect, left, x, y);
				// the scroll wheel handles the rest
				if(yy >= 4)
					slot.visible = false;
				aspectSlots.add(slot);
				leftScrollableSlots.add(slot);
			}
		}
		
		Supplier<AspectHandler> right = () -> AspectHandler.getFrom(getSlot(1).getStack());
		if(right.get() != null){
			Aspect[] values = right.get().getAllowedAspects().toArray(new Aspect[0]);
			for(int i = 0; i < values.length; i++){
				Aspect aspect = values[i];
				int yy = i / 3;
				int xx = i % 3;
				int x = 92 + 20 * xx;
				int y = 46 + 20 * yy;
				AspectSlot slot = new AspectSlot(aspect, right, x, y);
				// the scroll wheel handles the rest
				if(yy >= 4)
					slot.visible = false;
				aspectSlots.add(slot);
				rightScrollableSlots.add(slot);
			}
		}
		
		// when on the logical client: tell the GUI, somehow.
		// I'll use an interface, and have VisManipulatorsGUI implement it
		// to avoid ever seeing "GuiScreen".
		if(scroller != null)
			scroller.refreshScrolling();
	}
	
	/**
	 * Only for VisManipulatorsGUI.
	 */
	public interface ScrollRefreshListener{
		void refreshScrolling();
	}
	
	public List<AspectHandler> getOpenHandlers(){
		// left slot storage, right slot storage, left item stack if present, right item stack if present
		List<AspectHandler> ret = new ArrayList<>();
		ret.add(leftSlotStorage);
		ret.add(rightSlotStorage);
		AspectHandler left = AspectHandler.getFrom(getSlot(0).getStack());
		if(left != null)
			ret.add(left);
		AspectHandler right = AspectHandler.getFrom(getSlot(1).getStack());
		if(right != null)
			ret.add(right);
		return ret;
	}
}