package net.arcanamod.world;

import net.arcanamod.network.Connection;
import net.arcanamod.network.PkSyncChunkAura;
import net.minecraft.dispenser.IPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A view of the nodes in the world for a particular tick.
 */
public class ServerAuraView implements AuraView{
	
	ServerWorld world;
	
	public ServerAuraView(ServerWorld world){
		this.world = world;
	}
	
	public Collection<Node> getAllNodes(){
		Collection<Node> allNodes = new ArrayList<>();
		for(ChunkHolder holder : world.getChunkProvider().chunkManager.getLoadedChunksIterable()){
			Chunk chunk = holder.getChunkIfComplete();
			if(chunk != null){
				AuraChunk nc = AuraChunk.getFrom(chunk);
				if(nc != null)
					allNodes.addAll(nc.getNodes());
			}
		}
		return allNodes;
	}
	
	public void sendChunkToClients(IPosition pos){
		sendChunkToClients(new BlockPos(pos));
	}
	
	public void sendChunkToClients(BlockPos pos){
		sendChunkToClients(new ChunkPos(pos));
	}
	
	public void sendChunkToClients(ChunkPos pos){
		Connection.INSTANCE.send(PacketDistributor.ALL.noArg(), new PkSyncChunkAura(pos, getNodesWithinChunk(pos), getTaintWithinChunk(pos)));
	}
	
	public void sendAllChunksToClients(Collection<? extends IPosition> pos){
		// don't send sync packets for the same chunk
		pos.stream()
				.map(BlockPos::new)
				.map(ChunkPos::new)
				.distinct()
				.forEach(this::sendChunkToClients);
	}
	
	public World getWorld(){
		return world;
	}
}