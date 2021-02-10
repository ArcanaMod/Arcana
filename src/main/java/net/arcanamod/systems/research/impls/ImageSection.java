package net.arcanamod.systems.research.impls;

import net.arcanamod.systems.research.EntrySection;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class ImageSection extends EntrySection{
	
	public static final String TYPE = "image";
	
	ResourceLocation image;
	
	public ImageSection(String image){
		this(new ResourceLocation(image));
	}
	
	public ImageSection(ResourceLocation image){
		this.image = image;
	}
	
	public String getType(){
		return TYPE;
	}
	
	public ResourceLocation getImage(){
		return image;
	}
	
	public CompoundNBT getData(){
		CompoundNBT tag = new CompoundNBT();
		tag.putString("image", getImage().toString());
		return tag;
	}
}