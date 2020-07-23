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

public class AuraChunkCapability{
	
	@CapabilityInject(AuraChunk.class)
	public static Capability<AuraChunk> NODE_CHUNK_CAPABILITY = null;
	
	public static final ResourceLocation KEY = arcLoc("node_chunk_capability");
	
	public static void init(){
		CapabilityManager.INSTANCE.register(AuraChunk.class, new Storage(), AuraChunkImpl::new);
	}
	
	private static class Storage implements Capability.IStorage<AuraChunk>{
		
		@Nullable
		public INBT writeNBT(Capability<AuraChunk> capability, AuraChunk instance, Direction side){
			return instance.serializeNBT();
		}
		
		public void readNBT(Capability<AuraChunk> capability, AuraChunk instance, Direction side, INBT nbt){
			if(nbt instanceof CompoundNBT)
				instance.deserializeNBT((CompoundNBT)nbt);
		}
	}
	
	public static class Provider implements ICapabilitySerializable<CompoundNBT>{
		
		private final AuraChunk cap = new AuraChunkImpl();
		
		public CompoundNBT serializeNBT(){
			return cap.serializeNBT();
		}
		
		public void deserializeNBT(CompoundNBT nbt){
			cap.deserializeNBT(nbt);
		}
		
		@Nonnull
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side){
			return capability == NODE_CHUNK_CAPABILITY ? LazyOptional.of(() -> (T)cap) : LazyOptional.empty();
		}
	}
}