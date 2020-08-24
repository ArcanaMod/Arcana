package net.arcanamod.network;

import net.arcanamod.Arcana;
import net.arcanamod.world.Node;
import net.arcanamod.capabilities.AuraChunk;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class PkSyncNodeAura{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	ChunkPos chunk;
	Collection<Node> nodes;
	
	public PkSyncNodeAura(ChunkPos chunk, Collection<Node> nodes){
		this.chunk = chunk;
		this.nodes = nodes;
	}
	
	public static void encode(PkSyncNodeAura msg, PacketBuffer buffer){
		CompoundNBT compound = new CompoundNBT();
		ListNBT data = new ListNBT();
		for(Node node : msg.nodes)
			data.add(node.serializeNBT());
		compound.put("nodes", data);
		buffer.writeCompoundTag(compound);
		buffer.writeInt(msg.chunk.x);
		buffer.writeInt(msg.chunk.z);
	}
	
	public static PkSyncNodeAura decode(PacketBuffer buffer){
		ListNBT list = buffer.readCompoundTag().getList("nodes", Constants.NBT.TAG_COMPOUND);
		Collection<Node> nodeSet = new ArrayList<>(list.size());
		for(INBT nodeNBT : list)
			if(nodeNBT instanceof CompoundNBT)
				nodeSet.add(Node.fromNBT((CompoundNBT)nodeNBT));
		int x = buffer.readInt();
		int z = buffer.readInt();
		return new PkSyncNodeAura(new ChunkPos(x, z), nodeSet);
	}
	
	public static void handle(PkSyncNodeAura msg, Supplier<NetworkEvent.Context> supplier){
		supplier.get().enqueueWork(() -> {
			// I'm on client
			Chunk chunk = Arcana.proxy.getWorldOnClient().getChunk(msg.chunk.x, msg.chunk.z);
			AuraChunk nc = AuraChunk.getFrom(chunk);
			if(nc != null)
				nc.setNodes(new ArrayList<>(msg.nodes));
		});
		supplier.get().setPacketHandled(true);
	}
}