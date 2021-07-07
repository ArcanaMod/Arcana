package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.tiles.FociForgeTileEntity;
import net.arcanamod.containers.FociForgeContainer;
import net.arcanamod.containers.slots.AspectSlot;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.attachment.FocusItem;
import net.arcanamod.systems.spell.SpellState;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AirItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

import static net.arcanamod.containers.FociForgeContainer.ASPECT_H_COUNT;
import static net.arcanamod.containers.FociForgeContainer.ASPECT_V_COUNT;

public class FociForgeScreen extends AspectContainerScreen<FociForgeContainer> {
	public static final int WIDTH = 397;
	public static final int HEIGHT = 283;

	public static final int SPELL_X = 119;
	public static final int SPELL_Y = 32;
	public static final int SPELL_WIDTH = 202;
	public static final int SPELL_HEIGHT = 133;

	public static final int ASPECT_SCROLL_X = 80;
	public static final int ASPECT_SCROLL_Y = 52;
	public static final int ASPECT_SCROLL_HEIGHT = 97;
	public static final int FOCI_SCROLL_X = 343;
	public static final int FOCI_SCROLL_Y = 40;
	public static final int FOCI_SCROLL_HEIGHT = 137;
	public static final int SCROLL_WIDTH = 12;
	public static final int SCROLL_HEIGHT = 15;
	public static final int FOCI_V_COUNT = 9;
	public static final int FOCI_APPLY_X = 338;
	public static final int FOCI_APPLY_Y = 12;
	public static final int APPLY_BTN_SIZE = 16;

	public static final ResourceLocation BG = new ResourceLocation(Arcana.MODID, "textures/gui/container/gui_fociforge.png");

	FociForgeTileEntity te;
	float aspectScroll = 0, fociScroll = 0;
	boolean isScrollingAspect = false, isScrollingFoci = false, spellHasFocus = false;


	TextFieldWidget searchWidget;

	public FociForgeScreen(FociForgeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn){
		super(screenContainer, inv, titleIn);
		this.te = screenContainer.te;
		this.aspectContainer = screenContainer;
		this.aspectContainer.setSymbolic(true);
		xSize = WIDTH;
		ySize = HEIGHT;
		scrollAspectTo(0);
		scrollFociTo(0);
	}

	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY){
		renderBackground(stack);
		minecraft.getTextureManager().bindTexture(BG);
		searchWidget.render(stack, mouseX, mouseY, partialTicks);
		minecraft.getTextureManager().bindTexture(BG);
		ClientUiUtil.drawModalRectWithCustomSizedTexture(stack, guiLeft, guiTop, 0, 0, WIDTH, HEIGHT, 397, 397);
		ClientUiUtil.drawModalRectWithCustomSizedTexture(stack, guiLeft + ASPECT_SCROLL_X, guiTop + ASPECT_SCROLL_Y + (int)(ASPECT_SCROLL_HEIGHT * aspectScroll), 7, 345, SCROLL_WIDTH, SCROLL_HEIGHT, 397, 397);
		ClientUiUtil.drawModalRectWithCustomSizedTexture(stack, guiLeft + FOCI_SCROLL_X, guiTop + FOCI_SCROLL_Y + (int)(FOCI_SCROLL_HEIGHT * fociScroll), 7, 345, SCROLL_WIDTH, SCROLL_HEIGHT, 397, 397);
		// Foci apply btn
		ClientUiUtil.drawModalRectWithCustomSizedTexture(stack, guiLeft + FOCI_APPLY_X, guiTop + FOCI_APPLY_Y, 288, 313, APPLY_BTN_SIZE, APPLY_BTN_SIZE, 397, 397);
		te.spellState.render(stack, guiLeft, guiTop,guiLeft + SPELL_X, guiTop + SPELL_Y, SPELL_WIDTH, SPELL_HEIGHT, mouseX, mouseY);
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
		double guiX = x - guiLeft;
		double guiY = y - guiTop;

		if (button == 0) {
			if (guiX >= ASPECT_SCROLL_X && guiX < ASPECT_SCROLL_X + SCROLL_WIDTH
				&& guiY >= ASPECT_SCROLL_Y && guiY < ASPECT_SCROLL_X + ASPECT_SCROLL_HEIGHT) {
				isScrollingAspect = true;
			} else if (guiX >= FOCI_SCROLL_X && guiX < FOCI_SCROLL_X + SCROLL_WIDTH
						&& guiY >= FOCI_SCROLL_Y && guiY < FOCI_SCROLL_X + FOCI_SCROLL_HEIGHT) {
				isScrollingFoci = true;
			} else if (guiX > SPELL_X && guiX < SPELL_X + SPELL_WIDTH
						&& guiY > SPELL_Y && guiY < SPELL_Y + SPELL_HEIGHT) {
				spellHasFocus = true;
			} else if (guiX >= FOCI_APPLY_X && guiY >= FOCI_APPLY_Y && guiX <= FOCI_APPLY_X + APPLY_BTN_SIZE && guiY <= FOCI_APPLY_Y + APPLY_BTN_SIZE) container.changeFociSpell();
			te.spellState.mouseDown((int)(guiX - SPELL_X), (int)(guiY - SPELL_Y), button, aspectContainer.getHeldAspect());
		}

		return super.mouseClicked(x, y, button);
	}

	@Override
	public boolean mouseDragged(double x, double y, int button, double move_x, double move_y) {
		double guiX = x - guiLeft;
		double guiY = y - guiTop;
		if (button == 0) {
			if (isScrollingAspect) {
				this.aspectScroll = (float)((guiY - ASPECT_SCROLL_Y - 7.5F) / (ASPECT_SCROLL_HEIGHT));
				this.aspectScroll = MathHelper.clamp(this.aspectScroll, 0.0F, 1.0F);
				scrollAspectTo(this.aspectScroll);
				return true;
			} else if (isScrollingFoci) {
				this.fociScroll = (float) ((guiY - FOCI_SCROLL_Y - 7.5F) / (FOCI_SCROLL_HEIGHT));
				this.fociScroll = MathHelper.clamp(this.fociScroll, 0.0F, 1.0F);
				scrollFociTo(this.fociScroll);
				return true;
			} else if (spellHasFocus) {
				te.spellState.drag((int)(guiX - SPELL_X), (int)(guiY - SPELL_Y), button, move_x, move_y);
				return true;
			}
		}
		return super.mouseDragged(x, y, button, move_x, move_y);
	}

	// TODO: Move selection logic to rising edge trigger (mouseDown)
	@Override
	public boolean mouseReleased(double x, double y, int button) {
		double guiX = x - guiLeft;
		double guiY = y - guiTop;
		ItemStack heldItem = Arcana.proxy.getPlayerOnClient().inventory.getItemStack();
		if (button == 0) {
			isScrollingAspect = false;
			isScrollingFoci = false;
			spellHasFocus = false;
			boolean success = te.spellState.mouseUp((int)(guiX - SPELL_X), (int)(guiY - SPELL_Y), button, aspectContainer.getHeldAspect(), heldItem);
			if (success) {
				aspectContainer.setHeldAspect(null);
			}
		} else if (button == 1) {
			boolean success = te.spellState.mouseUp((int)(guiX - SPELL_X), (int)(guiY - SPELL_Y), button, aspectContainer.getHeldAspect(), heldItem);
			if (success) {
				aspectContainer.setHeldAspect(null);
			}
		}

		return super.mouseReleased(x, y, button);
	}

	@Override
	protected void renderHoveredTooltip(MatrixStack matrices, int mouseX, int mouseY) {
		for (AspectSlot slot : aspectContainer.getAspectSlots()) {
			if (slot.getInventory().get() != null && slot.visible) {
				if (isMouseOverSlot(mouseX, mouseY, slot)) {
					if (slot.getAspect() != null) {
						slot.description = I18n.format("tooltip.arcana.fociforge."+slot.getAspect().name().toLowerCase());
					}
				}
			}
		}
		super.renderHoveredTooltip(matrices, mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double x, double y, double bars) {
		if (x < this.guiLeft + (WIDTH / 2.0)) {
			int extraRows = (Aspects.getWithoutPrimalsOrSins().size() + ASPECT_H_COUNT - 1) / ASPECT_H_COUNT - ASPECT_V_COUNT;
			this.aspectScroll = (float)(this.aspectScroll - bars / extraRows);
			this.aspectScroll = MathHelper.clamp(this.aspectScroll, 0.0F, 1.0F);
			scrollAspectTo(this.aspectScroll);
			this.refreshSlotVisibility();
		} else {
			int extraRows = FocusItem.DEFAULT_NUMSTYLES - ASPECT_V_COUNT;
			this.fociScroll = (float)(this.fociScroll - bars / extraRows);
			this.fociScroll = MathHelper.clamp(this.fociScroll, 0.0F, 1.0F);
			scrollFociTo(this.fociScroll);
		}
		return true;
	}

	// required for onClose to be called
	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}

	@Override
	public void onClose() {
		te.spellState.exitGui();
		super.onClose();
	}

	public void scrollAspectTo(float pos) {
		List<Aspect> searchAspects = AspectUtils.castContaingAspects();
		int extraRows = (searchAspects.size() + ASPECT_H_COUNT - 1) / ASPECT_H_COUNT - ASPECT_V_COUNT;
		int scroll = Math.max(0, Math.round(pos * extraRows));

		for(int row = 0; row < ASPECT_V_COUNT; row++) {
			for(int col = 0; col < ASPECT_H_COUNT; col++) {
				int slot = row * ASPECT_H_COUNT + col;
				int aspectNum = (scroll + row) * ASPECT_H_COUNT + col;
				if (aspectNum >= 0 && aspectNum < searchAspects.size()) {
					aspectContainer.scrollableSlots.get(slot).setAspect(searchAspects.get(aspectNum));
				} else {
					aspectContainer.scrollableSlots.get(slot).setAspect(Aspects.EMPTY);
				}
			}
		}
	}

	public void scrollFociTo(float pos) {
		int possibleFoci = FocusItem.DEFAULT_NUMSTYLES;

		int extraRows = possibleFoci - FOCI_V_COUNT;
		int scroll = Math.max(0, Math.round(pos * extraRows));

		for (int row = 0; row < FOCI_V_COUNT; row++) {
			int fociNum = scroll + row;
			if (te.focus() != ItemStack.EMPTY && fociNum >= 0 && fociNum < possibleFoci) {
				ItemStack dummyFoci = new ItemStack(ArcanaItems.DEFAULT_FOCUS.get(), 1);
				dummyFoci.getOrCreateTag().putInt("style", fociNum);
				container.fociSlots.get(row).putStack(dummyFoci);
			} else {
				container.fociSlots.get(row).putStack(ItemStack.EMPTY);
			}
		}
	}

	private static boolean slotMatchesSearch(AspectSlot slot, String str) {
		if (str.equals(""))
			return true;
		return AspectUtils.getLocalizedAspectDisplayName(slot.getAspect()).toLowerCase().contains(str.toLowerCase());
	}

	@Override
	protected void init() {
		super.init();
		
		searchWidget = new TextFieldWidget(font, guiLeft + 13, guiTop + 36, 120, 15, ITextComponent.getTextComponentOrEmpty(I18n.format("fociForge.search")));
		searchWidget.setMaxStringLength(30);
		searchWidget.setEnableBackgroundDrawing(false);
		searchWidget.setTextColor(0xFFFFFF);
		searchWidget.setVisible(true);
		searchWidget.setCanLoseFocus(false);
		searchWidget.setFocused2(true);
		children.add(searchWidget);
	}

	protected void refreshSlotVisibility(){
		List<AspectSlot> slots = aspectContainer.getAspectSlots();
		for (AspectSlot slot : slots) {
			slot.visible = slotMatchesSearch(slot, searchWidget.getText());
		}
	}
}
