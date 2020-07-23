package net.arcanamod.capabilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Attached to all living entities. Provides methods for checking whether an entity is within a taint biome,
 * whether it should be tracked, and setting these values.
 */
public interface TaintTrackable{
	
	boolean isTracking();
	
	void setTracking(boolean tracking);
	
	boolean isInTaintBiome();
	
	void setInTaintBiome(boolean inTaintBiome);
	
	int getTimeInTaintBiome();
	
	void setTimeInTaintBiome(int timeInTaintBiome);
	
	void addTimeInTaintBiome(int timeInTaintBiome);
	
	CompoundNBT serializeNBT();
	
	void deserializeNBT(@Nonnull CompoundNBT data);
	
	@SuppressWarnings("ConstantConditions")
	@Nullable
	static TaintTrackable getFrom(@Nonnull LivingEntity holder){
		return holder.getCapability(TaintTrackableCapability.TAINT_TRACKABLE_CAPABILITY, null).orElse(null);
	}
}