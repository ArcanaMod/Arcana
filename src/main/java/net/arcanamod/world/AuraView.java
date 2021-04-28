package net.arcanamod.world;

import net.arcanamod.capabilities.AuraChunk;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface AuraView{
	
	Function<World, AuraView> SIDED_FACTORY = (world) -> world instanceof ClientWorld ? new ClientAuraView((ClientWorld)world) :  world instanceof ServerWorld ? new ServerAuraView((ServerWorld)world) : null;
	double HALF_NODE = .7;
	
	Collection<Node> getAllNodes();
	
	World getWorld();
	
	static AuraView getSided(World world){
		return SIDED_FACTORY.apply(world);
	}
	
	// no-op on client
	default void tickTaintLevel(){}
	
	default AuraChunk getAuraChunk(ChunkPos pos){
		IChunk chunk = getWorld().getChunk(pos.x, pos.z, ChunkStatus.FULL, false);
		if(chunk instanceof Chunk)
			return AuraChunk.getFrom((Chunk)chunk);
		return null;
	}
	
	default AuraChunk getAuraChunk(BlockPos pos){
		return getAuraChunk(new ChunkPos(pos));
	}
	
	default Collection<Node> getNodesWithinChunk(ChunkPos pos){
		AuraChunk nc = getAuraChunk(pos);
		return nc != null ? nc.getNodes() : Collections.emptyList();
	}
	
	default float getFluxWithinChunk(ChunkPos pos){
		AuraChunk nc = getAuraChunk(pos);
		return nc != null ? nc.getFluxLevel() : -1;
	}
	
	default float getFluxAt(BlockPos pos){
		return getFluxWithinChunk(new ChunkPos(pos));
	}
	
	/**
	 * Adds taint to a particular chunk. Returns the previous taint level, or -1 if the chunk isn't loaded.
	 *
	 * @param pos
	 * 		The chunk to add taint to.
	 * @return The previous taint level.
	 */
	default float addFluxToChunk(ChunkPos pos, float amount){
		AuraChunk nc = getAuraChunk(pos);
		if(nc != null){
			float level = nc.getFluxLevel();
			nc.addFlux(amount);
			return level;
		}else{
			return -1;
		}
	}
	
	default float addFluxAt(BlockPos pos, float amount){
		return addFluxToChunk(new ChunkPos(pos), amount);
	}
	
	/**
	 * Sets the taint level of a particular chunk. Returns the previous taint level, or -1 if the chunk isn't loaded.
	 *
	 * @param pos
	 * 		The chunk to set the taint of.
	 * @return The previous taint level.
	 */
	default float setFluxOfChunk(ChunkPos pos, float amount){
		AuraChunk nc = getAuraChunk(pos);
		if(nc != null){
			float level = nc.getFluxLevel();
			nc.setFlux(amount);
			return level;
		}else{
			return -1;
		}
	}
	
	default float setFluxAt(BlockPos pos, float amount){
		return setFluxOfChunk(new ChunkPos(pos), amount);
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
		//then getNodesWithinAABB for each
		List<Node> list = new ArrayList<>();
		for(ChunkPos pos : relevant)
			if(getAuraChunk(pos) != null)
				list.addAll(getAuraChunk(pos).getNodesWithinAABB(bounds));
		return list;
	}
	
	default Collection<Node> getNodesOfType(NodeType type){
		return getAllNodes().stream()
				.filter(node -> node.type() == type)
				.collect(Collectors.toList());
	}
	
	default Optional<Node> getNodeByUuid(UUID id){
		return getAllNodes().stream()
				.filter(node -> node.nodeUniqueId.equals(id))
				.findFirst();
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
				.map(this::getAuraChunk)
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
				.map(this::getAuraChunk)
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
				.map(this::getAuraChunk)
				.map(chunk -> chunk.getNodesWithinAABB(bounds))
				.flatMap(Collection::stream)
				.filter(node -> node.type() == type)
				.filter(node -> node != excluded)
				.collect(Collectors.toList());
	}
	
	default boolean addNode(Node node){
		// get the relevant chunk
		AuraChunk nc = getAuraChunk(new ChunkPos(new BlockPos(node)));
		if(nc != null){
			nc.addNode(node);
			return true;
		}
		return false;
	}
	
	default boolean removeNode(Node node){
		// get the relevant chunk
		AuraChunk nc = getAuraChunk(new ChunkPos(new BlockPos(node)));
		if(nc != null && nc.getNodes().contains(node)){
			nc.removeNode(node);
			return true;
		}
		return false;
	}
	
	default Optional<Node> raycast(Vector3d from, double length, boolean ignoreBlocks, Entity entity){
		Vector3d to = from.add(entity.getLookVec().mul(length, length, length));
		BlockRayTraceResult result = null;
		if(!ignoreBlocks)
			result = getWorld().rayTraceBlocks(new RayTraceContext(from, to, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, entity));
		AxisAlignedBB box = new AxisAlignedBB(from, to);
		Node ret = null;
		double curDist = length;
		for(Node node : getNodesWithinAABB(box)){
			AxisAlignedBB nodeBox = new AxisAlignedBB(node.x - HALF_NODE, node.y - HALF_NODE, node.z - HALF_NODE, node.x + HALF_NODE, node.y + HALF_NODE, node.z + HALF_NODE);
			Optional<Vector3d> optional = nodeBox.rayTrace(from, to);
			if(optional.isPresent()){
				double dist = from.squareDistanceTo(optional.get());
				if(dist < curDist){
					ret = node;
					curDist = dist;
				}
			}
		}
		if(!ignoreBlocks)
			// Blocked by a block
			if(result.getHitVec().squareDistanceTo(from) < curDist)
				return Optional.empty();
		return Optional.ofNullable(ret);
	}
	
	default Optional<Node> raycast(Vector3d from, double length, Entity entity){
		return raycast(from, length, false, entity);
	}
}