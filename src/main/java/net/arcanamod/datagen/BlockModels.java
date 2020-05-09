package net.arcanamod.datagen;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import javax.annotation.Nonnull;

import static net.arcanamod.datagen.ArcanaDataGenerators.DAIR;

public class BlockModels extends BlockModelProvider{
	
	public BlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper){
		super(generator, Arcana.MODID, existingFileHelper);
	}
	
	protected void registerModels(){
		ArcanaDataGenerators.WOODS.forEach((name, texture) -> {
			fenceInventory(name + "_fence_inventory", texture);
			fencePost(name + "_fence", texture);
			fenceSide(name + "_fence", texture);
			
			fenceGate(name + "_fence_gate", texture);
			fenceGateOpen(name + "_fence_gate", texture);
			fenceGateWall(name + "_fence_gate", texture);
			fenceGateWallOpen(name + "_fence_gate", texture);
		});
	}
	
	@Nonnull
	public String getName(){
		return "Arcana Block Models";
	}
}