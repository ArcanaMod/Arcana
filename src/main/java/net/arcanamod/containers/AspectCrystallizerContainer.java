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
		addPlayerSlots(playerInventory);
		addOwnSlots(inventory);
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
		// Full aspect container @ 17,17
		addSlot(new Slot(slots, 0, 17, 17));
		// Empty aspect container @ 17,53
		addSlot(new Slot(slots, 1, 17, 53));
		// Crystal @ 122,35
		addSlot(new Slot(slots, 2, 122, 35){
			public boolean isItemValid(ItemStack stack){
				return false;
			}
		});
	}
}