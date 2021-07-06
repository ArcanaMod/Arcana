package net.arcanamod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.client.research.impls.StringSectionRenderer;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkModifyPins;
import net.arcanamod.systems.research.EntrySection;
import net.arcanamod.systems.research.Pin;
import net.arcanamod.systems.research.Requirement;
import net.arcanamod.systems.research.ResearchEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3f;
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

import static net.arcanamod.client.gui.ClientUiUtil.drawTexturedModalRect;

public class ResearchEntryScreen extends Screen {
	public ResourceLocation bg;
	ResearchEntry entry;
	int index;
	Screen parentScreen;
	
	Button left, right, cont, ret;
	List<PinButton> pins;
	
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
	
	public static float TEXT_SCALING = ArcanaConfig.BOOK_TEXT_SCALING.get().floatValue();
	
	public ResearchEntryScreen(ResearchEntry entry, Screen parentScreen){
		super(new StringTextComponent(""));
		this.entry = entry;
		this.parentScreen = parentScreen;
		bg = new ResourceLocation(entry.key().getNamespace(), "textures/gui/research/" + entry.category().book().getPrefix() + SUFFIX);
	}
	
	public void render(MatrixStack stack,  int mouseX, int mouseY, float partialTicks){
		renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		getMinecraft().getTextureManager().bindTexture(bg);
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		drawTexturedModalRect(stack, (width - 256) / 2, (height - 181) / 2 + HEIGHT_OFFSET, 0, 0, 256, 181);
		
		// Main rendering
		if(totalLength() > index){
			EntrySection section = getSectionAtIndex(index);
			if(section != null)
				EntrySectionRenderer.get(section).render(stack, section, sectionIndex(index), width, height, mouseX, mouseY, false, getMinecraft().player);
		}
		if(totalLength() > index + 1){
			EntrySection section = getSectionAtIndex(index + 1);
			if(section != null)
				EntrySectionRenderer.get(section).render(stack, section, sectionIndex(index + 1), width, height, mouseX, mouseY, true, getMinecraft().player);
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
				renderer(requirement).render(stack, baseX + i * reqWidth + 2, y, requirement, getMinecraft().player.ticksExisted, partialTicks, getMinecraft().player);
				renderAmount(stack, requirement, baseX + i * reqWidth + 2, y, requirement.getAmount(), requirement.satisfied(getMinecraft().player));
			}
			// Show tooltips
			for(int i = 0, size = requirements.size(); i < size; i++)
				if(mouseX >= 20 * i + baseX + 2 && mouseX <= 20 * i + baseX + 18 && mouseY >= y && mouseY <= y + 18){
					List<ITextComponent> tooltip = renderer(requirements.get(i)).tooltip(requirements.get(i), getMinecraft().player);
					List<String> lines = new ArrayList<>();
					for(int i1 = 0, tooltipSize = tooltip.size(); i1 < tooltipSize; i1++){
						String s = tooltip.get(i1).getString();
						s = (i1 == 0 ? TextFormatting.WHITE : TextFormatting.GRAY) + s;
						lines.add(s);
					}
					GuiUtils.drawHoveringText(stack, lines.stream().map(StringTextComponent::new).collect(Collectors.toList()), mouseX, mouseY, width, height, -1, getMinecraft().fontRenderer);
					break;
				}
		}
		
		// After-renders (such as tooltips)
		if(totalLength() > index){
			EntrySection section = getSectionAtIndex(index);
			if(section != null)
				EntrySectionRenderer.get(section).renderAfter(stack, section, sectionIndex(index), width, height, mouseX, mouseY, false, getMinecraft().player);
		}
		if(totalLength() > index + 1){
			EntrySection section = getSectionAtIndex(index + 1);
			if(section != null)
				EntrySectionRenderer.get(section).renderAfter(stack, section, sectionIndex(index + 1), width, height, mouseX, mouseY, true, getMinecraft().player);
		}
		
		// Pin tooltips
		pins.forEach(button -> button.renderAfter(stack, mouseX, mouseY));
	}
	
	public void init(@Nonnull Minecraft mc, int p_init_2_, int p_init_3_){
		super.init(mc, p_init_2_, p_init_3_);
		final int y = (height - 181) / 2 + 190 + HEIGHT_OFFSET;
		final int x = width / 2 - 6;
		final int dist = 127;
		left = addButton(new ChangePageButton(x - dist, y, false, button -> {
			if(canTurnLeft())
				index -= 2;
			updateButtons();
		}));
		right = addButton(new ChangePageButton(x + dist, y, true, button -> {
			if(canTurnRight())
				index += 2;
			updateButtons();
		}));
		String text = I18n.format("researchEntry.continue");
		ExtendedButton button = new ExtendedButton(x - getMinecraft().fontRenderer.getStringWidth(text) / 2 + 2, y + 20, getMinecraft().fontRenderer.getStringWidth(text) + 10, 18, new StringTextComponent(text), __ -> {
			Connection.sendTryAdvance(entry.key());
			// need to update visuals when an advance packet is received...
			updateButtons();
		}){
			// I can't be bothered to make a new type for something which will use this behaviours exactly once.
			// If I ever need this behaviour elsewhere, I'll move it to a proper class.
			public void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partial){
				active = Researcher.getFrom(mc.player).entryStage(entry) < entry.sections().size() && entry.sections().get(Researcher.getFrom(getMinecraft().player).entryStage(entry)).getRequirements().stream().allMatch(it -> it.satisfied(getMinecraft().player));
				super.renderWidget(stack, mouseX, mouseY, partial);
			}
		};
		cont = addButton(button);
		ret = addButton(new ReturnToBookButton(width / 2 - 7, (height - 181) / 2 - 26, b -> Minecraft.getInstance().displayGuiScreen(parentScreen)));
		pins = new ArrayList<>();
		updateButtons();
	}
	
	public void updateButtons(){
		left.visible = canTurnLeft();
		right.visible = canTurnRight();
		Researcher researcher = Researcher.getFrom(getMinecraft().player);
		cont.visible = researcher.entryStage(entry) < getVisibleSections().size();
		
		pins.forEach(button -> {
			buttons.remove(button);
			children.remove(button);
		});
		pins.clear();
		List<Pin> collect = entry.getAllPins(getMinecraft().world).filter(p -> researcher.entryStage(p.getEntry()) >= p.getStage()).collect(Collectors.toList());
		for(int i = 0, size = collect.size(); i < size; i++){
			Pin pin = collect.get(i);
			PinButton e = new PinButton((width / 2) + PAGE_WIDTH + 21, (height - PAGE_HEIGHT) / 2 + i * (size > 7 ? 21 : 22) - (size > 7 ? 15 : 0), pin);
			pins.add(e);
			addButton(e);
		}
	}
	
	public boolean keyPressed(int keyCode, int scanCode, int modifiers){
		if(super.keyPressed(keyCode, scanCode, modifiers))
			return true;
		else{
			InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
			if(getMinecraft().gameSettings.keyBindInventory.isActiveAndMatches(mouseKey))
				Minecraft.getInstance().displayGuiScreen(parentScreen);
			else if(keyCode == 292)
				StringSectionRenderer.clearCache();
			return false;
		}
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton){
		super.mouseClicked(mouseX, mouseY, mouseButton);
		// mouse button 1 is right click, just return
		if(mouseButton == 1){
			Minecraft.getInstance().displayGuiScreen(parentScreen);
			return true;
		}
		// mouse button 0 is left click, defer to entries and requirements
		Researcher r = Researcher.getFrom(getMinecraft().player);
		if(r.entryStage(entry) < entry.sections().size() && entry.sections().get(r.entryStage(entry)).getRequirements().size() > 0){
			List<Requirement> requirements = entry.sections().get(r.entryStage(entry)).getRequirements();
			final int y = (height - 181) / 2 + 180;
			final int reqSize = 20;
			final int baseX = (width / 2) - (reqSize * requirements.size() / 2);
			for(int i = 0, size = requirements.size(); i < size; i++)
				if(mouseX >= reqSize * i + baseX && mouseX <= reqSize * i + baseX + reqSize && mouseY >= y && mouseY <= y + reqSize)
					return requirements.get(i).onClick(entry, getMinecraft().player);
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
	
	public boolean mouseScrolled(double mouseX, double mouseY, double scroll){
		if(scroll > 0 && canTurnLeft()){
			index -= 2;
			updateButtons();
			return true;
		}
		if(scroll < 0 && canTurnRight()){
			index += 2;
			updateButtons();
			return true;
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
	
	// What entry we're looking at
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
	
	// How far along in the entry we are
	private int sectionIndex(int index){
		int cur = 0;
		for(EntrySection section : getVisibleSections()){
			if(cur <= index && cur + span(section) > index)
				return index - cur;
			cur += span(section);
		}
		return 0; // throw/show an error
	}
	
	// Index of the given stage
	int indexOfStage(int stage){
		int cur = 0;
		List<EntrySection> sections = getVisibleSections();
		for(int i = 0, size = sections.size(); i < size; i++){
			EntrySection section = sections.get(i);
			if(i == stage)
				return cur;
			cur += span(section);
		}
		return 0; // throw/show an error
	}
	
	private List<EntrySection> getVisibleSections(){
		return entry.sections().stream().filter(this::visible).collect(Collectors.toList());
	}
	
	private boolean visible(EntrySection section){
		// cant use getMinecraft here because this is called from ResearchBookScreen before this is set
		return Researcher.getFrom(Minecraft.getInstance().player).entryStage(entry) >= entry.sections().indexOf(section);
	}
	
	private <T extends Requirement> RequirementRenderer<T> renderer(T requirement){
		return RequirementRenderer.get(requirement);
	}
	
	private void renderAmount(MatrixStack stack, Requirement requirement, int x, int y, int amount, boolean complete){
		if(renderer(requirement).shouldDrawTickOrCross(requirement, amount)){
			//display tick or cross
			getMinecraft().getTextureManager().bindTexture(bg);
			RenderSystem.color4f(1f, 1f, 1f, 1f);
			// ensure it renders over items
			setBlitOffset(300);
			drawTexturedModalRect(stack, x + 10, y + 9, complete ? 0 : 8, 247, 8, 9);
			setBlitOffset(0);
		}else{
			String s = String.valueOf(amount);
			IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			Matrix4f matrix = TransformationMatrix.identity().getMatrix();
			matrix.translate(new Vector3f(0, 0, 300));
			getMinecraft().fontRenderer.renderString(s, (float)(x + 17 - getMinecraft().fontRenderer.getStringWidth(s)), (float)(y + 9), complete ? 0xaaffaa : 0xffaaaa, true, matrix, buffer, false, 0, 15728880);
			buffer.finish();
		}
	}
	
	private int span(EntrySection section){
		return EntrySectionRenderer.get(section).span(section, Minecraft.getInstance().player);
	}
	
	public boolean isPauseScreen(){
		return false;
	}
	
	class ChangePageButton extends Button{
		
		boolean right;
		
		public ChangePageButton(int x, int y, boolean right, IPressable pressable){
			super(x, y, 12, 6, new StringTextComponent(""), pressable);
			this.right = right;
		}
		
		@ParametersAreNonnullByDefault
		public void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks){
			if(visible){
				isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				float mult = isHovered ? 1f : 0.5f;
				RenderSystem.color4f(mult, mult, mult, 1f);
				int texX = right ? 12 : 0;
				int texY = 185;
				getMinecraft().getTextureManager().bindTexture(bg);
				drawTexturedModalRect(stack, x, y, texX, texY, width, height);
				RenderSystem.color4f(1f, 1f, 1f, 1f);
			}
		}
	}
	
	class ReturnToBookButton extends Button{
		
		public ReturnToBookButton(int x, int y, IPressable event){
			super(x, y, 15, 8, new StringTextComponent(""), event);
		}
		
		@ParametersAreNonnullByDefault
		public void renderWidget(MatrixStack stack,  int mouseX, int mouseY, float partialTicks){
			if(visible){
				isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				float mult = isHovered ? 1f : 0.5f;
				RenderSystem.color4f(mult, mult, mult, 1f);
				int texX = 41;
				int texY = 204;
				getMinecraft().getTextureManager().bindTexture(bg);
				drawTexturedModalRect(stack, x, y, texX, texY, width, height);
				RenderSystem.color4f(1f, 1f, 1f, 1f);
			}
		}
	}
	
	class PinButton extends Button{
		
		Pin pin;
		
		public PinButton(int x, int y, Pin pin){
			super(x, y, 18, 18, new StringTextComponent(""), b -> {
				if(Minecraft.getInstance().currentScreen instanceof ResearchEntryScreen){
					// can't reference variables here directly
					ResearchEntryScreen screen = (ResearchEntryScreen)Minecraft.getInstance().currentScreen;
					if(!Screen.hasControlDown()){
						// if stage index is an even number, skip there; else skip to before it.
						int stageIndex = screen.indexOfStage(pin.getStage());
						screen.index = stageIndex % 2 == 0 ? stageIndex : stageIndex - 1;
						screen.updateButtons();
					}else{
						Researcher from = Researcher.getFrom(Minecraft.getInstance().player);
						List<Integer> pinned = from.getPinned().get(pin.getEntry().key());
						if(pinned != null){
							if(!pinned.contains(pin.getStage())){
								from.addPinned(pin.getEntry().key(), pin.getStage());
								Connection.sendModifyPins(pin, PkModifyPins.Diff.pin);
							}else{
								from.removePinned(pin.getEntry().key(), pin.getStage());
								Connection.sendModifyPins(pin, PkModifyPins.Diff.unpin);
							}
						}else{
							// well we know for sure its not been pinned so we have no pins here
							from.addPinned(pin.getEntry().key(), pin.getStage());
							Connection.sendModifyPins(pin, PkModifyPins.Diff.pin);
						}
					}
				}
			});
			visible = true;
			this.pin = pin;
		}
		
		public void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks){
			if(visible){
				RenderSystem.color3f(1, 1, 1);
				
				int stageIndex = indexOfStage(pin.getStage());
				int xOffset = index == (stageIndex % 2 == 0 ? stageIndex : stageIndex - 1) ? 6 : isHovered ? 4 : 0;
				
				getMinecraft().getTextureManager().bindTexture(bg);
				RenderSystem.color4f(1f, 1f, 1f, 1f);
				drawTexturedModalRect(stack, x - 2, y - 1, 16 + (6 - xOffset), 238, 34 - (6 - xOffset), 18);

				ClientUiUtil.renderIcon(stack, pin.getIcon(), x + xOffset - 1, y - 1, 0);
			}
		}
		
		public void renderAfter(MatrixStack stack, int mouseX, int mouseY){
			// check if we're already pinned
			List<Integer> pinned = Researcher.getFrom(getMinecraft().player).getPinned().get(entry.key());
			String tooltip = TextFormatting.AQUA + I18n.format(pinned != null && pinned.contains(pin.getStage()) ? "researchEntry.unpin" : "researchEntry.pin");
			
			isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if(isHovered)
				GuiUtils.drawHoveringText(stack, Lists.newArrayList(new StringTextComponent(pin.getIcon().getStack().getDisplayName().getString()), new StringTextComponent(tooltip)), mouseX, mouseY, ResearchEntryScreen.this.width, ResearchEntryScreen.this.height, -1, Minecraft.getInstance().fontRenderer);
		}
	}
}