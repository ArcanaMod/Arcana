package net.arcanamod.client.research;

import net.arcanamod.client.research.impls.GuessworkSectionRenderer;
import net.arcanamod.client.research.impls.RecipeSectionRenderer;
import net.arcanamod.client.research.impls.StringSectionRenderer;
import net.arcanamod.research.EntrySection;
import net.arcanamod.research.impls.GuessworkSection;
import net.arcanamod.research.impls.RecipeSection;
import net.arcanamod.research.impls.StringSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public interface EntrySectionRenderer<T extends EntrySection>{
	
	Map<String, EntrySectionRenderer<?>> map = new HashMap<>();
	
	static void init(){
		map.put(StringSection.TYPE, new StringSectionRenderer());
		map.put(GuessworkSection.TYPE, new GuessworkSectionRenderer());
		map.put(RecipeSection.TYPE, new RecipeSectionRenderer());
	}
	
	void render(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player);
	
	void renderAfter(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player);
	
	int span(T section, PlayerEntity player);
	
	/**
	 * Called when the mouse is clicked anywhere on the screen while this section is visible.
	 *
	 * @param section
	 * 		The section that is visible.
	 * @param pageIndex
	 * 		The index within the section that is visible.
	 * @param screenWidth
	 * 		The width of the screen.
	 * @param screenHeight
	 * 		The height of the screen.
	 * @param mouseX
	 * 		The x location of the mouse.
	 * @param mouseY
	 * 		The y location of the mouse.
	 * @param right
	 * 		Whether the section that is visible is on the left or right.
	 * @param player
	 * 		The player that clicked.
	 */
	default void onClick(T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
	}
	
	@SuppressWarnings("unchecked")
	static <T extends EntrySection> EntrySectionRenderer<T> get(String type){
		return (EntrySectionRenderer<T>)map.get(type);
	}
	
	@SuppressWarnings("unchecked")
	static <T extends EntrySection> EntrySectionRenderer<T> get(EntrySection type){
		return (EntrySectionRenderer<T>)map.get(type.getType());
	}
	
	default Minecraft mc(){
		return Minecraft.getInstance();
	}
	
	default FontRenderer fr(){
		return mc().fontRenderer;
	}
}