package net.arcanamod.world;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * A view of the nodes in the world for a particular tick.
 */
public class NodeView{
	
	ServerWorld world;
	
	private static final Method getLoadedChunksIterable;
	private static final boolean shouldTickNodes;
	private static final Logger LOGGER = LogManager.getLogger();
	
	static{
		Method temp;
		boolean shouldTick = true;
		try{
			temp = ChunkManager.class.getDeclaredMethod("getLoadedChunksIterable");
			LOGGER.info("Successfully reflected into ChunkManager; required for node ticking.");
		}catch(NoSuchMethodException e){
			e.printStackTrace();
			LOGGER.error("Failed to reflect into ChunkManager to get all loaded chunks; can't tick nodes!");
			temp = null;
			shouldTick = false;
		}
		getLoadedChunksIterable = temp;
		shouldTickNodes = shouldTick;
	}
	
	public NodeView(ServerWorld world){
		this.world = world;
	}
	
	public Set<Node> getAllNodes(){
		Set<Node> allNodes = new HashSet<>();
		if(shouldTickNodes){
			try{
				getLoadedChunksIterable.setAccessible(true);
				for(ChunkHolder holder : ((Iterable<ChunkHolder>)getLoadedChunksIterable.invoke(world.getChunkProvider().chunkManager))){
					Chunk chunk = holder.getChunkIfComplete();
					if(chunk != null){
						NodeChunk nc = NodeChunk.getFrom(chunk);
						if(nc != null)
							allNodes.addAll(nc.getNodes());
					}
				}
				getLoadedChunksIterable.setAccessible(false);
			}catch(IllegalAccessException | InvocationTargetException e){
				e.printStackTrace();
			}
		}
		return allNodes;
	}
}