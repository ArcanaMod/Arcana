package net.kineticdevelopment.arcana.common.containers;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.client.gui.VisManipulatorsGUI;
import net.kineticdevelopment.arcana.core.aspects.AspectHandlerCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VisManipulatorsContainer extends Container{
	
	// Inventory w/ two slots
	public IInventory manipulatorInv = new InventoryBasic("", false, 2){
		public void markDirty(){
			super.markDirty();
			VisManipulatorsContainer.this.onCraftMatrixChanged(this);
		}
	};
	
	List<ItemChangeListerer> listeners = new ArrayList<>();
	
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
		int baseX = 8, baseY = VisManipulatorsGUI.HEIGHT - 82;
		
		for(int row = 0; row < 3; row++)
			for(int col = 0; col < 9; col++){
				int x = baseX + col * 18;
				int y = row * 18 + baseY;
				addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
			}
		
		for(int col = 0; col < 9; col++)
			addSlotToContainer(new Slot(playerInventory, col, baseX + col * 18, baseY + 18 * 3 + 4));
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
		listeners.forEach(ItemChangeListerer::onChange);
	}
	
	// TODO: kinda ugly solution. possible fixes:
	//  - make this more standard and less ugly (like w/ a ContainerBase)
	//  - make AspectSlots owned by the container (preferable, might do this anyways)
	
	public void addChangeListener(ItemChangeListerer listener){
		listeners.add(listener);
	}
	
	public void clearListeners(){
		listeners.clear();
	}
	
	@FunctionalInterface
	public interface ItemChangeListerer{
		void onChange();
	}
}