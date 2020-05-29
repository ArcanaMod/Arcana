package net.arcanamod.world.impl;

import net.arcanamod.world.NodeChunk;
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

public class NodeChunkCapability{
	
	@CapabilityInject(NodeChunk.class)
	public static Capability<NodeChunk> NODE_CHUNK_CAPABILITY = null;
	
	public static final ResourceLocation KEY = arcLoc("node_chunk_capability");
	
	public static void init(){
		CapabilityManager.INSTANCE.register(NodeChunk.class, new Storage(), NodeChunkImpl::new);
	}
	
	private static class Storage implements Capability.IStorage<NodeChunk>{
		
		@Nullable
		public INBT writeNBT(Capability<NodeChunk> capability, NodeChunk instance, Direction side){
			return instance.serializeNBT();
		}
		
		public void readNBT(Capability<NodeChunk> capability, NodeChunk instance, Direction side, INBT nbt){
			if(nbt instanceof CompoundNBT)
				instance.deserializeNBT((CompoundNBT)nbt);
		}
	}
	
	public static class Provider implements ICapabilitySerializable<CompoundNBT>{
		
		private final NodeChunk cap = new NodeChunkImpl();
		
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