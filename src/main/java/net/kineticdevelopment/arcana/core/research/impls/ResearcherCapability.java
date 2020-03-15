package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.research.Researcher;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class ResearcherCapability{
	
	@CapabilityInject(Researcher.class)
	static Capability<Researcher> RESEARCHER_CAPABILITY = null;
	
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
}