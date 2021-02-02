package net.arcanamod.worldgen;

import com.mojang.datafixers.Dynamic;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.CrystalClusterBlock;
import net.arcanamod.capabilities.AuraChunk;
import net.arcanamod.event.WorldTickHandler;
import net.arcanamod.world.Node;
import net.arcanamod.world.NodeType;
import net.minecraft.block.Block;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static net.arcanamod.world.NodeType.DEFAULT;
import static net.arcanamod.world.NodeType.SPECIAL_TYPES;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NodeFeature extends Feature<NoFeatureConfig>{
	
	private static final Map<Aspect, Supplier<Block>> CRYSTAL_CLUSTERS_FROM_ASPECTS = new HashMap<>();
	static{
		CRYSTAL_CLUSTERS_FROM_ASPECTS.put(Aspects.AIR, ArcanaBlocks.AIR_CLUSTER);
		CRYSTAL_CLUSTERS_FROM_ASPECTS.put(Aspects.EARTH, ArcanaBlocks.EARTH_CLUSTER);
		CRYSTAL_CLUSTERS_FROM_ASPECTS.put(Aspects.FIRE, ArcanaBlocks.FIRE_CLUSTER);
		CRYSTAL_CLUSTERS_FROM_ASPECTS.put(Aspects.WATER, ArcanaBlocks.WATER_CLUSTER);
		CRYSTAL_CLUSTERS_FROM_ASPECTS.put(Aspects.ORDER, ArcanaBlocks.ORDER_CLUSTER);
		CRYSTAL_CLUSTERS_FROM_ASPECTS.put(Aspects.CHAOS, ArcanaBlocks.CHAOS_CLUSTER);
		
	}
	
	public NodeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory){
		super(configFactory);
	}
	
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config){
		//requireNonNull(NodeChunk.getFrom((Chunk)world.getChunk(pos))).addNode(new Node(NORMAL.genNodeAspects(pos, world, rand), NORMAL, pos.getX(), pos.getY(), pos.getZ()));
		// its a chunkprimer, not a chunk, with no capability data attached
		// add it on the next tick.
		BlockPos newPos = pos.up(5 + rand.nextInt(2));
		NodeType type = rand.nextInt(100) < ArcanaConfig.SPECIAL_NODE_CHANCE.get() ? new ArrayList<>(SPECIAL_TYPES).get(rand.nextInt(SPECIAL_TYPES.size())) : DEFAULT;
		if(rand.nextInt(100) < ArcanaConfig.NODE_CHANCE.get()){
			WorldTickHandler.onTick.add(newWorld -> {
				IAspectHandler aspects = type.genBattery(newPos, newWorld, rand);
				requireNonNull(AuraChunk.getFrom((Chunk)newWorld.getChunk(newPos))).addNode(new Node(aspects, type, newPos.getX(), newPos.getY(), newPos.getZ(), 0));
				// Add some crystal clusters around here too
				int successes = 0;
				BlockPos.Mutable pointer = new BlockPos.Mutable(pos);
				for(int i = 0; i < 40 && successes < (rand.nextInt(5) + 6); i++){
					// Pick a random block from the ground
					pointer.move(rand.nextInt(7) - 3, rand.nextInt(5) - 2, rand.nextInt(7) - 3);
					if(newWorld.getBlockState(pointer).isAir() || newWorld.getBlockState(pointer).getMaterial().isReplaceable()){
						// If it has at least one open side,
						for(Direction value : Direction.values())
							if(newWorld.getBlockState(pointer.offset(value)).isSolid()){
								// Place a crystal,
								newWorld.setBlockState(pointer, CRYSTAL_CLUSTERS_FROM_ASPECTS.get(aspects.getHolder(rand.nextInt(aspects.getHoldersAmount())).getContainedAspect()).get().getDefaultState().with(CrystalClusterBlock.FACING, value.getOpposite()).with(CrystalClusterBlock.AGE, 3).with(CrystalClusterBlock.WATERLOGGED, newWorld.getBlockState(pointer).getFluidState().isTagged(FluidTags.WATER)));
								// Increment successes
								successes++;
							}
					}
				}
			});
		}
		return true;
	}
}