package net.arcanamod.datagen;

import net.arcanamod.Arcana;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import javax.annotation.Nonnull;

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
		
		ArcanaDataGenerators.STONES.forEach((name, texture) -> {
			cubeAll(name, texture);
			slab(name + "_slab", texture, texture, texture);
			slabTop(name + "_slab_top", texture, texture, texture);
			stairs(name + "_stairs", texture, texture, texture);
			stairsInner(name + "_stairs_inner", texture, texture, texture);
			stairsOuter(name + "_stairs_outer", texture, texture, texture);
			pressurePlate(name, texture);
			wallInventory(name + "_wall_inventory", texture);
			wallPost(name + "_wall_post", texture);
			wallSide(name + "_wall_side", texture);
		});
	}
	
	public void pressurePlate(String name, ResourceLocation texture){
		pressurePlateUp(name, texture);
		pressurePlateDown(name, texture);
	}
	
	public void pressurePlateUp(String name, ResourceLocation texture){
		withExistingParent(name + "_pressure_plate", "pressure_plate_up")
			.texture("texture", texture);
	}
	
	public void pressurePlateDown(String name, ResourceLocation texture){
		withExistingParent(name + "_pressure_plate_down", "pressure_plate_down")
				.texture("texture", texture);
	}
	
	@Nonnull
	public String getName(){
		return "Arcana Block Models";
	}
}