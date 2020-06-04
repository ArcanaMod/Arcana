package net.arcanamod.network;

import net.arcanamod.Arcana;
import net.arcanamod.world.ClientNodeView;
import net.arcanamod.world.Node;
import net.arcanamod.world.NodeChunk;
import net.minecraft.client.world.ClientWorld;
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

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class PkSyncChunkNodes{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	ChunkPos chunk;
	Set<Node> nodes;
	
	public PkSyncChunkNodes(ChunkPos chunk, Set<Node> nodes){
		this.chunk = chunk;
		this.nodes = nodes;
	}
	
	public static void encode(PkSyncChunkNodes msg, PacketBuffer buffer){
		CompoundNBT compound = new CompoundNBT();
		ListNBT data = new ListNBT();
		for(Node node : msg.nodes)
			data.add(node.serializeNBT());
		compound.put("nodes", data);
		buffer.writeCompoundTag(compound);
		buffer.writeInt(msg.chunk.x);
		buffer.writeInt(msg.chunk.z);
	}
	
	public static PkSyncChunkNodes decode(PacketBuffer buffer){
		ListNBT list = buffer.readCompoundTag().getList("nodes", Constants.NBT.TAG_COMPOUND);
		Set<Node> nodeSet = new HashSet<>(list.size());
		for(INBT nodeNBT : list)
			if(nodeNBT instanceof CompoundNBT)
				nodeSet.add(Node.fromNBT((CompoundNBT)nodeNBT));
		int x = buffer.readInt();
		int z = buffer.readInt();
		return new PkSyncChunkNodes(new ChunkPos(x, z), nodeSet);
	}
	
	public static void handle(PkSyncChunkNodes msg, Supplier<NetworkEvent.Context> supplier){
		supplier.get().enqueueWork(() -> {
			// I'm on client
			Chunk chunk = Arcana.proxy.getWorldOnClient().getChunk(msg.chunk.x, msg.chunk.z);
			NodeChunk nc = NodeChunk.getFrom(chunk);
			if(nc != null){
				nc.setNodes(new HashSet<>(msg.nodes));
				if(new ClientNodeView((ClientWorld)Arcana.proxy.getWorldOnClient()).getAllNodes().size() > 0)
					System.out.println(">0 nodes exist on client");
			}
		});
		supplier.get().setPacketHandled(true);
	}
}