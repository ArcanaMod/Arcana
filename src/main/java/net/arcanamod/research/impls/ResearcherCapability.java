package net.arcanamod.research.impls;

import net.arcanamod.Arcana;
import net.arcanamod.research.Researcher;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResearcherCapability{
	
	@CapabilityInject(Researcher.class)
	public static Capability<Researcher> RESEARCHER_CAPABILITY = null;
	
	public static final ResourceLocation KEY = new ResourceLocation(Arcana.MODID, "researcher_capability");
	
	public static void init(){
		CapabilityManager.INSTANCE.register(Researcher.class, new Storage(), ResearcherImpl::new);
	}
	
	private static class Storage implements Capability.IStorage<Researcher>{
		
		@Nullable
		public NBTBase writeNBT(Capability<Researcher> capability, Researcher instance, EnumFacing side){
			return instance.serialize();
		}
		
		public void readNBT(Capability<Researcher> capability, Researcher instance, EnumFacing side, NBTBase nbt){
			if(nbt instanceof NBTTagCompound)
				instance.deserialize((NBTTagCompound)nbt);
		}
	}
	
	public static class Provider implements ICapabilitySerializable<NBTTagCompound>{
		
		private final Researcher cap = new ResearcherImpl();
		
		public NBTTagCompound serializeNBT(){
			return (NBTTagCompound)cap.serialize();
		}
		
		public void deserializeNBT(NBTTagCompound nbt){
			cap.deserialize(nbt);
		}
		
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing){
			return capability == RESEARCHER_CAPABILITY;
		}
		
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side){
			// if Capability<T> == Capability<Researcher>, then T is Researcher, so this won't cause issues.
			return capability == RESEARCHER_CAPABILITY ? (T)cap : null;
		}
	}
}