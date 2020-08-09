package net.arcanamod.datagen;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.util.annotations.AnnotationUtil;
import net.arcanamod.util.annotations.GIM;
import net.arcanamod.util.annotations.GLT;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.fml.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ItemModels extends ItemModelProvider{

	private static final Logger LOGGER = LogManager.getLogger();

	public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper){
		super(generator, Arcana.MODID, existingFileHelper);
	}
	
	protected void registerModels(){
		ArcanaDataGenerators.WOODS.forEach((name, texture) -> {
			withExistingParent(name + "_fence", arcBlockLoc(name + "_fence_inventory"));
			withExistingParent(name + "_fence_gate", arcBlockLoc(name + "_fence_gate"));
		});
		
		ArcanaDataGenerators.STONES.forEach((name, texture) -> {
			withExistingParent(name, arcBlockLoc(name));
			withExistingParent(name + "_slab", arcBlockLoc(name + "_slab"));
			withExistingParent(name + "_stairs", arcBlockLoc(name + "_stairs"));
			withExistingParent(name + "_pressure_plate", arcBlockLoc(name + "_pressure_plate"));
			withExistingParent(name + "_wall", arcBlockLoc(name + "_wall_inventory"));
		});
		
		Aspects.getWithoutEmpty().forEach(aspect -> {
			withExistingParent("aspect_" + aspect.name().toLowerCase(), "item/generated")
					.texture("layer0", new ResourceLocation(Arcana.MODID, "aspect/" + aspect.name().toLowerCase()));
			//withExistingParent("phial_" + aspect.name().toLowerCase(), "item/generated")
			//		.texture("layer0", new ResourceLocation(Arcana.MODID, "item/phial_" + aspect.name().toLowerCase()));
			withExistingParent(aspect.name().toLowerCase() + "_crystal", "item/generated")
					.texture("layer0", new ResourceLocation(Arcana.MODID, "item/crystals/" + aspect.name().toLowerCase() + "_crystal"));
		});


		/*withExistingParent("rough_limestone",arcBlockLoc("rough_limestone"));
		withExistingParent("smooth_limestone",arcBlockLoc("smooth_limestone"));
		withExistingParent("pridestone_bricks",arcBlockLoc("pridestone_bricks"));
		withExistingParent("pridestone_small_bricks",arcBlockLoc("pridestone_small_bricks"));
		withExistingParent("wet_pridestone",arcBlockLoc("wet_pridestone"));
		withExistingParent("wet_smooth_pridestone",arcBlockLoc("wet_smooth_pridestone"));
		withExistingParent("pridestone",arcBlockLoc("pridestone"));
		withExistingParent("smooth_pridestone",arcBlockLoc("smooth_pridestone"));
		withExistingParent("prideclay",arcBlockLoc("prideclay"));
		withExistingParent("gilded_prideclay",arcBlockLoc("gilded_prideclay"));
		withExistingParent("carved_prideful_gold_block",arcBlockLoc("carved_prideful_gold_block"));
		withExistingParent("chiseled_prideful_gold_block",arcBlockLoc("chiseled_prideful_gold_block"));
		withExistingParent("prideful_gold_block",arcBlockLoc("prideful_gold_block"));
		withExistingParent("prideful_gold_tile",arcBlockLoc("prideful_gold_tile"));*/
		withExistingParent("silver_block", arcBlockLoc("silver_block"));
		withExistingParent("silver_ore", arcBlockLoc("silver_ore"));
		withExistingParent("void_metal_block", arcBlockLoc("void_metal_block"));
		withExistingParent("tainted_granite", arcBlockLoc("tainted_granite"));
		withExistingParent("tainted_diorite", arcBlockLoc("tainted_diorite"));
		withExistingParent("tainted_andesite", arcBlockLoc("tainted_andesite"));
		
		/*generated("silver_ingot");
		generated("void_metal_nugget");*/

		try {
			fromAnnotations();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void fromAnnotations() throws IllegalAccessException {
		Class<?>[] classes = AnnotationUtil.targetedClasses;
		for (Class clazz : classes) {
			Field[] fields = clazz.getFields();
			for (Field field : fields) {
				// if field has GIM annotation
				if (field.isAnnotationPresent(GIM.class)) {
					LOGGER.debug("Found field in "+clazz.getName()+".class: name:" + field.getName() + " type:" + field.getType());
					if (field.get(field.getType()) instanceof RegistryObject) {
						// get RegistryObject from field
						RegistryObject<Block> reg = (RegistryObject<Block>) field.get(field.getType());
						LOGGER.debug("RegistryObject: " + reg.getId().toString());
						String path = reg.getId().getPath();
						// if annotation type is ITEM generate item model, if BLOCK_REF is generated withExistingParent
						GIM annotation = field.getAnnotation(GIM.class);
						if (annotation.type().equals(GIM.Type.BLOCK_REF)){
							withExistingParent(path,arcBlockLoc(path));
						} else if (annotation.type().equals(GIM.Type.ITEM)){
							// if source is not empty generate item model with other texture
							if (!annotation.source().equals("")){
								generated(path,annotation.source());
							} else generated(path);
						}
					}
				}
			}
		}
	}

	public ResourceLocation arcBlockLoc(String loc){
		return arcLoc("block/" + loc);
	}
	
	public ResourceLocation arcItemLoc(String loc){
		return arcLoc("item/" + loc);
	}
	
	public ResourceLocation arcLoc(String loc){
		return new ResourceLocation(Arcana.MODID, loc);
	}
	
	public void generated(String name, String textureName){
		withExistingParent(name, mcLoc("item/generated"))
			.texture("layer0", arcItemLoc(textureName));
	}
	
	public void generated(String nameAndTexture){
		generated(nameAndTexture, nameAndTexture);
	}
	
	@Nonnull
	public String getName(){
		return "Arcana Item Models";
	}
}