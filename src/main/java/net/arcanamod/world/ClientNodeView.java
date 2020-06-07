package net.arcanamod.world;

import net.arcanamod.client.ClientNodeHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A view of the nodes in the world for a particular tick.
 */
public class ClientNodeView implements INodeView{
	
	ClientWorld world;
	
	public ClientNodeView(ClientWorld world){
		this.world = world;
	}
	
	public Collection<Node> getAllNodes(){
		Collection<Node> allNodes = new ArrayList<>();
		for(ChunkPos chunkPos : ClientNodeHandler.clientLoadedChunks){
			IChunk chunk = world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, false);
			if(chunk instanceof Chunk){ // also a nonnull check
				NodeChunk nc = NodeChunk.getFrom((Chunk)chunk);
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