package net.arcanamod.world;

import net.arcanamod.network.Connection;
import net.arcanamod.network.PkSyncChunkNodes;
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
import java.util.HashSet;
import java.util.Set;

/**
 * A view of the nodes in the world for a particular tick.
 */
public class ServerNodeView implements INodeView{
	
	ServerWorld world;
	
	public ServerNodeView(ServerWorld world){
		this.world = world;
	}
	
	public Collection<Node> getAllNodes(){
		Collection<Node> allNodes = new ArrayList<>();
		for(ChunkHolder holder : world.getChunkProvider().chunkManager.getLoadedChunksIterable()){
			Chunk chunk = holder.getChunkIfComplete();
			if(chunk != null){
				NodeChunk nc = NodeChunk.getFrom(chunk);
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
		Connection.INSTANCE.send(PacketDistributor.ALL.noArg(), new PkSyncChunkNodes(pos, getNodesWithinChunk(pos)));
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