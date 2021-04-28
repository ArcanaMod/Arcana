package net.arcanamod.datagen;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

import static net.arcanamod.blocks.ArcanaBlocks.*;
import static net.arcanamod.datagen.ArcanaDataGenerators.*;

public class Blockstates extends BlockStateProvider{

	ExistingFileHelper efh;
	
	public Blockstates(DataGenerator gen, ExistingFileHelper exFileHelper){
		super(gen, Arcana.MODID, exFileHelper);
		efh = exFileHelper;
	}
	
	protected void registerStatesAndModels(){
		fenceBlock(DAIR_FENCE.get(), DAIR);
		fenceBlock(DEAD_FENCE.get(), DEAD);
		fenceBlock(EUCALYPTUS_FENCE.get(), EUCALYPTUS);
		fenceBlock(HAWTHORN_FENCE.get(), HAWTHORN);
		fenceBlock(GREATWOOD_FENCE.get(), GREATWOOD);
		fenceBlock(SILVERWOOD_FENCE.get(), SILVERWOOD);
		fenceBlock(TRYPOPHOBIUS_FENCE.get(), TRYPOPHOBIUS);
		fenceBlock(WILLOW_FENCE.get(), WILLOW);
		
		fenceGateBlock(DAIR_FENCE_GATE.get(), DAIR);
		fenceGateBlock(DEAD_FENCE_GATE.get(), DEAD);
		fenceGateBlock(EUCALYPTUS_FENCE_GATE.get(), EUCALYPTUS);
		fenceGateBlock(HAWTHORN_FENCE_GATE.get(), HAWTHORN);
		fenceGateBlock(GREATWOOD_FENCE_GATE.get(), GREATWOOD);
		fenceGateBlock(SILVERWOOD_FENCE_GATE.get(), SILVERWOOD);
		fenceGateBlock(TRYPOPHOBIUS_FENCE_GATE.get(), TRYPOPHOBIUS);
		fenceGateBlock(WILLOW_FENCE_GATE.get(), WILLOW);
		
		simpleBlock(ArcanaBlocks.ARCANE_STONE.get());
		simpleBlock(ArcanaBlocks.ARCANE_STONE_BRICKS.get());
		simpleBlock(ArcanaBlocks.DUNGEON_BRICKS.get());
		simpleBlock(ArcanaBlocks.CRACKED_DUNGEON_BRICKS.get());
		simpleBlock(ArcanaBlocks.MOSSY_DUNGEON_BRICKS.get());
		

		simpleBlock(ArcanaBlocks.TAINTED_GRANITE.get());
		simpleBlock(ArcanaBlocks.TAINTED_DIORITE.get());
		simpleBlock(ArcanaBlocks.TAINTED_ANDESITE.get());
		
		slabBlock(ARCANE_STONE_SLAB.get(), ArcanaDataGenerators.ARCANE_STONE, ArcanaDataGenerators.ARCANE_STONE);
		slabBlock(ARCANE_STONE_BRICKS_SLAB.get(), ArcanaDataGenerators.ARCANE_STONE_BRICKS, ArcanaDataGenerators.ARCANE_STONE_BRICKS);
		slabBlock(DUNGEON_BRICKS_SLAB.get(), ArcanaDataGenerators.DUNGEON_BRICKS, ArcanaDataGenerators.DUNGEON_BRICKS);
		slabBlock(CRACKED_DUNGEON_BRICKS_SLAB.get(), ArcanaDataGenerators.CRACKED_DUNGEON_BRICKS, ArcanaDataGenerators.CRACKED_DUNGEON_BRICKS);
		slabBlock(MOSSY_DUNGEON_BRICKS_SLAB.get(), ArcanaDataGenerators.MOSSY_DUNGEON_BRICKS, ArcanaDataGenerators.MOSSY_DUNGEON_BRICKS);
		
		stairsBlock(ARCANE_STONE_STAIRS.get(), ArcanaDataGenerators.ARCANE_STONE);
		stairsBlock(ARCANE_STONE_BRICKS_STAIRS.get(), ArcanaDataGenerators.ARCANE_STONE_BRICKS);
		stairsBlock(DUNGEON_BRICKS_STAIRS.get(), ArcanaDataGenerators.DUNGEON_BRICKS);
		stairsBlock(CRACKED_DUNGEON_BRICKS_STAIRS.get(), ArcanaDataGenerators.CRACKED_DUNGEON_BRICKS);
		stairsBlock(MOSSY_DUNGEON_BRICKS_STAIRS.get(), ArcanaDataGenerators.MOSSY_DUNGEON_BRICKS);
		
		// pressure plate blockstates are done manually
		
		wallBlock(ARCANE_STONE_WALL.get(), ArcanaDataGenerators.ARCANE_STONE);
		wallBlock(ARCANE_STONE_BRICKS_WALL.get(), ArcanaDataGenerators.ARCANE_STONE_BRICKS);
		wallBlock(DUNGEON_BRICKS_WALL.get(), ArcanaDataGenerators.DUNGEON_BRICKS);
		wallBlock(CRACKED_DUNGEON_BRICKS_WALL.get(), ArcanaDataGenerators.CRACKED_DUNGEON_BRICKS);
		wallBlock(MOSSY_DUNGEON_BRICKS_WALL.get(), ArcanaDataGenerators.MOSSY_DUNGEON_BRICKS);
		
		simpleBlock(SILVER_BLOCK.get());
		simpleBlock(SILVER_ORE.get());
		simpleBlock(VOID_METAL_BLOCK.get());
		//simpleBlock(CINNABAR_ORE.get());
	}
	
	@Nonnull
	public String getName(){
		return "Arcana Blockstates";
	}
}