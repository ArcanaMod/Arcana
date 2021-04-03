package net.arcanamod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.containers.AspectContainer;
import net.arcanamod.containers.slots.AspectSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

public abstract class AspectContainerScreen<T extends AspectContainer> extends ContainerScreen<T>{
	
	protected T aspectContainer;
	
	public AspectContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn){
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		for(AspectSlot slot : aspectContainer.getAspectSlots())
			if(slot.getInventory().get() != null && slot.visible){
				if(slot.getAspect() != null){
					itemRenderer.renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(slot.getAspect()), slot.x, slot.y);
					itemRenderer.renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, AspectUtils.getItemStackForAspect(slot.getAspect()), slot.x - 1, slot.y + 3, slot.shouldShowAmount() ? String.valueOf(slot.getAmount()) : "");
				}
				if(isMouseOverSlot(mouseX, mouseY, slot)){
					GlStateManager.disableLighting();
					GlStateManager.enableBlend();
					GuiUtils.drawGradientRect(300, slot.x, slot.y, slot.x + 16, slot.y + 16, 0x60ccfffc, 0x60ccfffc);
				}
			}
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY){
		super.renderHoveredToolTip(mouseX, mouseY);
		for(AspectSlot slot : aspectContainer.getAspectSlots())
			if(slot.getInventory().get() != null && slot.visible)
				if(isMouseOverSlot(mouseX, mouseY, slot))
					if(slot.getAspect() != Aspects.EMPTY && slot.getAspect() != null)
						ClientUiUtil.drawAspectTooltip(slot.getAspect(), mouseX, mouseY, width, height);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks){
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		if(aspectContainer.getHeldAspect() != null){
			float temp = itemRenderer.zLevel;
			itemRenderer.zLevel = 500;
			itemRenderer.renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(aspectContainer.getHeldAspect()), mouseX + 9, mouseY + 4);
			itemRenderer.renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, AspectUtils.getItemStackForAspect(aspectContainer.getHeldAspect()), mouseX + 9, mouseY + 7, aspectContainer.isSymbolic() ? "" : String.valueOf(aspectContainer.getHeldCount()));
			itemRenderer.zLevel = temp;
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		aspectContainer.handleClick((int)mouseX, (int)mouseY, mouseButton, this);
		return false;
	}

	protected boolean isMouseOverSlot(int mouseX, int mouseY, AspectSlot slot){
		return mouseX >= guiLeft + slot.x && mouseY >= guiTop + slot.y && mouseX < guiLeft + slot.x + 16 && mouseY < guiTop + slot.y + 16;
	}
	
	public int getGuiLeft(){
		return super.getGuiLeft();
	}
	
	public int getGuiTop(){
		return super.getGuiTop();
	}
	
	public boolean isSlotVisible(AspectSlot slot){
		return slot.visible;
	}
}