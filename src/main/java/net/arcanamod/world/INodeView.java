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
	
	Set<Node> getAllNodes();
	
	World getWorld();
	
	default Set<Node> getNodesWithinChunk(ChunkPos pos){
		NodeChunk nc = getNodeChunk(pos);
		return nc != null ? nc.getNodes() : Collections.emptySet();
	}
	
	default NodeChunk getNodeChunk(ChunkPos pos){
		IChunk chunk = getWorld().getChunk(pos.x, pos.z, ChunkStatus.FULL, false);
		if(chunk instanceof Chunk)
			return NodeChunk.getFrom((Chunk)chunk);
		return null;
	}
	
	default Set<Node> getNodesWithinAABB(AxisAlignedBB bounds){
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
				.collect(Collectors.toSet());
	}
	
	default Set<Node> getNodesOfType(NodeType type){
		return getAllNodes().stream().filter(node -> node.type() == type).collect(Collectors.toSet());
	}
	
	default Set<Node> getNodesOfTypeWithinAABB(AxisAlignedBB bounds, NodeType type){
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
				.collect(Collectors.toSet());
	}
	
	default Set<Node> getNodesExcluding(Node excluded){
		return getAllNodes().stream()
				.filter(node -> node != excluded)
				.collect(Collectors.toSet());
	}
	
	default Set<Node> getNodesOfTypeExcluding(NodeType type, Node excluded){
		return getAllNodes().stream()
				.filter(node -> node.type() == type)
				.filter(node -> node != excluded)
				.collect(Collectors.toSet());
	}
	
	default Set<Node> getNodesWithinAABBExcluding(AxisAlignedBB bounds, Node excluded){
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
				.collect(Collectors.toSet());
	}
	
	default Set<Node> getNodesOfTypeWithinAABBExcluding(AxisAlignedBB bounds, NodeType type, Node excluded){
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
				.collect(Collectors.toSet());
	}
}