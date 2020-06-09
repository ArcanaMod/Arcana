package net.arcanamod.aspects;

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

	@CapabilityInject(IAspectHandler.class)
	public static Capability<IAspectHandler> ASPECT_HANDLER = null;

	public static final ResourceLocation KEY = new ResourceLocation(Arcana.MODID, "new_aspect_handler_capability");

	public static void init(){
		CapabilityManager.INSTANCE.register(IAspectHandler.class, new Storage(), AspectBattery::new);
	}

	private static class Storage implements Capability.IStorage<IAspectHandler>{

		@Nullable
		public INBT writeNBT(Capability<IAspectHandler> capability, IAspectHandler instance, Direction side){
			return instance.serializeNBT();
		}

		public void readNBT(Capability<IAspectHandler> capability, IAspectHandler instance, Direction side, INBT nbt){
			instance.deserializeNBT((CompoundNBT)nbt);
		}
	}
}
