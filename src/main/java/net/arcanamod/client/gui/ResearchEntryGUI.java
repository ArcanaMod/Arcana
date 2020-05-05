package net.arcanamod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import javafx.geometry.Side;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.network.Connection;
import net.arcanamod.research.EntrySection;
import net.arcanamod.research.Requirement;
import net.arcanamod.research.ResearchEntry;
import net.arcanamod.research.Researcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResearchEntryGUI extends Screen{
	
	ResourceLocation bg;
	ResearchEntry entry;
	int index;
	
	Button left, right, cont;
	
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
		super(new StringTextComponent(""));
		this.entry = entry;
		bg = new ResourceLocation(entry.key().getNamespace(), "textures/gui/research/" + entry.category().book().getPrefix() + SUFFIX);
	}
	
	/*public void drawScreen(int mouseX, int mouseY, float partialTicks){
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		mc.getTextureManager().bindTexture(bg);
		GlStateManager.color(1f, 1f, 1f, 1f);
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
		
		// Requirements
		Researcher r = Researcher.getFrom(mc.player);
		if(r.entryStage(entry) < entry.sections().size() && entry.sections().get(r.entryStage(entry)).getRequirements().size() > 0){
			List<Requirement> requirements = entry.sections().get(r.entryStage(entry)).getRequirements();
			final int y = (height - 181) / 2 + 190;
			final int reqWidth = 20;
			final int baseX = (width / 2) - (reqWidth * requirements.size() / 2);
			for(int i = 0, size = requirements.size(); i < size; i++){
				Requirement requirement = requirements.get(i);
				renderer(requirement).render(baseX + i * reqWidth + 2, y, requirement, mc.player.ticksExisted, partialTicks, mc.player);
				renderAmount(baseX + i * reqWidth + 2, y, requirement.getAmount(), requirement.satisfied(mc.player));
			}
			// Show tooltips
			for(int i = 0, size = requirements.size(); i < size; i++)
				if(mouseX >= 20 * i + baseX + 2 && mouseX <= 20 * i + baseX + 18 && mouseY >= y && mouseY <= y + 18){
					List<String> tooltip = renderer(requirements.get(i)).tooltip(requirements.get(i), mc.player);
					List<String> lines = new ArrayList<>();
					for(int i1 = 0, tooltipSize = tooltip.size(); i1 < tooltipSize; i1++){
						String s = tooltip.get(i1);
						s = I18n.format(s);
						s = (i1 == 0 ? TextFormatting.WHITE : TextFormatting.GRAY) + s;
						lines.add(s);
					}
					GuiUtils.drawHoveringText(lines, mouseX, mouseY, width, height, -1, mc.fontRenderer);
					break;
				}
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
		String text = I18n.format("researchEntry.continue");
		GuiButtonExt button = new GuiButtonExt(2, x - fontRenderer.getStringWidth(text) / 2 + 2, y + 20, fontRenderer.getStringWidth(text) + 10, 18, text){
			// I can't be bothered to make a new type for something which will use this behaviours exactly once.
			// If I ever need this behaviour elsewhere, I'll move it to a proper class.
			public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial){
				enabled = Researcher.getFrom(mc.player).entryStage(entry) < entry.sections().size() && entry.sections().get(Researcher.getFrom(mc.player).entryStage(entry)).getRequirements().stream().allMatch(it -> it.satisfied(mc.player));
				super.drawButton(mc, mouseX, mouseY, partial);
			}
		};
		cont = addButton(button);
		
		updateButtonVisibility();
	}
	
	protected void actionPerformed(GuiButton button){
		if(button == left && canTurnLeft())
			index -= 2;
		else if(button == right && canTurnRight())
			index += 2;
		else if(button == cont)
			Connection.sendTryAdvance(entry);
		updateButtonVisibility();
	}
	
	public void updateButtonVisibility(){
		left.visible = canTurnLeft();
		right.visible = canTurnRight();
		cont.visible = Researcher.getFrom(mc.player).entryStage(entry) < getVisibleSections().size();
	}
	
	protected void keyTyped(char typedChar, int keyCode) throws IOException{
		if(this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)){
			ResearchBookGUI gui = new ResearchBookGUI(entry.category().book());
			gui.tab = entry.category().book().getCategories().indexOf(entry.category());
			if(gui.tab < 0)
				gui.tab = 0;
			mc.displayGuiScreen(gui);
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(totalLength() > index){
			EntrySection section = getSectionAtIndex(index);
			if(section != null)
				EntrySectionRenderer.get(section).onClick(section, sectionIndex(index), width, height, mouseX, mouseY, false, mc.player);
		}
		if(totalLength() > index + 1){
			EntrySection section = getSectionAtIndex(index + 1);
			if(section != null)
				EntrySectionRenderer.get(section).onClick(section, sectionIndex(index + 1), width, height, mouseX, mouseY, true, mc.player);
		}
		Researcher r = Researcher.getFrom(mc.player);
		if(r.entryStage(entry) < entry.sections().size() && entry.sections().get(r.entryStage(entry)).getRequirements().size() > 0){
			List<Requirement> requirements = entry.sections().get(r.entryStage(entry)).getRequirements();
			final int y = (height - 181) / 2 + 190;
			final int reqWidth = 20;
			final int baseX = (width / 2) - (reqWidth * requirements.size() / 2);
			for(int i = 0, size = requirements.size(); i < size; i++)
				if(mouseX >= 20 * i + baseX + 2 && mouseX <= 20 * i + baseX + 18 && mouseY >= y && mouseY <= y + 18){
					requirements.get(i).onClick(entry);
					break;
				}
		}
	}
	
	private boolean canTurnRight(){
		return index < totalLength() - 2;
	}
	
	private boolean canTurnLeft(){
		return index > 0;
	}
	
	private int totalLength(){
		return entry.sections().stream().filter(this::visible).mapToInt(this::span).sum();
	}
	
	private EntrySection getSectionAtIndex(int index){
		if(index == 0)
			return entry.sections().get(0);
		int cur = 0;
		for(EntrySection section : getVisibleSections()){
			if(cur <= index && cur + span(section) > index)
				return section;
			cur += span(section);
		}
		return null;
	}
	
	private int sectionIndex(int index){
		int cur = 0;
		for(EntrySection section : getVisibleSections()){
			if(cur <= index && cur + span(section) > index)
				return index - cur;
			cur += span(section);
		}
		return 0; // throw/show an error
	}
	
	private List<EntrySection> getVisibleSections(){
		return entry.sections().stream().filter(this::visible).collect(Collectors.toList());
	}
	
	private boolean visible(EntrySection section){
		return Researcher.getFrom(Minecraft.getMinecraft().player).entryStage(entry) >= entry.sections().indexOf(section);
	}
	
	private <T extends Requirement> RequirementRenderer<T> renderer(T requirement){
		return RequirementRenderer.get(requirement);
	}
	
	private void renderAmount(int x, int y, int amount, boolean complete){
		if(amount == 1 || amount == 0){
			//display tick or cross
			mc.getTextureManager().bindTexture(bg);
			GlStateManager.color(1f, 1f, 1f, 1f);
			// ensure it renders over items
			zLevel = 300;
			drawTexturedModalRect(x + 10, y + 9, complete ? 0 : 8, 247, 8, 9);
			zLevel = 0;
		}else{
			String s = String.valueOf(amount);
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableBlend();
			mc.fontRenderer.drawStringWithShadow(s, (float)(x + 17 - mc.fontRenderer.getStringWidth(s)), (float)(y + 9), complete ? 0xaaffaa : 0xffaaaa);
			GlStateManager.enableBlend();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
		}
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
	}*/
}