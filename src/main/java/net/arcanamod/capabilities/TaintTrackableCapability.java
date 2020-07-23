package net.arcanamod.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.arcanamod.Arcana.arcLoc;

public class TaintTrackableCapability{
	
	@CapabilityInject(TaintTrackable.class)
	public static Capability<TaintTrackable> TAINT_TRACKABLE_CAPABILITY = null;
	
	public static final ResourceLocation KEY = arcLoc("taint_trackable_capability");
	
	public static void init(){
		CapabilityManager.INSTANCE.register(TaintTrackable.class, new TaintTrackableCapability.Storage(), TaintTrackableImpl::new);
	}
	
	private static class Storage implements Capability.IStorage<TaintTrackable>{
		
		@Nullable
		public INBT writeNBT(Capability<TaintTrackable> capability, TaintTrackable instance, Direction side){
			return instance.serializeNBT();
		}
		
		public void readNBT(Capability<TaintTrackable> capability, TaintTrackable instance, Direction side, INBT nbt){
			if(nbt instanceof CompoundNBT)
				instance.deserializeNBT((CompoundNBT)nbt);
		}
	}
	
	public static class Provider implements ICapabilitySerializable<CompoundNBT>{
		
		private final TaintTrackable cap = new TaintTrackableImpl();
		
		public CompoundNBT serializeNBT(){
			return cap.serializeNBT();
		}
		
		public void deserializeNBT(CompoundNBT nbt){
			cap.deserializeNBT(nbt);
		}
		
		@SuppressWarnings("unchecked")
		@Nonnull
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side){
			return capability == TAINT_TRACKABLE_CAPABILITY ? LazyOptional.of(() -> (T)cap) : LazyOptional.empty();
		}
	}
}