package net.kineticdevelopment.arcana.common.worldgen;

import net.kineticdevelopment.arcana.common.config.OregenConfig;
import net.kineticdevelopment.arcana.common.handlers.WorldTickHandler;
import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.util.ArrayDeque;
import java.util.Random;

/**
 * OreGenerator - Generates all arcana ores
 * @see WorldTickHandler - Handles retrogenning
 * @author Mozaran
 */
public class OreGenerator implements IWorldGenerator {
    public static final String RETRO_NAME = "ArcanaOreGen";
    public static OreGenerator instance = new OreGenerator();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generateWorld(random, chunkX, chunkZ, world, true);
    }

    public void generateWorld(Random random, int chunkX, int chunkZ, World world, boolean newGen) {
        if(!newGen && !OregenConfig.RETROGEN) return;

        boolean chunkWritten = false;
        if(OregenConfig.GENERATE_OVERWORLD) {
            if (world.provider.getDimensionType() == DimensionType.OVERWORLD) {
                // Add Amber Ore
                addOreSpawn(BlockInit.AMBER_ORE, (byte) 0, Blocks.STONE, world, random, chunkX * 16,
                        chunkZ * 16, OregenConfig.AMBER_MIN_VEIN_SIZE, OregenConfig.AMBER_MAX_VEIN_SIZE,
                        OregenConfig.AMBER_CHANCES_TO_SPAWN, OregenConfig.AMBER_MIN_Y, OregenConfig.AMBER_MAX_Y);
                chunkWritten = true;
            }
        }

        if(!newGen && chunkWritten) {
            // Forces chunk to save at next save point
            world.getChunkFromChunkCoords(chunkX, chunkZ).markDirty();
        }
    }

    public void addOreSpawn(Block block, byte blockMeta, Block targetBlock, World world, Random random, int blockXPos,
                            int blockZPos, int minVeinSize, int maxVeinSize, int chancesToSpawn, int minY, int maxY) {
        WorldGenMinable minable = new WorldGenMinable(block.getStateFromMeta(blockMeta), (minVeinSize +
                random.nextInt(maxVeinSize - minVeinSize + 1)), BlockMatcher.forBlock(targetBlock));
        for (int i = 0 ; i < chancesToSpawn ; i++) {
            int posX = blockXPos + random.nextInt(16);
            int posY = minY + random.nextInt(maxY - minY);
            int posZ = blockZPos + random.nextInt(16);
            minable.generate(world, random, new BlockPos(posX, posY, posZ));
        }
    }

    @SubscribeEvent
    public void handleChunkSaveEvent(ChunkDataEvent.Save event) {
        NBTTagCompound genTag = event.getData().getCompoundTag(RETRO_NAME);
        if (!genTag.hasKey("generated")) {
            // If we did not have this key then this is a new chunk and we will have proper ores generated.
            // Otherwise we are saving a chunk for which ores are not yet generated.
            genTag.setBoolean("generated", true);
        }
        event.getData().setTag(RETRO_NAME, genTag);
    }

    @SubscribeEvent
    public void handleChunkLoadEvent(ChunkDataEvent.Load event) {
        int dim = event.getWorld().provider.getDimension();

        boolean regen = false;
        NBTTagCompound tag = (NBTTagCompound) event.getData().getTag(RETRO_NAME);
        ChunkPos coord = event.getChunk().getPos();

        if (tag != null) {
            boolean generated = OregenConfig.RETROGEN && !tag.hasKey("generated");
            if (generated) {
                if (OregenConfig.VERBOSE) {
                    Main.logger.log(Level.DEBUG, "Queuing Retrogen for chunk: " + coord.toString() + ".");
                }
                regen = true;
            }
        } else {
            regen = OregenConfig.RETROGEN;
        }

        if (regen) {
            ArrayDeque<ChunkPos> chunks = WorldTickHandler.chunksToGen.get(dim);

            if (chunks == null) {
                WorldTickHandler.chunksToGen.put(dim, new ArrayDeque<>(128));
                chunks = WorldTickHandler.chunksToGen.get(dim);
            }
            if (chunks != null) {
                chunks.addLast(coord);
                WorldTickHandler.chunksToGen.put(dim, chunks);
            }
        }
    }
}
