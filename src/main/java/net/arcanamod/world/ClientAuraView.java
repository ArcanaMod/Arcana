package net.arcanamod.world;

import net.arcanamod.capabilities.AuraChunk;
import net.arcanamod.client.ClientAuraHandler;
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
public class ClientAuraView implements AuraView{
	
	ClientWorld world;
	
	public ClientAuraView(ClientWorld world){
		this.world = world;
	}
	
	public Collection<Node> getAllNodes(){
		Collection<Node> allNodes = new ArrayList<>();
		for(ChunkPos chunkPos : ClientAuraHandler.CLIENT_LOADED_CHUNKS){
			IChunk chunk = world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, false);
			if(chunk instanceof Chunk){ // also a nonnull check
				AuraChunk nc = AuraChunk.getFrom((Chunk)chunk);
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