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

public class VisHandlerCapability{

	@CapabilityInject(VisHandler.class)
	public static Capability<VisHandler> ASPECT_HANDLER = null;

	public static final ResourceLocation KEY = new ResourceLocation(Arcana.MODID, "aspect_handler_capability");

	public static void init(){
		CapabilityManager.INSTANCE.register(VisHandler.class, new Storage(), VisBattery::new);
	}

	private static class Storage implements Capability.IStorage<VisHandler>{

		@Nullable
		public INBT writeNBT(Capability<VisHandler> capability, VisHandler instance, Direction side){
			return instance.serializeNBT();
		}

		public void readNBT(Capability<VisHandler> capability, VisHandler instance, Direction side, INBT nbt){
			instance.deserializeNBT((CompoundNBT)nbt);
		}
	}
}