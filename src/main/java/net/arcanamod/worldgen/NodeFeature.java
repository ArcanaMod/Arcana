package net.arcanamod.worldgen;

import com.mojang.datafixers.Dynamic;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.world.Node;
import net.arcanamod.world.NodeChunk;
import net.arcanamod.world.NodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static net.arcanamod.world.NodeType.NORMAL;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NodeFeature extends Feature<NoFeatureConfig>{
	
	public NodeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory){
		super(configFactory);
	}
	
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config){
		//requireNonNull(NodeChunk.getFrom((Chunk)world.getChunk(pos))).addNode(new Node(NORMAL.genNodeAspects(pos, world, rand), NORMAL, pos.getX(), pos.getY(), pos.getZ()));
		// its a chunkprimer, not a chunk, with no capability data attached
		pos = pos.up(5);
		// I could add it to a list, but what then?
		
		return true;
	}
}