package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.minecraft.nbt.NBTTagCompound;

public class StringSection extends EntrySection{
	
	public static final String TYPE = "StringSection";
	
	String content;
	
	public StringSection(String content){
		this.content = content;
	}
	
	public String getType(){
		return TYPE;
	}
	
	public NBTTagCompound getData(){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("text", getText());
		return tag;
	}
	
	public String getText(){
		return content;
	}
}