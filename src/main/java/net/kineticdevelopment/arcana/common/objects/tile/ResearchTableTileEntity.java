package net.kineticdevelopment.arcana.common.objects.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResearchTableTileEntity extends TileEntity{
	
	public static final ResourceLocation ID = new ResourceLocation("arcana:research_table");
	
	protected ItemStackHandler items = new ItemStackHandler(2){
		protected void onContentsChanged(int slot){
			super.onContentsChanged(slot);
			markDirty();
		}
		
		protected int getStackLimit(int slot, @Nonnull ItemStack stack){
			return 1;
		}
	};
	
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)items;
		}
		return super.getCapability(capability, facing);
	}
	
	protected ItemStack note(){
		return items.getStackInSlot(0);
	}
	
	protected ItemStack ink(){
		return items.getStackInSlot(1);
	}
}