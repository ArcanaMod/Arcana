package net.kineticdevelopment.arcana.core.aspects;

import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class AspectHandlerCapability{
	
	@CapabilityInject(AspectHandler.class)
	public static Capability<AspectHandler> ASPECT_HANDLER = null;
	
	public static final ResourceLocation KEY = new ResourceLocation(Main.MODID, "aspect_handler_capability");
	
	public static void init(){
		CapabilityManager.INSTANCE.register(AspectHandler.class, new Storage(), VisBattery::new);
	}
	
	private static class Storage implements Capability.IStorage<AspectHandler>{
		
		@Nullable
		public NBTBase writeNBT(Capability<AspectHandler> capability, AspectHandler instance, EnumFacing side){
			return instance.serializeNBT();
		}
		
		public void readNBT(Capability<AspectHandler> capability, AspectHandler instance, EnumFacing side, NBTBase nbt){
			instance.deserializeNBT((NBTTagCompound)nbt);
		}
	}
}