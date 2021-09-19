package net.arcanamod.blocks.pipes;

import net.arcanamod.blocks.tiles.ArcanaTiles;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;

public class PipeWindowTileEntity extends TubeTileEntity implements ITickableTileEntity{
	
	private long lastTransferTime = -1;
	
	public PipeWindowTileEntity(){
		super(ArcanaTiles.ASPECT_WINDOW_TE.get());
	}
	
	public void deserializeNBT(CompoundNBT nbt){
		lastTransferTime = nbt.getInt("lastTransferTime");
	}
	
	public CompoundNBT serializeNBT(){
		// save if enabled
		CompoundNBT nbt = new CompoundNBT();
		nbt.putLong("lastTransferTime", lastTransferTime);
		return nbt;
	}
	
	public long getLastTransferTime(){
		return lastTransferTime;
	}
	
	public void addSpeck(AspectSpeck speck){
		super.addSpeck(speck);
		lastTransferTime = getWorld().getGameTime();
	}
}