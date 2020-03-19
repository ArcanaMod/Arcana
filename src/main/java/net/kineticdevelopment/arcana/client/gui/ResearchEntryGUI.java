package net.kineticdevelopment.arcana.client.gui;

import net.kineticdevelopment.arcana.client.research.EntrySectionRenderer;
import net.kineticdevelopment.arcana.core.research.EntrySection;
import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

public class ResearchEntryGUI extends GuiScreen{
	
	ResourceLocation bg;
	ResearchEntry entry;
	int index;
	
	GuiButton left, right;
	
	// there is: golem, crucible, crafting, infusion circle, arcane crafting, structure, wand(, arrow), crafting result
	public static final String OVERLAY_SUFFIX = "_gui_overlay.png";
	public static final String SUFFIX = "_gui.png";
	
	// 256 x 181 @ 0,0
	public static final int PAGE_X = 17;
	public static final int PAGE_Y = 10;
	public static final int PAGE_WIDTH = 105;
	public static final int PAGE_HEIGHT = 155;
	public static final int RIGHT_X_OFFSET = 119;
	
	public ResearchEntryGUI(ResearchEntry entry){
		this.entry = entry;
		bg = new ResourceLocation(entry.key().getResourceDomain(), "textures/gui/research/" + entry.category().getBook().getPrefix() + SUFFIX);
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		mc.getTextureManager().bindTexture(bg);
		drawTexturedModalRect((width - 256) / 2, (height - 181) / 2, 0, 0, 256, 181);
		
		// Main rendering
		if(totalLength() > index){
			EntrySection section = getSectionAtIndex(index);
			if(section != null)
				EntrySectionRenderer.get(section).render(section, sectionIndex(index), width, height, mouseX, mouseY, false, mc.player);
		}
		if(totalLength() > index + 1){
			EntrySection section = getSectionAtIndex(index + 1);
			if(section != null)
				EntrySectionRenderer.get(section).render(section, sectionIndex(index + 1), width, height, mouseX, mouseY, true, mc.player);
		}
		
		// After-renders (such as tooltips)
		if(totalLength() > index){
			EntrySection section = getSectionAtIndex(index);
			if(section != null)
				EntrySectionRenderer.get(section).renderAfter(section, sectionIndex(index), width, height, mouseX, mouseY, false, mc.player);
		}
		if(totalLength() > index + 1){
			EntrySection section = getSectionAtIndex(index + 1);
			if(section != null)
				EntrySectionRenderer.get(section).renderAfter(section, sectionIndex(index + 1), width, height, mouseX, mouseY, true, mc.player);
		}
	}
	
	public void initGui(){
		final int y = (height - 181) / 2 + 190;
		final int x = width / 2 - 6;
		final int dist = 127;
		left = addButton(new ChangePageButton(0, x - dist, y, false));
		right = addButton(new ChangePageButton(1, x + dist, y, true));
		
		updateButtonVisibility();
	}
	
	protected void actionPerformed(GuiButton button){
		if(button == left && canTurnLeft())
			index -= 2;
		else if(button == right && canTurnRight())
			index += 2;
		updateButtonVisibility();
	}
	
	protected void updateButtonVisibility(){
		left.visible = canTurnLeft();
		right.visible = canTurnRight();
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
	
	class ChangePageButton extends GuiButton{
		
		boolean right;
		
		public ChangePageButton(int buttonId, int x, int y, boolean right){
			super(buttonId, x, y, 12, 6, "");
			this.right = right;
		}
		
		@ParametersAreNonnullByDefault
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks){
			if(visible){
				hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				float mult = hovered ? 1f : 0.5f;
				GlStateManager.color(mult, mult, mult, 1f);
				int texX = right ? 12 : 0;
				int texY = 185;
				mc.getTextureManager().bindTexture(bg);
				drawTexturedModalRect(x, y, texX, texY, width, height);
				GlStateManager.color(1f, 1f, 1f, 1f);
			}
		}
	}
}