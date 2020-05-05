package net.arcanamod.worldgen;

import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.event.WorldTickHandler;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.util.ArrayDeque;
import java.util.Random;

public class NodeGenerator implements IWorldGenerator{
	public static final String RETRO_NAME = "ArcanaNodeGen";
	public static NodeGenerator instance = new NodeGenerator();
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider){
		generateWorld(random, chunkX, chunkZ, world, true);
	}
	
	public void generateWorld(Random random, int chunkX, int chunkZ, World world, boolean newGen){
		if(!newGen && !ArcanaConfig.NODE_RETROGEN)
			return;
		boolean chunkWritten = false;
		
		// TODO: node generation in other dimensions
		if(world.provider.getDimensionType() == DimensionType.OVERWORLD){
			if(random.nextInt(1000) < ArcanaConfig.NODE_CHANCE){
				// pick random x/y
				int x = random.nextInt(16), z = random.nextInt(16);
				// 2-10 blocks above highest block at x/y
				// underground gen needs some work
				int yOffset = random.nextInt(8) + 2;
				
				IBlockState gen = ArcanaBlocks.NORMAL_NODE.getDefaultState();
				
				//if(random.nextInt(100) < ArcanaConfig.SPECIAL_NODE_CHANCE){
				// TODO: generate other node types
				//}
				
				int xSpawn = chunkX * 16 + x;
				int zSpawn = chunkZ * 16 + z;
				world.setBlockState(new BlockPos(xSpawn, world.getHeight(xSpawn, zSpawn) + yOffset, zSpawn), gen);
				chunkWritten = true;
			}
		}
		
		if(!newGen && chunkWritten)
			world.getChunkFromChunkCoords(chunkX, chunkZ).markDirty();
	}
	
	@SubscribeEvent
	public void handleChunkSaveEvent(ChunkDataEvent.Save event){
		NBTTagCompound genTag = event.getData().getCompoundTag(RETRO_NAME);
		if(!genTag.hasKey("generated"))
			genTag.setBoolean("generated", true);
		event.getData().setTag(RETRO_NAME, genTag);
	}
	
	@SubscribeEvent
	public void handleChunkLoadEvent(ChunkDataEvent.Load event){
		int dim = event.getWorld().provider.getDimension();
		
		boolean regen = false;
		NBTTagCompound tag = (NBTTagCompound)event.getData().getTag(RETRO_NAME);
		ChunkPos coord = event.getChunk().getPos();
		
		if(tag != null){
			boolean generated = ArcanaConfig.NODE_RETROGEN && !tag.hasKey("generated");
			if(generated){
				if(ArcanaConfig.VERBOSE)
					Arcana.logger.log(Level.DEBUG, "Queuing Node retrogen for chunk: " + coord.toString() + ".");
				regen = true;
			}
		}else
			regen = ArcanaConfig.NODE_RETROGEN;
		
		if(regen){
			ArrayDeque<ChunkPos> chunks = WorldTickHandler.chunksToGen.get(dim);
			
			if(chunks == null){
				WorldTickHandler.chunksToGen.put(dim, new ArrayDeque<>(128));
				chunks = WorldTickHandler.chunksToGen.get(dim);
			}
			if(chunks != null){
				chunks.addLast(coord);
				WorldTickHandler.chunksToGen.put(dim, chunks);
			}
		}
	}
}