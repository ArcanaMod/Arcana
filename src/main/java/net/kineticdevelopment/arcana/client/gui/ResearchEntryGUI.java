package net.kineticdevelopment.arcana.client.gui;

import net.kineticdevelopment.arcana.client.research.EntrySectionRenderer;
import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.minecraft.client.gui.GuiScreen;

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
	public static final int RIGHT_X_OFFSET = 102 - PAGE_X;
	
	public ResearchEntryGUI(ResearchEntry entry){
		this.entry = entry;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawDefaultBackground();
		
		
		if(entry.sections().size() > index){
			EntrySection section = entry.sections().get(index);
			if(section != null)
				EntrySectionRenderer.get(section.getType()).render(section, /*pageIndex*/0, width, height, mouseX, mouseY, false, mc.player);
		}
		if(entry.sections().size() > index + 1){
			EntrySection section = entry.sections().get(index = 1);
			if(section != null)
				EntrySectionRenderer.get(section.getType()).render(section, /*pageIndex*/0, width, height, mouseX, mouseY, false, mc.player);
		}
	}
	
	private boolean canTurnRight(){
		return index < totalLength() - 2;
	}
	
	private boolean canTurnLeft(){
		return index > 0;
	}
	
	private int totalLength(){
		int sum = 0;
		for(int x = 0; index < entry.sections().size(); x++){
			EntrySection section = entry.sections().get(x);
			int span = EntrySectionRenderer.get(section).span(section, mc.player);
			sum += span;
		}
		return sum;
	}
	
	public void initGui(){
		super.initGui();
	}
}