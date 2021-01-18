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
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static net.arcanamod.containers.FociForgeContainer.ASPECT_H_COUNT;
import static net.arcanamod.containers.FociForgeContainer.ASPECT_V_COUNT;

public class FociForgeScreen extends AspectContainerScreen<FociForgeContainer> {
	public static final int WIDTH = 397;
	public static final int HEIGHT = 283;

	public static final int ASPECT_SCROLL_X = 80;
	public static final int ASPECT_SCROLL_Y = 52;
	public static final int ASPECT_SCROLL_HEIGHT = 97;
	public static final int FOCI_SCROLL_X = 343;
	public static final int FOCI_SCROLL_Y = 40;
	public static final int FOCI_SCROLL_HEIGHT = 137;
	public static final int SCROLL_WIDTH = 12;
	public static final int SCROLL_HEIGHT = 15;

	private static final ResourceLocation BG = new ResourceLocation(Arcana.MODID, "textures/gui/container/gui_fociforge.png");


	FociForgeTileEntity te;
	float aspectScroll = 0, fociScroll = 0;
	boolean isScrollingAspect = false, isScrollingFoci = false;

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
		UiUtil.drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT, 397, 397);
		UiUtil.drawModalRectWithCustomSizedTexture(guiLeft + ASPECT_SCROLL_X, guiTop + ASPECT_SCROLL_Y + (int)(ASPECT_SCROLL_HEIGHT * aspectScroll), 7, 345, SCROLL_WIDTH, SCROLL_HEIGHT, 397, 397);
		UiUtil.drawModalRectWithCustomSizedTexture(guiLeft + FOCI_SCROLL_X, guiTop + FOCI_SCROLL_Y + (int)(FOCI_SCROLL_HEIGHT * fociScroll), 7, 345, SCROLL_WIDTH, SCROLL_HEIGHT, 397, 397);
		searchWidget.render(mouseX, mouseY, partialTicks);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.searchWidget != null) {
			this.searchWidget.tick();
			searchWidget.setSuggestion(searchWidget.getText().equals("") ? I18n.format("fociForge.search") : "");
			this.refreshSlotVisibility();
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

	@Override
	public boolean mouseClicked(double x, double y, int button) {
		if (button == 0) {
			double guiX = x - (double)this.guiLeft;
			double guiY = y - (double)this.guiTop;

			if (guiX > ASPECT_SCROLL_X && guiX < ASPECT_SCROLL_X + SCROLL_WIDTH
				&& guiY > ASPECT_SCROLL_Y && guiY < ASPECT_SCROLL_X + ASPECT_SCROLL_HEIGHT) {
				isScrollingAspect = true;
			} else if (guiX > FOCI_SCROLL_X && guiX < FOCI_SCROLL_X + SCROLL_WIDTH
					&& guiY > FOCI_SCROLL_Y && guiY < FOCI_SCROLL_X + FOCI_SCROLL_HEIGHT) {
				isScrollingFoci = true;
			}
		}

		return super.mouseClicked(x, y, button);
	}

	@Override
	public boolean mouseDragged(double x, double y, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		double guiY = y - (double)this.guiTop;
		if (this.isScrollingAspect) {
			this.aspectScroll = (float)((guiY - ASPECT_SCROLL_Y - 7.5F) / (ASPECT_SCROLL_HEIGHT));
			this.aspectScroll = MathHelper.clamp(this.aspectScroll, 0.0F, 1.0F);
			this.container.scrollAspectTo(this.aspectScroll);
			return true;
		} else if (this.isScrollingFoci) {
			this.fociScroll = (float)((guiY - FOCI_SCROLL_Y - 7.5F) / (FOCI_SCROLL_HEIGHT));
			this.fociScroll = MathHelper.clamp(this.fociScroll, 0.0F, 1.0F);
			this.container.scrollFociTo(this.fociScroll);
			return true;
		} else {
			return super.mouseDragged(x, y, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
		}
	}

	@Override
	public boolean mouseReleased(double x, double y, int button) {
		if (button == 0) {
			this.isScrollingAspect = false;
			this.isScrollingFoci = false;
		}

		return super.mouseReleased(x, y, button);
	}

	@Override
	public boolean mouseScrolled(double x, double y, double bars) {
		int extraRows = (this.container.scrollableSlots.size() + ASPECT_H_COUNT - 1) / ASPECT_H_COUNT - ASPECT_V_COUNT;
		this.aspectScroll = (float)(this.aspectScroll - bars / extraRows);
		this.aspectScroll = MathHelper.clamp(this.aspectScroll, 0.0F, 1.0F);
		this.container.scrollAspectTo(this.aspectScroll);
		this.refreshSlotVisibility();
		return true;
	}

	private static boolean slotMatchesSearch(AspectSlot slot, String str) {
		if (str.equals(""))
			return true;
		return AspectUtils.getLocalizedAspectDisplayName(slot.getAspect()).toLowerCase().contains(str.toLowerCase());
	}

	@Override
	protected void init() {
		super.init();

		searchWidget = new TextFieldWidget(font,guiLeft + 13,guiTop + 36,120,15,I18n.format("fociForge.search"));
		searchWidget.setMaxStringLength(30);
		searchWidget.setEnableBackgroundDrawing(false);
		searchWidget.setTextColor(0xFFFFFF);
		searchWidget.setVisible(true);
		searchWidget.setCanLoseFocus(false);
		searchWidget.setFocused2(true);
		children.add(searchWidget);
	}

	protected void actionPerformed(@Nonnull Button button) {
		if (searchWidget.getText().equals(""))
			refreshSlotVisibility();
	}

	protected void refreshSlotVisibility(){
		List<AspectSlot> slots = aspectContainer.getAspectSlots();
		for(int i = 0; i < slots.size(); i++){
			AspectSlot slot = slots.get(i);
			slot.visible = slotMatchesSearch(slot, searchWidget.getText());;
		}
	}
}
