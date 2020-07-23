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

public class ResearcherCapability{
	
	@CapabilityInject(Researcher.class)
	public static Capability<Researcher> RESEARCHER_CAPABILITY = null;
	
	public static final ResourceLocation KEY = arcLoc("researcher_capability");
	
	public static void init(){
		CapabilityManager.INSTANCE.register(Researcher.class, new Storage(), ResearcherImpl::new);
	}
	
	private static class Storage implements Capability.IStorage<Researcher>{
		
		@Nullable
		public INBT writeNBT(Capability<Researcher> capability, Researcher instance, Direction side){
			return instance.serializeNBT();
		}
		
		public void readNBT(Capability<Researcher> capability, Researcher instance, Direction side, INBT nbt){
			if(nbt instanceof CompoundNBT)
				instance.deserializeNBT((CompoundNBT)nbt);
		}
	}
	
	public static class Provider implements ICapabilitySerializable<CompoundNBT>{
		
		private final Researcher cap = new ResearcherImpl();
		
		public CompoundNBT serializeNBT(){
			return cap.serializeNBT();
		}
		
		public void deserializeNBT(CompoundNBT nbt){
			cap.deserializeNBT(nbt);
		}
		
		@SuppressWarnings("unchecked")
		@Nonnull
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side){
			// if Capability<T> == Capability<Researcher>, then T is Researcher, so this won't cause issues.
			return capability == RESEARCHER_CAPABILITY ? LazyOptional.of(() -> (T)cap) : LazyOptional.empty();
		}
	}
}