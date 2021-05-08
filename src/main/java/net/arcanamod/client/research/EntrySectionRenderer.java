package net.arcanamod.client.research;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.client.research.impls.*;
import net.arcanamod.systems.research.EntrySection;
import net.arcanamod.systems.research.impls.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public interface EntrySectionRenderer<T extends EntrySection>{
	
	Map<String, EntrySectionRenderer<?>> map = new HashMap<>();
	
	static void init(){
		map.put(StringSection.TYPE, new StringSectionRenderer());
		map.put(CraftingSection.TYPE, new CraftingSectionRenderer());
		map.put(SmeltingSection.TYPE, new SmeltingSectionRenderer());
		map.put(AlchemySection.TYPE, new AlchemySectionRenderer());
		map.put(ArcaneCraftingSection.TYPE, new ArcaneCraftingSectionRenderer());
		map.put(ImageSection.TYPE, new ImageSectionRenderer());
		map.put(AspectCombosSection.TYPE, new AspectCombosSectionRenderer());
	}
	
	void render(MatrixStack stack, T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player);
	
	void renderAfter(MatrixStack stack, T section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player);
	
	int span(T section, PlayerEntity player);
	
	/**
	 * Called when the mouse is clicked anywhere on the screen while this section is visible.
	 *  @param section
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
	 */
	default boolean onClick(T section, int pageIndex, int screenWidth, int screenHeight, double mouseX, double mouseY, boolean right, PlayerEntity player){
		return false;
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