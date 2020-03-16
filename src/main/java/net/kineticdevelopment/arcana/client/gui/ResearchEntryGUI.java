package net.kineticdevelopment.arcana.client.gui;

import net.kineticdevelopment.arcana.client.research.EntrySectionRenderer;
import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class ResearchEntryGUI extends GuiScreen{
	
	ResearchEntry entry;
	int index;
	
	// there is: golem, crucible, crafting, infusion circle, arcane crafting, structure, wand(, arrow), crafting result
	public static final String OVERLAY_SUFFIX = "_gui_overlay.png";
	public static final String SUFFIX = "_gui.png";
	
	public static final int PAGE_X = 17;
	public static final int PAGE_Y = 10;
	public static final int PAGE_WIDTH = 105;
	public static final int PAGE_HEIGHT = 155;
	public static final int RIGHT_X_OFFSET = 119;
	
	public ResearchEntryGUI(ResearchEntry entry){
		this.entry = entry;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawDefaultBackground();
		// 256 x 181 @ 0,0
		mc.getTextureManager().bindTexture(new ResourceLocation(entry.key().getResourceDomain(), "textures/gui/research/" + entry.category().getBook().getPrefix() + SUFFIX));
		drawTexturedModalRect((width - 256) / 2, (height - 181) / 2, 0, 0, 256, 181);
		
		if(totalLength() > index){
			EntrySection section = getSectionAtIndex(index);
			if(section != null)
				EntrySectionRenderer.get(section.getType()).render(section, sectionIndex(index), width, height, mouseX, mouseY, false, mc.player);
		}
		if(totalLength() > index + 1){
			EntrySection section = getSectionAtIndex(index + 1);
			if(section != null)
				EntrySectionRenderer.get(section.getType()).render(section, sectionIndex(index + 1), width, height, mouseX, mouseY, true, mc.player);
		}
	}
	
	private boolean canTurnRight(){
		return index < totalLength() - 2;
	}
	
	private boolean canTurnLeft(){
		return index > 0;
	}
	
	private int totalLength(){
		return entry.sections().stream().mapToInt(this::span).sum();
	}
	
	public void initGui(){
		super.initGui();
	}
	
	private EntrySection getSectionAtIndex(int index){
		if(index == 0)
			return entry.sections().get(0);
		int cur = 0;
		for(EntrySection section : entry.sections()){
			if(cur <= index && cur + span(section) > index)
				return section;
			cur += span(section);
		}
		return null;
	}
	
	private int sectionIndex(int index){
		int cur = 0;
		for(EntrySection section : entry.sections()){
			if(cur <= index && cur + span(section) > index)
				return index - cur;
			cur += span(section);
		}
		return 0; // throw/show an error
	}
	
	private int span(EntrySection section){
		return EntrySectionRenderer.get(section).span(section, mc.player);
	}
	
	public boolean doesGuiPauseGame(){
		return false;
	}
}