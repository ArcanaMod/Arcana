package net.arcanamod.worldgen;

import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.event.WorldTickHandler;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.util.ArrayDeque;
import java.util.Random;

/**
 * OreGenerator - Generates all arcana ores
 *
 * @author Mozaran
 * @see WorldTickHandler - Handles retrogenning
 */
public class OreGenerator {//implements IWorldGenerator{
	public static final String RETRO_NAME = "ArcanaOreGen";
	public static OreGenerator instance = new OreGenerator();
	
	//@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, ChunkGenerator chunkGenerator, AbstractChunkProvider chunkProvider){
		generateWorld(random, chunkX, chunkZ, world, true);
	}
	
	public void generateWorld(Random random, int chunkX, int chunkZ, World world, boolean newGen){
		if(!newGen && !ArcanaConfig.ORE_RETROGEN)
			return;
		
		boolean chunkWritten = false;
		if(ArcanaConfig.GENERATE_OVERWORLD){
			if(world.getDimension().isSurfaceWorld()){
				// Add Amber Ore
//				addOreSpawn(ArcanaBlocks.AMBER_ORE, (byte)0, Blocks.STONE, world, random, chunkX * 16, chunkZ * 16, ArcanaConfig.AMBER_MIN_VEIN_SIZE, ArcanaConfig.AMBER_MAX_VEIN_SIZE, ArcanaConfig.AMBER_CHANCES_TO_SPAWN, ArcanaConfig.AMBER_MIN_Y, ArcanaConfig.AMBER_MAX_Y);
				chunkWritten = true;
			}
		}
		
		if(!newGen && chunkWritten){
			// Forces chunk to save at next save point
			//world.getChunkFromChunkCoords(chunkX, chunkZ).markDirty();
		}
	}
	
	public void addOreSpawn(Block block, byte blockMeta, Block targetBlock, World world, Random random, int blockXPos, int blockZPos, int minVeinSize, int maxVeinSize, int chancesToSpawn, int minY, int maxY){
		/*OreFeature minable = new OreFeature(block.getStateFromMeta(blockMeta), (minVeinSize + random.nextInt(maxVeinSize - minVeinSize + 1)), BlockMatcher.forBlock(targetBlock));
		for(int i = 0; i < chancesToSpawn; i++){
			int posX = blockXPos + random.nextInt(16);
			int posY = minY + random.nextInt(maxY - minY);
			int posZ = blockZPos + random.nextInt(16);
			minable.generate(world, random, new BlockPos(posX, posY, posZ));
		}*/
	}
	
	@SubscribeEvent
	public void handleChunkSaveEvent(ChunkDataEvent.Save event){
		/*CompoundNBT genTag = event.getData().getCompoundTag(RETRO_NAME);
		if(!genTag.hasKey("generated")){
			// If we did not have this key then this is a new chunk and we will have proper ores generated.
			// Otherwise we are saving a chunk for which ores are not yet generated.
			genTag.setBoolean("generated", true);
		}
		event.getData().setTag(RETRO_NAME, genTag);*/
	}
	
	@SubscribeEvent
	public void handleChunkLoadEvent(ChunkDataEvent.Load event){
		/*int dim = event.getWorld().provider.getDimension();
		
		boolean regen = false;
		CompoundNBT tag = (CompoundNBT)event.getData().getTag(RETRO_NAME);
		ChunkPos coord = event.getChunk().getPos();
		
		if(tag != null){
			boolean generated = ArcanaConfig.ORE_RETROGEN && !tag.hasKey("generated");
			if(generated){
				if(ArcanaConfig.VERBOSE){
					Arcana.logger.log(Level.DEBUG, "Queuing ore retrogen for chunk: " + coord.toString() + ".");
				}
				regen = true;
			}
		}else{
			regen = ArcanaConfig.ORE_RETROGEN;
		}
		
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
		}*/
	}
}
