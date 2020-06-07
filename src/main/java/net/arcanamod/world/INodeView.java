package net.arcanamod.world;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.DistExecutor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface INodeView{
	
	Function<World, INodeView> SIDED_FACTORY = DistExecutor.runForDist(() -> () -> (world) -> world instanceof ClientWorld ? new ClientNodeView((ClientWorld)world) : null, () -> () -> (world) -> world instanceof ServerWorld ? new ServerNodeView((ServerWorld)world) : null);
	
	Collection<Node> getAllNodes();
	
	World getWorld();
	
	default Collection<Node> getNodesWithinChunk(ChunkPos pos){
		NodeChunk nc = getNodeChunk(pos);
		return nc != null ? nc.getNodes() : Collections.emptyList();
	}
	
	default NodeChunk getNodeChunk(ChunkPos pos){
		IChunk chunk = getWorld().getChunk(pos.x, pos.z, ChunkStatus.FULL, false);
		if(chunk instanceof Chunk)
			return NodeChunk.getFrom((Chunk)chunk);
		return null;
	}
	
	default Collection<Node> getNodesWithinAABB(AxisAlignedBB bounds){
		// get all related chunks
		// that is, all chunks between minX and maxX, minZ and maxZ
		ChunkPos min = new ChunkPos(new BlockPos(bounds.minX, 0, bounds.minZ));
		ChunkPos max = new ChunkPos(new BlockPos(bounds.maxX, 0, bounds.maxZ));
		List<ChunkPos> relevant = new ArrayList<>();
		if(!min.equals(max))
			for(int xx = min.x; xx <= max.x; xx++)
				for(int zz = min.z; zz <= max.z; zz++)
					relevant.add(new ChunkPos(xx, zz));
		else
			relevant.add(min);
		//then getNodesWithinAABB foreach
		return relevant.stream()
				.map(this::getNodeChunk)
				.map(chunk -> chunk.getNodesWithinAABB(bounds))
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}
	
	default Collection<Node> getNodesOfType(NodeType type){
		return getAllNodes().stream()
				.filter(node -> node.type() == type)
				.collect(Collectors.toList());
	}
	
	default Collection<Node> getNodesOfTypeWithinAABB(AxisAlignedBB bounds, NodeType type){
		// get all related chunks
		// that is, all chunks between minX and maxX, minZ and maxZ
		ChunkPos min = new ChunkPos(new BlockPos(bounds.minX, 0, bounds.minZ));
		ChunkPos max = new ChunkPos(new BlockPos(bounds.maxX, 0, bounds.maxZ));
		List<ChunkPos> relevant = new ArrayList<>();
		if(!min.equals(max))
			for(int xx = min.x; xx <= max.x; xx++)
				for(int zz = min.z; zz <= max.z; zz++)
					relevant.add(new ChunkPos(xx, zz));
		else
			relevant.add(min);
		//then getNodesWithinAABB foreach
		return relevant.stream()
				.map(this::getNodeChunk)
				.map(chunk -> chunk.getNodesWithinAABB(bounds))
				.flatMap(Collection::stream)
				.filter(node -> node.type() == type)
				.collect(Collectors.toList());
	}
	
	default Collection<Node> getNodesExcluding(Node excluded){
		return getAllNodes().stream()
				.filter(node -> node != excluded)
				.collect(Collectors.toList());
	}
	
	default Collection<Node> getNodesOfTypeExcluding(NodeType type, Node excluded){
		return getAllNodes().stream()
				.filter(node -> node.type() == type)
				.filter(node -> node != excluded)
				.collect(Collectors.toList());
	}
	
	default Collection<Node> getNodesWithinAABBExcluding(AxisAlignedBB bounds, Node excluded){
		// get all related chunks
		// that is, all chunks between minX and maxX, minZ and maxZ
		ChunkPos min = new ChunkPos(new BlockPos(bounds.minX, 0, bounds.minZ));
		ChunkPos max = new ChunkPos(new BlockPos(bounds.maxX, 0, bounds.maxZ));
		List<ChunkPos> relevant = new ArrayList<>();
		if(!min.equals(max))
			for(int xx = min.x; xx <= max.x; xx++)
				for(int zz = min.z; zz <= max.z; zz++)
					relevant.add(new ChunkPos(xx, zz));
		else
			relevant.add(min);
		//then getNodesWithinAABB foreach
		return relevant.stream()
				.map(this::getNodeChunk)
				.map(chunk -> chunk.getNodesWithinAABB(bounds))
				.flatMap(Collection::stream)
				.filter(node -> node != excluded)
				.collect(Collectors.toList());
	}
	
	default Collection<Node> getNodesOfTypeWithinAABBExcluding(AxisAlignedBB bounds, NodeType type, Node excluded){
		// get all related chunks
		// that is, all chunks between minX and maxX, minZ and maxZ
		ChunkPos min = new ChunkPos(new BlockPos(bounds.minX, 0, bounds.minZ));
		ChunkPos max = new ChunkPos(new BlockPos(bounds.maxX, 0, bounds.maxZ));
		List<ChunkPos> relevant = new ArrayList<>();
		if(!min.equals(max))
			for(int xx = min.x; xx <= max.x; xx++)
				for(int zz = min.z; zz <= max.z; zz++)
					relevant.add(new ChunkPos(xx, zz));
		else
			relevant.add(min);
		//then getNodesWithinAABB foreach
		return relevant.stream()
				.map(this::getNodeChunk)
				.map(chunk -> chunk.getNodesWithinAABB(bounds))
				.flatMap(Collection::stream)
				.filter(node -> node.type() == type)
				.filter(node -> node != excluded)
				.collect(Collectors.toList());
	}
	
	default boolean addNode(Node node){
		// get the relevant chunk
		NodeChunk nc = getNodeChunk(new ChunkPos(new BlockPos(node)));
		if(nc != null){
			nc.addNode(node);
			return true;
		}
		return false;
	}
	
	default boolean removeNode(Node node){
		// get the relevant chunk
		NodeChunk nc = getNodeChunk(new ChunkPos(new BlockPos(node)));
		if(nc != null && nc.getNodes().contains(node)){
			nc.removeNode(node);
			return true;
		}
		return false;
	}
}