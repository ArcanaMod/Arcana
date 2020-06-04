package net.arcanamod.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerWorld;

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
	
	public Set<Node> getAllNodes(){
		Set<Node> allNodes = new HashSet<>();
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
	
	public World getWorld(){
		return world;
	}
}