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

import java.util.HashMap;
import java.util.Map;

public interface EntrySectionRenderer<T extends EntrySection>{
	
	Map<String, EntrySectionRenderer<?>> map = new HashMap<>();
	
	static void init(){
		map.put(StringSection.TYPE, new StringSectionRenderer());
		map.put(GuessworkSection.TYPE, new GuessworkSectionRenderer());
		map.put(RecipeSection.TYPE, new RecipeSectionRenderer());
	}
	
	void render(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player);
	void renderAfter(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player);
	int span(T section, EntityPlayer player);
	
	/**
	 * Called when the mouse is clicked anywhere on the screen while this section is visible.
	 *
	 * @param section The section that is visible.
	 * @param pageIndex The index within the section that is visible.
	 * @param screenWidth The width of the screen.
	 * @param screenHeight The height of the screen.
	 * @param mouseX The x location of the mouse.
	 * @param mouseY The y location of the mouse.
	 * @param right Whether the section that is visible is on the left or gith.
	 * @param player The player that clicked.
	 */
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