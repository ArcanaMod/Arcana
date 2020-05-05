package net.arcanamod.util.taint;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.*;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the spread of taint
 *
 * @author Atlas
 * @see TaintCleaner
 * @see TaintLevelHandler
 */
public class TaintHandler{
	/**
	 * List of blocks tainted grass spreads to
	 */
	static Block[] TaintedGrassProspects = {Blocks.GRASS, Blocks.MYCELIUM};
	
	/**
	 * List of blocks tainted soil spreads to
	 */
	static Block[] TaintedSoilProspects = {Blocks.DIRT, Blocks.SANDSTONE};
	
	/**
	 * List of blocks tainted rock spreads to
	 */
	static Block[] TaintedRockProspects = {Blocks.COBBLESTONE, Blocks.STONE, Blocks.STONEBRICK, Blocks.MOSSY_COBBLESTONE};
	
	/**
	 * List of blocks tainted crust spreads to
	 */
	static Block[] TaintedCrustProspects = {Blocks.CONCRETE, Blocks.CLAY, Blocks.END_STONE};
	
	/**
	 * Hashmap of single block spreading
	 */
	
	@SuppressWarnings("serial")
	
	static HashMap<Block, Block> singleBlockConversions = new HashMap<Block, Block>(){{
		/*
		 * //Trees
		 *
		 * put(Blocks.OAK_LOG, ArcanaBlocks.tainted_oak_log);
		 *
		 * put(Blocks.OAK_LEAVES, ArcanaBlocks.tainted_oak_leaves);
		 *
		 *
		 * put(Blocks.ACACIA_STAIRS, ArcanaBlocks.tainted_acacia_stairs);
		 *
		 * put(Blocks.SPRUCE_STAIRS, ArcanaBlocks.tainted_spruce_stairs);
		 *
		 *
		 * put(ArcanaBlocks.dair_log, ArcanaBlocks.tainted_dair_log);
		 *
		 * put(ArcanaBlocks.stripped_dair_log, ArcanaBlocks.tainted_stripped_dair_log);
		 *
		 * put(ArcanaBlocks.dair_leaves, ArcanaBlocks.tainted_dair_leaves);
		 *
		 * put(ArcanaBlocks.dair_stairs, ArcanaBlocks.tainted_dair_stairs);
		 *
		 * put(ArcanaBlocks.dair_planks, ArcanaBlocks.tainted_dair_planks);
		 *
		 * put(ArcanaBlocks.dair_slab, ArcanaBlocks.tainted_dair_slab);
		 *
		 *
		 * put(ArcanaBlocks.greatwood_log, ArcanaBlocks.tainted_greatwood_log);
		 *
		 * put(ArcanaBlocks.stripped_greatwood_log,
		 * ArcanaBlocks.stripped_tainted_greatwood_log);
		 *
		 * put(ArcanaBlocks.greatwood_leaves, ArcanaBlocks.tainted_greatwood_leaves);
		 *
		 * put(ArcanaBlocks.greatwood_stairs, ArcanaBlocks.tainted_greatwood_stairs);
		 *
		 * put(ArcanaBlocks.greatwood_planks, ArcanaBlocks.tainted_greatwood_planks);
		 *
		 * put(ArcanaBlocks.greatwood_slab, ArcanaBlocks.tainted_greatwood_slab);
		 *
		 *
		 * put(ArcanaBlocks.willow_log, ArcanaBlocks.tainted_willow_log);
		 *
		 * put(ArcanaBlocks.stripped_willow_log,
		 * ArcanaBlocks.stripped_tainted_willow_log);
		 *
		 * put(ArcanaBlocks.willow_leaves, ArcanaBlocks.tainted_willow_leaves);
		 *
		 * put(ArcanaBlocks.willow_stairs, ArcanaBlocks.tainted_willow_stairs);
		 *
		 * put(ArcanaBlocks.willow_planks, ArcanaBlocks.tainted_willow_planks);
		 *
		 * put(ArcanaBlocks.willow_slab, ArcanaBlocks.tainted_willow_slab);
		 *
		 *
		 * //Misc put(Blocks.GRASS_PATH, ArcanaBlocks.tainted_path);
		 *
		 * put(Blocks.FARMLAND, ArcanaBlocks.tainted_farmland);
		 *
		 * put(Blocks.SAND, ArcanaBlocks.tainted_sand);
		 *
		 * put(Blocks.RED_SAND, ArcanaBlocks.tainted_sand);
		 *
		 * put(Blocks.GRAVEL, ArcanaBlocks.tainted_gravel);
		 *
		 *
		 *
		 * //Ores
		 *
		 * put(Blocks.COAL_ORE, ArcanaBlocks.tainted_coal_ore);
		 *
		 * put(Blocks.DIAMOND_ORE, ArcanaBlocks.tainted_diamond_ore);
		 *
		 * put(Blocks.IRON_ORE, ArcanaBlocks.tainted_iron_ore);
		 *
		 * put(Blocks.GOLD_ORE, ArcanaBlocks.tainted_gold_ore);
		 *
		 * put(Blocks.REDSTONE_ORE, ArcanaBlocks.tainted_redstone_ore);
		 *
		 * put(Blocks.LAPIS_ORE, ArcanaBlocks.tainted_lapis_lazuli_ore);
		 *
		 * put(Blocks.EMERALD_ORE, ArcanaBlocks.tainted_emerald_ore);
		 *
		 * //put(ArcanaBlocks.cinnabar_ore, ArcanaBlocks.tainted_cinnabar_ore);
		 *
		 *
		 *
		 * //Mat. Blocks
		 *
		 * put(Blocks.COBBLESTONE_SLAB, ArcanaBlocks.tainted_rock_slab);
		 *
		 * put(Blocks.REDSTONE_BLOCK, ArcanaBlocks.tainted_redstone_block);
		 *
		 * put(ArcanaBlocks.thaumium_block, ArcanaBlocks.tainted_thaumium_block);
		 *
		 * put(Blocks.LAPIS_BLOCK, ArcanaBlocks.tainted_lapis_block);
		 *
		 * put(Blocks.IRON_BLOCK, ArcanaBlocks.tainted_iron_block);
		 *
		 * put(Blocks.GOLD_BLOCK, ArcanaBlocks.tainted_gold_block);
		 *
		 * put(Blocks.EMERALD_BLOCK, ArcanaBlocks.tainted_emerald_block);
		 *
		 * put(Blocks.DIAMOND_BLOCK, ArcanaBlocks.tainted_diamond_block);
		 *
		 * put(Blocks.COAL_BLOCK, ArcanaBlocks.tainted_coal_block);
		 *
		 * put(ArcanaBlocks.arcanium_block, ArcanaBlocks.tainted_arcanium_block);
		 *
		 *
		 *
		 *
		 *
		 * put(ArcanaBlocks.tainted_amber_ore, Blocks.GRASS);
		 *
		 *
		 *
		 * //To be replaced with tainted wood pile
		 *
		 * put(Blocks.OAK_DOOR, Blocks.AIR);
		 *
		 * put(Blocks.OAK_TRAPDOOR, Blocks.AIR);
		 */
		
	}};
	
	/**
	 * Spreads taint in a full cube around it
	 *
	 * @param worldIn
	 * @param pos
	 */
	@SuppressWarnings("serial")
	public static void spreadTaint(World worldIn, BlockPos pos){
		
		if(TaintLevelHandler.getTaintLevel(worldIn) >= 5){
			
			// Map of <OriginalBlocks -> TaintedBlock>
			
			// For <OriginalBlock -> TaintedBlock> see singleBlockConversions above
			
			HashMap<Block[], Block> conversionLists = new HashMap<Block[], Block>(){{
				put(TaintedSoilProspects, ArcanaBlocks.TAINTED_SOIL);
				put(TaintedRockProspects, ArcanaBlocks.TAINTED_ROCK);
				put(TaintedCrustProspects, ArcanaBlocks.TAINTED_CRUST);
				put(TaintedGrassProspects, ArcanaBlocks.TAINTED_GRASS);
			}};
			
			int int1 = -1;
			
			int int2 = 2;
			
			for(int x = int1; x < int2; x++){
				for(int y = int1; y < int2; y++){
					for(int z = int1; z < int2; z++){
						BlockPos nPos = pos.add(x, y, z);
						
						Block b = worldIn.getBlockState(nPos).getBlock();
						
						boolean changed = false;
						
						for(Map.Entry<Block[], Block> entry : conversionLists.entrySet()){
							if(Arrays.stream(entry.getKey()).anyMatch(bl -> b.equals(bl.getBlockState().getBlock()))){
								if(worldIn.getBlockState(nPos).getPropertyKeys().contains(RotatedPillarBlock.AXIS)){
									worldIn.setBlockState(nPos, entry.getValue().getDefaultState().withProperty(RotatedPillarBlock.AXIS, worldIn.getBlockState(nPos).getValue(RotatedPillarBlock.AXIS)));
								}else{
									worldIn.setBlockState(nPos, entry.getValue().getDefaultState());
								}
								
								changed = true;
								
								break;
								
							}
							
						}
						
						if(changed){
							
							continue;
							
						}
						
						for(Map.Entry<Block, Block> entry : singleBlockConversions.entrySet()){
							
							if(b.equals(entry.getKey().getBlockState().getBlock())){
								
								if(worldIn.getBlockState(nPos).getPropertyKeys().contains(RotatedPillarBlock.AXIS)){
									
									worldIn.setBlockState(nPos, entry.getValue().getDefaultState().withProperty(RotatedPillarBlock.AXIS, worldIn.getBlockState(nPos).getValue(RotatedPillarBlock.AXIS)));
								}else if(worldIn.getBlockState(nPos).getProperties().containsKey(StairsBlock.SHAPE)){
									worldIn.setBlockState(nPos, entry.getValue().getDefaultState().withProperty(StairsBlock.SHAPE, worldIn.getBlockState(nPos).getValue(StairsBlock.SHAPE)).withProperty(StairsBlock.FACING, worldIn.getBlockState(nPos).getValue(StairsBlock.FACING)).withProperty(StairsBlock.HALF, worldIn.getBlockState(nPos).getValue(StairsBlock.HALF)));
									
								}else if(worldIn.getBlockState(nPos).getProperties().containsKey(SlabBlock.HALF)){
									
									worldIn.setBlockState(nPos, entry.getValue().getDefaultState().withProperty(SlabBlock.HALF, worldIn.getBlockState(nPos).getValue(SlabBlock.HALF)));
								}else{
									worldIn.setBlockState(nPos, entry.getValue().getDefaultState());
								}
								
								break;
								
							}
						}
					}
				}
			}
		}
	}
}
