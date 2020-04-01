package net.kineticdevelopment.arcana.common.objects.tile;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResearchTableTileEntity extends TileEntity{
	
	public static final ResourceLocation ID = new ResourceLocation("arcana:research_table");
	
	// Three slots for wand, ink, note
	// up to 9 for crafting guesswork + 6 elements for arcane crafting
	// up to, idk, 12 for arcane infusion
	// golemancy will be weird
	// so its ~15 max
	
	// slots 0-2 are always there, the rest are reserved for the games themselves
	
	protected ItemStackHandler items = new ItemStackHandler(15){
		protected void onContentsChanged(int slot){
			super.onContentsChanged(slot);
			markDirty();
		}
	};
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		if(compound.hasKey("items"))
			items.deserializeNBT((NBTTagCompound)compound.getTag("items"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		compound.setTag("items", items.serializeNBT());
		return compound;
	}
	
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)items;
		return super.getCapability(capability, facing);
	}
	
	protected ItemStack note(){
		return items.getStackInSlot(0);
	}
	
	protected ItemStack ink(){
		return items.getStackInSlot(1);
	}
}