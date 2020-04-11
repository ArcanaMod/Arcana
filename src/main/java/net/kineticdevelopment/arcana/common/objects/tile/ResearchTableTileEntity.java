package net.kineticdevelopment.arcana.common.objects.tile;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.core.aspects.AspectHandlerCapability;
import net.kineticdevelopment.arcana.core.aspects.VisBattery;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResearchTableTileEntity extends TileEntity{
	
	public static final ResourceLocation ID = new ResourceLocation("arcana:research_table");
	
	private boolean shouldDrop = true;
	
	// Three slots for wand (OR any AspectHandler, but research games can only be performed with a wand), ink, note
	// up to 9 for crafting guesswork for arcane crafting
	// up to, idk, 12 for arcane infusion
	// golemancy will be weird
	// so its ~15 max?
	
	// slots 0-2 are always there, the rest are reserved for the games themselves
	
	protected ItemStackHandler items = new ItemStackHandler(15){
		protected void onContentsChanged(int slot){
			super.onContentsChanged(slot);
			markDirty();
		}
	};
	
	protected VisBattery aspects = new VisBattery();
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		if(compound.hasKey("items"))
			items.deserializeNBT(compound.getCompoundTag("items"));
		if(compound.hasKey("aspects"))
			aspects.deserializeNBT(compound.getCompoundTag("aspects"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		compound.setTag("items", items.serializeNBT());
		compound.setTag("aspects", aspects.serializeNBT());
		return compound;
	}
	
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)items;
		else if(capability == AspectHandlerCapability.ASPECT_HANDLER)
			return (T)aspects;
		return super.getCapability(capability, facing);
	}
	
	public NBTTagCompound saveToNBT(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("aspects", aspects.serializeNBT());
		return compound;
	}
	
	public ItemStack visItem(){
		return items.getStackInSlot(0);
	}
	
	public ItemStack ink(){
		return items.getStackInSlot(1);
	}
	
	public ItemStack note(){
		return items.getStackInSlot(2);
	}
	
	public void setShouldDrop(boolean shouldDrop){
		this.shouldDrop = shouldDrop;
	}
	
	public boolean shouldDrop(){
		return shouldDrop;
	}
}