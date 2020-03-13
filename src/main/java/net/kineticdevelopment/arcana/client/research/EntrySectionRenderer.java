package net.kineticdevelopment.arcana.client.research;

import net.kineticdevelopment.arcana.client.research.impls.StringSectionRenderer;
import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.kineticdevelopment.arcana.core.research.impls.StringSection;
import net.minecraft.entity.player.EntityPlayer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface EntrySectionRenderer<T extends EntrySection>{
	
	Map<String, Supplier<EntrySectionRenderer<?>>> map = new LinkedHashMap<>();
	
	static void init(){
		map.put(StringSection.TYPE, StringSectionRenderer::new);
	}
	
	static <T extends EntrySection> EntrySectionRenderer<T> get(String type){
		return (EntrySectionRenderer<T>)map.get(type);
	}
	
	int span(T section, EntityPlayer player);
	
	void render(T section, int mouseX, int mouseY, boolean right, EntityPlayer player);
	void renderAfter(T section, int mouseX, int mouseY, boolean right, EntityPlayer player);
}