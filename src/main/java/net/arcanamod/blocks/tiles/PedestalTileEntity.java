package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PedestalTileEntity extends TileEntity{
	
	public PedestalTileEntity(){
		super(ArcanaTiles.PEDESTAL_TE.get());
	}
	
	protected ItemStackHandler items = new ItemStackHandler(1){
		protected void onContentsChanged(int slot){
			super.onContentsChanged(slot);
			markDirty();
		}
	};
	
	public ItemStack getItem(){
		return items.getStackInSlot(0);
	}
	
	public void setItem(ItemStack stack){
		items.setStackInSlot(0, stack);
	}
	
	@Override
	public void read(BlockState state, CompoundNBT compound){
		super.read(state, compound);
		if(compound.contains("items"))
			items.deserializeNBT(compound.getCompound("items"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound){
		super.write(compound);
		compound.put("items", items.serializeNBT());
		return compound;
	}
	
	public CompoundNBT getUpdateTag(){
		CompoundNBT nbt = super.getUpdateTag();
		nbt.put("items", items.serializeNBT());
		return nbt;
	}
	
	public void handleUpdateTag(BlockState state, CompoundNBT tag){
		super.handleUpdateTag(state, tag);
		items.deserializeNBT(tag.getCompound("items"));
	}
	
	public AxisAlignedBB getRenderBoundingBox(){
		return new AxisAlignedBB(pos, pos.add(1, 2, 1));
	}
}