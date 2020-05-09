package net.arcanamod.datagen;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

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
	}
	
	@Nonnull
	public String getName(){
		return "Arcana Blockstates";
	}
}