package net.arcanamod.network;

import net.arcanamod.capabilities.AuraChunk;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PkRequestNodeSync{
	
	ChunkPos chunk;
	
	public PkRequestNodeSync(ChunkPos chunk){
		this.chunk = chunk;
	}
	
	public static void encode(PkRequestNodeSync msg, PacketBuffer buffer){
		buffer.writeInt(msg.chunk.x);
		buffer.writeInt(msg.chunk.z);
	}
	
	public static PkRequestNodeSync decode(PacketBuffer buffer){
		int x = buffer.readInt();
		int z = buffer.readInt();
		return new PkRequestNodeSync(new ChunkPos(x, z));
	}
	
	public static void handle(PkRequestNodeSync msg, Supplier<NetworkEvent.Context> supplier){
		supplier.get().enqueueWork(() -> {
			// I'm on server
			// Get nodes at chunk
			Chunk chunk = (Chunk)supplier.get().getSender().world.getChunk(msg.chunk.x, msg.chunk.z, ChunkStatus.FULL, false);
			if(chunk != null){
				AuraChunk nc = AuraChunk.getFrom(chunk);
				// Send a PkSyncChunkNodes
				if(nc != null)
					Connection.sendTo(new PkSyncChunkNodes(msg.chunk, nc.getNodes()), supplier.get().getSender());
			}
		});
		supplier.get().setPacketHandled(true);
	}
}