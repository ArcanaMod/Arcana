package net.arcanamod.blocks.tiles;

import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundNBT;

public class AspectValveTileEntity extends AspectTubeTileEntity{
	
	private boolean enabled = true;
	private boolean suppressedByRedstone = false;
	private long lastChangedTick = -1;
	
	public AspectValveTileEntity(){
		super(ArcanaTiles.ASPECT_VALVE_TE.get());
	}
	
	public boolean enabled(){
		return enabled && !suppressedByRedstone;
	}
	
	public boolean enabledByHand(){
		return enabled;
	}
	
	public void setEnabledAndNotify(boolean enabled){
		this.enabled = enabled;
		notifyChange();
	}
	
	public boolean isSuppressedByRedstone(){
		return suppressedByRedstone;
	}
	
	public void setSuppressedByRedstone(boolean suppressedByRedstone){
		this.suppressedByRedstone = suppressedByRedstone;
		notifyChange();
	}
	
	@SuppressWarnings("ConstantConditions")
	private void notifyChange(){
		lastChangedTick = world.getGameTime();
		scan(Sets.newHashSet(getPos()));
	}
	
	public long getLastChangedTick(){
		return lastChangedTick;
	}
	
	public void deserializeNBT(CompoundNBT nbt){
		enabled = nbt.getBoolean("enabled");
	}
	
	public CompoundNBT serializeNBT(){
		// save if enabled
		CompoundNBT nbt = new CompoundNBT();
		nbt.putBoolean("enabled", enabled);
		return nbt;
	}
}