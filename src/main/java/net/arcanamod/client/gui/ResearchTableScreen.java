package net.arcanamod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.arcanamod.client.research.PuzzleRenderer;
import net.arcanamod.containers.slots.AspectSlot;
import net.arcanamod.containers.ResearchTableContainer;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.ResearchBooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.List;

public class ResearchTableScreen extends AspectContainerScreen<ResearchTableContainer> {
	
	public static final int WIDTH = 378;
	public static final int HEIGHT = 280;
	
	private static final ResourceLocation BG = new ResourceLocation(Arcana.MODID, "textures/gui/container/gui_researchbook.png");
	private static final ResourceLocation NO_INK = new ResourceLocation(Arcana.MODID, "textures/gui/research/no_ink_overlay.png");
	
	ResearchTableTileEntity te;
	int page = 0;
	
	Button leftArrow, rightArrow;
	TextFieldWidget searchWidget;
	
	public ResearchTableScreen(ResearchTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn){
		super(screenContainer, inv, titleIn);
		this.te = screenContainer.te;
		this.aspectContainer = screenContainer;
		xSize = WIDTH;
		ySize = HEIGHT;
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		renderBackground();
		minecraft.getTextureManager().bindTexture(BG);
		renderModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT, 378, 378);
		if(!te.note().isEmpty() && te.note().getItem() == ArcanaItems.RESEARCH_NOTE.get()){
			CompoundNBT compound = te.note().getTag();
			if(compound != null){
				Puzzle puzzle = ResearchBooks.puzzles.get(new ResourceLocation(compound.getString("puzzle")));
				if (puzzle!=null) {
					PuzzleRenderer.get(puzzle).render(puzzle, ((ResearchTableContainer) aspectContainer).puzzleSlots, ((ResearchTableContainer) aspectContainer).puzzleItemSlots, width, height, mouseX, mouseY, playerInventory.player);
					if (te.ink().isEmpty()) {
						// tell them "no u cant do research without a pen"
						this.minecraft.getTextureManager().bindTexture(this.NO_INK);
						renderModalRectWithCustomSizedTexture(guiLeft + 137, guiTop + 31, 0, 0, 223, 143, 223, 143);
						String noInk = I18n.format("researchTable.ink_needed");
						font.drawString(noInk, guiLeft + 141 + (213 - font.getStringWidth(noInk)) / 2, guiTop + 35 + (134 - font.FONT_HEIGHT) / 2, -1);
					}
				}
			}
		}
		searchWidget.render(mouseX, mouseY, partialTicks);
	}

	public static void renderModalRectWithCustomSizedTexture(int x, int y, float texX, float texY, int width, int height, int textureWidth, int textureHeight){
		int z = Minecraft.getInstance().currentScreen != null ? Minecraft.getInstance().currentScreen.getBlitOffset() : 1;
		AbstractGui.blit(x, y, z, texX, texY, width, height, textureWidth, textureHeight);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.searchWidget != null) {
			this.searchWidget.tick();
			searchWidget.setSuggestion(searchWidget.getText().equals("") ? I18n.format("researchTable.search") : "");
			this.updateAspectSearch();
		}
	}

	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		if (searchWidget.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)) {
			return true;
		} else {
			return searchWidget.isFocused() && searchWidget.getVisible() && p_keyPressed_1_ != 256 || super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		}
	}

	private void updateAspectSearch() {
		if (!this.searchWidget.getText().equals("")) {
			ResearchTableContainer container = (ResearchTableContainer)aspectContainer;
			List<AspectSlot> slots = container.scrollableSlots;
			for(int i = 0; i < slots.size(); i++){
				AspectSlot slot = slots.get(i);
				slot.visible = (i >= 36 * page && i < 36 * (page + 1)) && AspectUtils.getLocalizedAspectDisplayName(slot.getAspect()).toLowerCase().contains(this.searchWidget.getText().toLowerCase());
			}
		}
		else refreshSlotVisibility();
	}

	@Override
	protected void init() {
		super.init();

		searchWidget = new TextFieldWidget(font,guiLeft + 13,guiTop + 14,120,15,I18n.format("researchTable.search"));
		searchWidget.setMaxStringLength(30);
		searchWidget.setEnableBackgroundDrawing(false);
		searchWidget.setTextColor(16777215);
		searchWidget.setVisible(true);
		searchWidget.setCanLoseFocus(false);
		searchWidget.setFocused2(true);
		children.add(searchWidget);
		leftArrow = addButton(new ChangeAspectPageButton(guiLeft + 11, guiTop + 183, false, this::actionPerformed));
		rightArrow = addButton(new ChangeAspectPageButton(guiLeft + 112, guiTop + 183, true, this::actionPerformed));
	}

	protected void actionPerformed(@Nonnull Button button) {
		if(button == leftArrow && page > 0)
			page--;
		ResearchTableContainer container = (ResearchTableContainer)aspectContainer;
		if(button == rightArrow && container.scrollableSlots.size() > 36 * (page + 1))
			page++;

		if (searchWidget.getText().equals(""))
			refreshSlotVisibility();
	}
	
	protected void refreshSlotVisibility(){
		ResearchTableContainer container = (ResearchTableContainer)aspectContainer;
		List<AspectSlot> slots = container.scrollableSlots;
		for(int i = 0; i < slots.size(); i++){
			AspectSlot slot = slots.get(i);
			slot.visible = i >= 36 * page && i < 36 * (page + 1);
		}
	}
	
	class ChangeAspectPageButton extends Button{
		
		boolean right;
		
		public ChangeAspectPageButton(int x, int y, boolean right, Button.IPressable onPress){
			super(x, y, 15, 11, "", onPress);
			this.right = right;
		}

		@Override
		public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
			if(visible){
				//hovered = mouseX >= guiLeft + x && mouseY >= guiTop + y && mouseX < guiLeft + x + width && mouseY < guiTop + y + height;
				int teX = right ? 120 : 135;
				int teY = 307;
				ResearchTableContainer container = (ResearchTableContainer)aspectContainer;
				// first check if there are multiple pages
				if (container!=null)
				if(container.scrollableSlots.size() > 36)
					if(right){
						// if I am not on the last page
						if(container.scrollableSlots.size() > 36 * (page + 1)){
							teY -= 11;
							if(isHovered)
								teY -= 11;
						}
					}else{
						// if I am not on the first page
						if(page > 0){
							teY -= 11;
							if(isHovered)
								teY -= 11;
						}
					}
				// then just draw
				minecraft.getTextureManager().bindTexture(BG);
				GlStateManager.disableLighting();
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				renderModalRectWithCustomSizedTexture(x, y, teX, teY, width, height, 378, 378);
			}
			//super.render(p_render_1_,p_render_2_,p_render_3_); //Don't render default button! //TODO: Hover dosn't work
		}
	}
}