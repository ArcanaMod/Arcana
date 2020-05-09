package net.arcanamod.datagen;

import net.arcanamod.Arcana;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;

import javax.annotation.Nonnull;

public class ItemModels extends ItemModelProvider{
	
	public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper){
		super(generator, Arcana.MODID, existingFileHelper);
	}
	
	protected void registerModels(){
		ArcanaDataGenerators.WOODS.forEach((name, texture) ->
				withExistingParent(name + "_fence", new ResourceLocation(Arcana.MODID, "block/" + name + "_fence_inventory")));
	}
	
	@Nonnull
	public String getName(){
		return "Arcana Item Models";
	}
}