package net.kineticdevelopment.arcana.client.research;

import net.kineticdevelopment.arcana.client.research.impls.GuessworkSectionRenderer;
import net.kineticdevelopment.arcana.client.research.impls.RecipeSectionRenderer;
import net.kineticdevelopment.arcana.client.research.impls.StringSectionRenderer;
import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.kineticdevelopment.arcana.core.research.impls.GuessworkSection;
import net.kineticdevelopment.arcana.core.research.impls.RecipeSection;
import net.kineticdevelopment.arcana.core.research.impls.StringSection;
import net.kineticdevelopment.arcana.utilities.StreamUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.kineticdevelopment.arcana.utilities.StreamUtils.cached;

public interface EntrySectionRenderer<T extends EntrySection>{
	
	Map<String, Supplier<EntrySectionRenderer<?>>> map = new LinkedHashMap<>();
	
	static void init(){
		map.put(StringSection.TYPE, cached(StringSectionRenderer::new));
		map.put(GuessworkSection.TYPE, cached(GuessworkSectionRenderer::new));
		map.put(RecipeSection.TYPE, cached(RecipeSectionRenderer::new));
	}
	
	void render(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player);
	void renderAfter(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player);
	int span(T section, EntityPlayer player);
	default void onClick(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player){}
	
	static <T extends EntrySection> EntrySectionRenderer<T> get(String type){
		return (EntrySectionRenderer<T>)map.get(type).get();
	}
	
	static <T extends EntrySection> EntrySectionRenderer<T> get(EntrySection type){
		return (EntrySectionRenderer<T>)map.get(type.getType()).get();
	}
	
	default Minecraft mc(){
		return Minecraft.getMinecraft();
	}
	
	default FontRenderer fr(){
		return mc().fontRenderer;
	}
}