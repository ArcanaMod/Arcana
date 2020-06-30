package net.arcanamod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.network.Connection;
import net.arcanamod.research.EntrySection;
import net.arcanamod.research.Requirement;
import net.arcanamod.research.ResearchEntry;
import net.arcanamod.research.Researcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResearchEntryScreen extends Screen{
	
	ResourceLocation bg;
	ResearchEntry entry;
	int index;
	
	Button left, right, cont, ret;
	
	// there is: golem, crucible, crafting, infusion circle, arcane crafting, structure, wand(, arrow), crafting result
	public static final String OVERLAY_SUFFIX = "_gui_overlay.png";
	public static final String SUFFIX = "_gui.png";
	
	// 256 x 181 @ 0,0
	public static final int PAGE_X = 17;
	public static final int PAGE_Y = 10;
	public static final int PAGE_WIDTH = 105;
	public static final int PAGE_HEIGHT = 155;
	public static final int RIGHT_X_OFFSET = 119;
	public static final int HEIGHT_OFFSET = -10;
	
	public static float TEXT_SCALING = ArcanaConfig.BOOK_TEXT_SCALING.get();
	
	public ResearchEntryScreen(ResearchEntry entry){
		super(new StringTextComponent(""));
		this.entry = entry;
		bg = new ResourceLocation(entry.key().getNamespace(), "textures/gui/research/" + entry.category().book().getPrefix() + SUFFIX);
	}
	
	public static void drawModalRectWithCustomSizedTexture(int x, int y, float texX, float texY, int width, int height, int textureWidth, int textureHeight){
		int z = Minecraft.getInstance().currentScreen != null ? Minecraft.getInstance().currentScreen.getBlitOffset() : 1;
		AbstractGui.blit(x, y, z, texX, texY, width, height, textureWidth, textureHeight);
	}
	
	public static void drawTexturedModalRect(int x, int y, float texX, float texY, int width, int height){
		drawModalRectWithCustomSizedTexture(x, y, texX, texY, width, height, 256, 256);
	}
	
	public void render(int mouseX, int mouseY, float partialTicks){
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		getMinecraft().getTextureManager().bindTexture(bg);
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		drawTexturedModalRect((width - 256) / 2, (height - 181) / 2 + HEIGHT_OFFSET, 0, 0, 256, 181);
		
		// Main rendering
		if(totalLength() > index){
			EntrySection section = getSectionAtIndex(index);
			if(section != null)
				EntrySectionRenderer.get(section).render(section, sectionIndex(index), width, height, mouseX, mouseY, false, getMinecraft().player);
		}
		if(totalLength() > index + 1){
			EntrySection section = getSectionAtIndex(index + 1);
			if(section != null)
				EntrySectionRenderer.get(section).render(section, sectionIndex(index + 1), width, height, mouseX, mouseY, true, getMinecraft().player);
		}
		
		// Requirements
		Researcher r = Researcher.getFrom(getMinecraft().player);
		if(r.entryStage(entry) < entry.sections().size() && entry.sections().get(r.entryStage(entry)).getRequirements().size() > 0){
			List<Requirement> requirements = entry.sections().get(r.entryStage(entry)).getRequirements();
			final int y = (height - 181) / 2 + 180;
			final int reqWidth = 20;
			final int baseX = (width / 2) - (reqWidth * requirements.size() / 2);
			for(int i = 0, size = requirements.size(); i < size; i++){
				Requirement requirement = requirements.get(i);
				renderer(requirement).render(baseX + i * reqWidth + 2, y, requirement, getMinecraft().player.ticksExisted, partialTicks, getMinecraft().player);
				renderAmount(baseX + i * reqWidth + 2, y, requirement.getAmount(), requirement.satisfied(getMinecraft().player));
			}
			// Show tooltips
			for(int i = 0, size = requirements.size(); i < size; i++)
				if(mouseX >= 20 * i + baseX + 2 && mouseX <= 20 * i + baseX + 18 && mouseY >= y && mouseY <= y + 18){
					List<ITextComponent> tooltip = renderer(requirements.get(i)).tooltip(requirements.get(i), getMinecraft().player);
					List<String> lines = new ArrayList<>();
					for(int i1 = 0, tooltipSize = tooltip.size(); i1 < tooltipSize; i1++){
						String s = tooltip.get(i1).getFormattedText();
						s = (i1 == 0 ? TextFormatting.WHITE : TextFormatting.GRAY) + s;
						lines.add(s);
					}
					GuiUtils.drawHoveringText(lines, mouseX, mouseY, width, height, -1, getMinecraft().fontRenderer);
					break;
				}
		}
		
		// After-renders (such as tooltips)
		if(totalLength() > index){
			EntrySection section = getSectionAtIndex(index);
			if(section != null)
				EntrySectionRenderer.get(section).renderAfter(section, sectionIndex(index), width, height, mouseX, mouseY, false, getMinecraft().player);
		}
		if(totalLength() > index + 1){
			EntrySection section = getSectionAtIndex(index + 1);
			if(section != null)
				EntrySectionRenderer.get(section).renderAfter(section, sectionIndex(index + 1), width, height, mouseX, mouseY, true, getMinecraft().player);
		}
	}
	
	public void init(@Nonnull Minecraft mc, int p_init_2_, int p_init_3_){
		super.init(mc, p_init_2_, p_init_3_);
		final int y = (height - 181) / 2 + 190 + HEIGHT_OFFSET;
		final int x = width / 2 - 6;
		final int dist = 127;
		left = addButton(new ChangePageButton(x - dist, y, false, button -> {
			if(canTurnLeft())
				index -= 2;
			updateButtonVisibility();
		}));
		right = addButton(new ChangePageButton(x + dist, y, true, button -> {
			if(canTurnRight())
				index += 2;
			updateButtonVisibility();
		}));
		String text = I18n.format("researchEntry.continue");
		ExtendedButton button = new ExtendedButton(x - getMinecraft().fontRenderer.getStringWidth(text) / 2 + 2, y + 20, getMinecraft().fontRenderer.getStringWidth(text) + 10, 18, text, __ -> {
			Connection.sendTryAdvance(entry.key());
			updateButtonVisibility();
		}){
			// I can't be bothered to make a new type for something which will use this behaviours exactly once.
			// If I ever need this behaviour elsewhere, I'll move it to a proper class.
			public void renderButton(int mouseX, int mouseY, float partial){
				active = Researcher.getFrom(mc.player).entryStage(entry) < entry.sections().size() && entry.sections().get(Researcher.getFrom(getMinecraft().player).entryStage(entry)).getRequirements().stream().allMatch(it -> it.satisfied(getMinecraft().player));
				super.renderButton(mouseX, mouseY, partial);
			}
		};
		cont = addButton(button);
		ret = addButton(new ReturnToBookButton(width / 2 - 7, (height - 181) / 2 - 26, p_onPress_1_ -> returnToBook()));
		updateButtonVisibility();
	}
	
	public void updateButtonVisibility(){
		left.visible = canTurnLeft();
		right.visible = canTurnRight();
		cont.visible = Researcher.getFrom(getMinecraft().player).entryStage(entry) < getVisibleSections().size();
	}
	
	public boolean keyPressed(int keyCode, int scanCode, int modifiers){
		if(super.keyPressed(keyCode, scanCode, modifiers))
			return true;
		else{
			InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
			if(getMinecraft().gameSettings.keyBindInventory.isActiveAndMatches(mouseKey))
				returnToBook();
			return false;
		}
	}
	
	private void returnToBook(){
		ResearchBookScreen gui = new ResearchBookScreen(entry.category().book());
		gui.tab = entry.category().book().getCategories().indexOf(entry.category());
		if(gui.tab < 0)
			gui.tab = 0;
		Minecraft.getInstance().displayGuiScreen(gui);
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton){
		super.mouseClicked(mouseX, mouseY, mouseButton);
		Researcher r = Researcher.getFrom(getMinecraft().player);
		if(r.entryStage(entry) < entry.sections().size() && entry.sections().get(r.entryStage(entry)).getRequirements().size() > 0){
			List<Requirement> requirements = entry.sections().get(r.entryStage(entry)).getRequirements();
			final int y = (height - 181) / 2 + 190;
			final int reqWidth = 20;
			final int baseX = (width / 2) - (reqWidth * requirements.size() / 2);
			for(int i = 0, size = requirements.size(); i < size; i++)
				if(mouseX >= 20 * i + baseX + 2 && mouseX <= 20 * i + baseX + 18 && mouseY >= y && mouseY <= y + 18)
					return requirements.get(i).onClick(entry);
		}
		if(totalLength() > index){
			EntrySection section = getSectionAtIndex(index);
			if(section != null)
				return EntrySectionRenderer.get(section).onClick(section, sectionIndex(index), width, height, mouseX, mouseY, false, getMinecraft().player);
		}
		if(totalLength() > index + 1){
			EntrySection section = getSectionAtIndex(index + 1);
			if(section != null)
				return EntrySectionRenderer.get(section).onClick(section, sectionIndex(index + 1), width, height, mouseX, mouseY, true, getMinecraft().player);
		}
		return false;
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
		return Researcher.getFrom(getMinecraft().player).entryStage(entry) >= entry.sections().indexOf(section);
	}
	
	private <T extends Requirement> RequirementRenderer<T> renderer(T requirement){
		return RequirementRenderer.get(requirement);
	}
	
	private void renderAmount(int x, int y, int amount, boolean complete){
		if(amount == 1 || amount == 0){
			//display tick or cross
			getMinecraft().getTextureManager().bindTexture(bg);
			RenderSystem.color4f(1f, 1f, 1f, 1f);
			// ensure it renders over items
			setBlitOffset(300);
			drawTexturedModalRect(x + 10, y + 9, complete ? 0 : 8, 247, 8, 9);
			setBlitOffset(0);
		}else{
			String s = String.valueOf(amount);
			/*RenderSystem.disableLighting();
			RenderSystem.disableDepthTest();
			RenderSystem.disableBlend();*/
			IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			Matrix4f matrix = TransformationMatrix.identity().getMatrix();
			matrix.translate(new Vector3f(0, 0, 300));
			getMinecraft().fontRenderer.renderString(s, (float)(x + 17 - getMinecraft().fontRenderer.getStringWidth(s)), (float)(y + 9), complete ? 0xaaffaa : 0xffaaaa, true, matrix, buffer, false, 0, 15728880);
			buffer.finish();
			/*RenderSystem.enableBlend();
			RenderSystem.enableLighting();
			RenderSystem.enableDepthTest();*/
		}
	}
	
	private int span(EntrySection section){
		return EntrySectionRenderer.get(section).span(section, getMinecraft().player);
	}
	
	public boolean isPauseScreen(){
		return false;
	}
	
	class ChangePageButton extends Button{
		
		boolean right;
		
		public ChangePageButton(int x, int y, boolean right, IPressable pressable){
			super(x, y, 12, 6, "", pressable);
			this.right = right;
		}
		
		@ParametersAreNonnullByDefault
		public void renderButton(int mouseX, int mouseY, float partialTicks){
			if(visible){
				isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				float mult = isHovered ? 1f : 0.5f;
				RenderSystem.color4f(mult, mult, mult, 1f);
				int texX = right ? 12 : 0;
				int texY = 185;
				getMinecraft().getTextureManager().bindTexture(bg);
				drawTexturedModalRect(x, y, texX, texY, width, height);
				RenderSystem.color4f(1f, 1f, 1f, 1f);
			}
		}
	}
	
	class ReturnToBookButton extends Button{
		
		public ReturnToBookButton(int x, int y, IPressable pressable){
			super(x, y, 15, 8, "text", pressable);
		}
		
		@ParametersAreNonnullByDefault
		public void renderButton(int mouseX, int mouseY, float partialTicks){
			if(visible){
				isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				float mult = isHovered ? 1f : 0.5f;
				RenderSystem.color4f(mult, mult, mult, 1f);
				int texX = 41;
				int texY = 204;
				getMinecraft().getTextureManager().bindTexture(bg);
				drawTexturedModalRect(x, y, texX, texY, width, height);
				RenderSystem.color4f(1f, 1f, 1f, 1f);
			}
		}
	}
}