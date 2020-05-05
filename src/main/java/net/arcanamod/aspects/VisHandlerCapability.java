package net.arcanamod.aspects;

import net.arcanamod.Arcana;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class VisHandlerCapability{
	
	@CapabilityInject(VisHandler.class)
	public static Capability<VisHandler> ASPECT_HANDLER = null;
	
	public static final ResourceLocation KEY = new ResourceLocation(Arcana.MODID, "aspect_handler_capability");
	
	public static void init(){
		CapabilityManager.INSTANCE.register(VisHandler.class, new Storage(), VisBattery::new);
	}
	
	private static class Storage implements Capability.IStorage<VisHandler>{
		
		@Nullable
		public NBTBase writeNBT(Capability<VisHandler> capability, VisHandler instance, EnumFacing side){
			return instance.serializeNBT();
		}
		
		public void readNBT(Capability<VisHandler> capability, VisHandler instance, EnumFacing side, NBTBase nbt){
			instance.deserializeNBT((NBTTagCompound)nbt);
		}
	}
}