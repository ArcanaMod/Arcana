package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.VisBattery;
import net.arcanamod.aspects.VisHandlerCapability;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
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
	
	public ResearchTableTileEntity(){
		super(ArcanaTiles.RESEARCH_TABLE_TE.get());
	}
	
	/*
	
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
	public void readFromNBT(CompoundNBT compound){
		super.readFromNBT(compound);
		if(compound.hasKey("items"))
			items.deserializeNBT(compound.getCompoundTag("items"));
		if(compound.hasKey("aspects"))
			aspects.deserializeNBT(compound.getCompoundTag("aspects"));
	}
	
	@Override
	public CompoundNBT writeToNBT(CompoundNBT compound){
		super.writeToNBT(compound);
		compound.setTag("items", items.serializeNBT());
		compound.setTag("aspects", aspects.serializeNBT());
		return compound;
	}
	
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing){
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)items;
		else if(capability == VisHandlerCapability.ASPECT_HANDLER)
			return (T)aspects;
		return super.getCapability(capability, facing);
	}
	
	public CompoundNBT saveToNBT(){
		CompoundNBT compound = new CompoundNBT();
		compound.setTag("aspects", aspects.serializeNBT());
		return compound;
	}
	
	public ItemStack visItem(){
		return items.getStackInSlot(0);
	}
	
	public ItemStack ink(){
		return items.getStackInSlot(1);
	}
	
	@Nonnull
	public ItemStack note(){
		return items.getStackInSlot(2);
	}
	
	public void setShouldDrop(boolean shouldDrop){
		this.shouldDrop = shouldDrop;
	}
	
	public boolean shouldDrop(){
		return shouldDrop;
	}*/
}