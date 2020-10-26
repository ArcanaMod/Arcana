package net.arcanamod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.blocks.tiles.FociForgeTileEntity;
import net.arcanamod.containers.FociForgeContainer;
import net.arcanamod.containers.slots.AspectSlot;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.List;

public class FociForgeScreen extends AspectContainerScreen<FociForgeContainer> {
	public static final int WIDTH = 378;
	public static final int HEIGHT = 280;

	private static final ResourceLocation BG = new ResourceLocation(Arcana.MODID, "textures/gui/container/gui_researchbook.png");
	private static final ResourceLocation NO_INK = new ResourceLocation(Arcana.MODID, "textures/gui/research/no_ink_overlay.png");

	FociForgeTileEntity te;
	int page = 0;

	Button leftArrow, rightArrow;
	TextFieldWidget searchWidget;

	public FociForgeScreen(FociForgeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn){
		super(screenContainer, inv, titleIn);
		this.te = screenContainer.te;
		this.aspectContainer = screenContainer;
		xSize = WIDTH;
		ySize = HEIGHT;
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		renderBackground();
		minecraft.getTextureManager().bindTexture(BG);
		UiUtil.drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT, 378, 378);
		searchWidget.render(mouseX, mouseY, partialTicks);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.searchWidget != null) {
			this.searchWidget.tick();
			searchWidget.setSuggestion(searchWidget.getText().equals("") ? I18n.format("fociForge.search") : "");
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
		if (!searchWidget.getText().equals("")) {
			FociForgeContainer container = aspectContainer;
			List<AspectSlot> slots = container.scrollableSlots;
			for(int i = 0; i < slots.size(); i++){
				AspectSlot slot = slots.get(i);
				slot.visible = (i >= 36 * page && i < 36 * (page + 1)) && AspectUtils.getLocalizedAspectDisplayName(slot.getAspect()).toLowerCase().contains(this.searchWidget.getText().toLowerCase());
			}
		}else
			refreshSlotVisibility();
	}

	@Override
	protected void init() {
		super.init();

		searchWidget = new TextFieldWidget(font,guiLeft + 13,guiTop + 14,120,15,I18n.format("fociForge.search"));
		searchWidget.setMaxStringLength(30);
		searchWidget.setEnableBackgroundDrawing(false);
		searchWidget.setTextColor(16777215);
		searchWidget.setVisible(true);
		searchWidget.setCanLoseFocus(false);
		searchWidget.setFocused2(true);
		children.add(searchWidget);
		leftArrow = addButton(new FociForgeScreen.ChangeAspectPageButton(guiLeft + 11, guiTop + 183, false, this::actionPerformed));
		rightArrow = addButton(new FociForgeScreen.ChangeAspectPageButton(guiLeft + 112, guiTop + 183, true, this::actionPerformed));
	}

	protected void actionPerformed(@Nonnull Button button) {
		if(button == leftArrow && page > 0)
			page--;
		FociForgeContainer container = aspectContainer;
		if(button == rightArrow && container.scrollableSlots.size() > 36 * (page + 1))
			page++;

		if (searchWidget.getText().equals(""))
			refreshSlotVisibility();
	}

	protected void refreshSlotVisibility(){
		FociForgeContainer container = aspectContainer;
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
		public void render(int mouseX, int mouseY, float partialTicks) {
			if(visible){
				isHovered = mouseX >= x && mouseY >= + y && mouseX < guiLeft + x + width && mouseY < guiTop + y + height;
				int teX = right ? 120 : 135;
				int teY = 307;
				FociForgeContainer container = aspectContainer;
				// first check if there are multiple pages
				if(container != null)
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
				RenderSystem.color4f(1, 1, 1, 1);
				UiUtil.drawModalRectWithCustomSizedTexture(x, y, teX, teY, width, height, 378, 378);
			}
		}
	}
}
