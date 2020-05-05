package net.arcanamod.research.impls;

import net.arcanamod.research.EntrySection;
import net.minecraft.nbt.CompoundNBT;

/**
 * An entry section that displays text over any number of pages.
 */
public class StringSection extends EntrySection{
	
	public static final String TYPE = "StringSection";
	
	String content;
	
	public StringSection(String content){
		this.content = content;
	}
	
	public String getType(){
		return TYPE;
	}
	
	public CompoundNBT getData(){
		CompoundNBT tag = new CompoundNBT();
		tag.setString("text", getText());
		return tag;
	}
	
	public String getText(){
		return content;
	}
}