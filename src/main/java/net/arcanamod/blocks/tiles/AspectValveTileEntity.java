package net.arcanamod.blocks.tiles;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
	
	public void setSuppressedByRedstone(boolean suppress){
		if(suppressedByRedstone != suppress){
			suppressedByRedstone = suppress;
			notifyChange();
		}
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
		suppressedByRedstone = nbt.getBoolean("suppressed");
	}
	
	public CompoundNBT serializeNBT(){
		// save if enabled
		CompoundNBT nbt = new CompoundNBT();
		nbt.putBoolean("enabled", enabled);
		nbt.putBoolean("suppressed", isSuppressedByRedstone());
		return nbt;
	}
	
	@Nonnull
	public CompoundNBT getUpdateTag(){
		CompoundNBT nbt = super.getUpdateTag();
		nbt.putBoolean("suppressed", isSuppressedByRedstone());
		return nbt;
	}
	
	public void handleUpdateTag(BlockState state, CompoundNBT tag){
		super.handleUpdateTag(state, tag);
		setSuppressedByRedstone(tag.getBoolean("suppressed"));
	}
	
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket(){
		return new SUpdateTileEntityPacket(pos, -1, getUpdateTag());
	}
	
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
		handleUpdateTag(getBlockState(), pkt.getNbtCompound());
	}
}