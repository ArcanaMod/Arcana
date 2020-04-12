package net.kineticdevelopment.arcana.client.research;

import net.kineticdevelopment.arcana.client.research.impls.GuessworkSectionRenderer;
import net.kineticdevelopment.arcana.client.research.impls.RecipeSectionRenderer;
import net.kineticdevelopment.arcana.client.research.impls.StringSectionRenderer;
import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.kineticdevelopment.arcana.core.research.impls.GuessworkSection;
import net.kineticdevelopment.arcana.core.research.impls.RecipeSection;
import net.kineticdevelopment.arcana.core.research.impls.StringSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.LinkedHashMap;
import java.util.Map;

public interface EntrySectionRenderer<T extends EntrySection>{
	
	Map<String, EntrySectionRenderer<?>> map = new LinkedHashMap<>();
	
	static void init(){
		map.put(StringSection.TYPE, new StringSectionRenderer());
		map.put(GuessworkSection.TYPE, new GuessworkSectionRenderer());
		map.put(RecipeSection.TYPE, new RecipeSectionRenderer());
	}
	
	void render(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player);
	void renderAfter(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player);
	int span(T section, EntityPlayer player);
	default void onClick(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player){}
	
	static <T extends EntrySection> EntrySectionRenderer<T> get(String type){
		return (EntrySectionRenderer<T>)map.get(type);
	}
	
	static <T extends EntrySection> EntrySectionRenderer<T> get(EntrySection type){
		return (EntrySectionRenderer<T>)map.get(type.getType());
	}
	
	default Minecraft mc(){
		return Minecraft.getMinecraft();
	}
	
	default FontRenderer fr(){
		return mc().fontRenderer;
	}
}