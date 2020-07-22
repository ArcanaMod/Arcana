package net.arcanamod.blocks.tiles;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.common.util.Constants;

public class AspectWindowTileEntity extends AspectTubeTileEntity implements ITickableTileEntity{
	
	private Aspect lastTransferAspect = Aspects.AIR;
	private int lastTransferTime = -11;
	
	public AspectWindowTileEntity(){
		super(ArcanaTiles.ASPECT_WINDOW_TE.get());
	}
	
	public int getColor(){
		int elapsed = (int)(getWorld().getGameTime() - lastTransferTime);
		if(elapsed > 12)
			return 0xFFFFFF;
		else
			return lastTransferAspect.getColorRange().get(elapsed / 3);
	}
	
	void notifyAspect(Aspect aspect){
		lastTransferAspect = aspect;
		lastTransferTime = (int)world.getGameTime();
		BlockState state = world.getBlockState(getPos());
		world.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.RERENDER_MAIN_THREAD);
		markDirty();
	}
	
	public void deserializeNBT(CompoundNBT nbt){
		lastTransferAspect = Aspects.valueOf(nbt.getString("lastTransferAspect"));
		lastTransferTime = nbt.getInt("lastTransferTime");
	}
	
	public CompoundNBT serializeNBT(){
		// save if enabled
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("lastTransferAspect", lastTransferAspect.name());
		nbt.putInt("lastTransferTime", lastTransferTime);
		return nbt;
	}
	
	public void tick(){
		int elapsed = (int)(getWorld().getGameTime() - lastTransferTime);
		if(elapsed < 14){
			BlockState state = world.getBlockState(getPos());
			world.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.RERENDER_MAIN_THREAD);
		}
	}
	
	public int getLastTransferTime(){
		return lastTransferTime;
	}
}