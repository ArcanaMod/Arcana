package net.arcanamod.datagen;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import javax.annotation.Nonnull;

import static net.arcanamod.datagen.ArcanaDataGenerators.*;

public class Blockstates extends BlockStateProvider{
	
	ExistingFileHelper efh;
	
	public Blockstates(DataGenerator gen, ExistingFileHelper exFileHelper){
		super(gen, Arcana.MODID, exFileHelper);
		efh = exFileHelper;
	}
	
	protected void registerStatesAndModels(){
		fenceBlock(ArcanaBlocks.DAIR_FENCE.get(), DAIR);
		fenceBlock(ArcanaBlocks.DEAD_FENCE.get(), DEAD);
		fenceBlock(ArcanaBlocks.EUCALYPTUS_FENCE.get(), EUCALYPTUS);
		fenceBlock(ArcanaBlocks.HAWTHORN_FENCE.get(), HAWTHORN);
		fenceBlock(ArcanaBlocks.GREATWOOD_FENCE.get(), GREATWOOD);
		fenceBlock(ArcanaBlocks.SILVERWOOD_FENCE.get(), SILVERWOOD);
		fenceBlock(ArcanaBlocks.TRYPOPHOBIUS_FENCE.get(), TRYPOPHOBIUS);
		fenceBlock(ArcanaBlocks.WILLOW_FENCE.get(), WILLOW);
	}
	
	@Nonnull
	public String getName(){
		return "Arcana Blockstates";
	}
}