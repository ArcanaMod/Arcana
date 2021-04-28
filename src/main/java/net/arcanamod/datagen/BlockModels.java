package net.arcanamod.datagen;

import net.arcanamod.Arcana;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

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

		cubeAll("rough_limestone", new ResourceLocation(Arcana.MODID, "block/rough_limestone"));
		cubeAll("smooth_limestone", new ResourceLocation(Arcana.MODID, "block/smooth_limestone"));
		cubeAll("pridestone_bricks", new ResourceLocation(Arcana.MODID, "block/pridestone_bricks"));
		cubeAll("pridestone_small_bricks", new ResourceLocation(Arcana.MODID, "block/pridestone_small_bricks"));
		cubeAll("wet_pridestone", new ResourceLocation(Arcana.MODID, "block/wet_pridestone"));
		cubeAll("wet_smooth_pridestone", new ResourceLocation(Arcana.MODID, "block/wet_smooth_pridestone"));
		cubeAll("pridestone", new ResourceLocation(Arcana.MODID, "block/pridestone"));
		cubeAll("smooth_pridestone", new ResourceLocation(Arcana.MODID, "block/smooth_pridestone"));
		cubeAll("prideclay", new ResourceLocation(Arcana.MODID, "block/prideclay"));
		cubeAll("gilded_prideclay", new ResourceLocation(Arcana.MODID, "block/gilded_prideclay"));
		cubeAll("chiseled_prideful_gold_block", new ResourceLocation(Arcana.MODID, "block/chiseled_prideful_gold_block"));
		cubeAll("carved_prideful_gold_block", new ResourceLocation(Arcana.MODID, "block/carved_prideful_gold_block"));
		cubeAll("prideful_gold_block", new ResourceLocation(Arcana.MODID, "block/prideful_gold_block"));
		cubeAll("prideful_gold_tile", new ResourceLocation(Arcana.MODID, "block/prideful_gold_tile"));
		cubeAll("tainted_granite",new ResourceLocation(Arcana.MODID, "block/tainted_granite"));
		cubeAll("tainted_diorite",new ResourceLocation(Arcana.MODID, "block/tainted_diorite"));
		cubeAll("tainted_andesite",new ResourceLocation(Arcana.MODID, "block/tainted_andesite"));

		cubeAll("silver_block", new ResourceLocation(Arcana.MODID, "block/silver_block"));
		cubeAll("silver_ore", new ResourceLocation(Arcana.MODID, "block/silver_ore"));
		cubeAll("void_metal_block", new ResourceLocation(Arcana.MODID, "block/void_metal_block"));
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