package net.arcanamod.aspects.handlers;

import net.arcanamod.Arcana;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class AspectHandlerCapability {

	@CapabilityInject(AspectHandler.class)
	public static Capability<AspectHandler> ASPECT_HANDLER = null;

	public static final ResourceLocation KEY = new ResourceLocation(Arcana.MODID, "new_aspect_handler_capability");

	public static void init(){
		CapabilityManager.INSTANCE.register(AspectHandler.class, new Storage(), AspectBattery::new);
	}

	private static class Storage implements Capability.IStorage<AspectHandler>{

		@Nullable
		public INBT writeNBT(Capability<AspectHandler> capability, AspectHandler instance, Direction side){
			return instance.serializeNBT();
		}

		public void readNBT(Capability<AspectHandler> capability, AspectHandler instance, Direction side, INBT nbt){
			instance.deserializeNBT((CompoundNBT)nbt);
		}
	}
}
