package net.arcanamod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.containers.AspectContainer;
import net.arcanamod.containers.AspectSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.io.IOException;
import java.util.Arrays;

public abstract class AspectContainerScreen<T extends AspectContainer> extends ContainerScreen<T>{
	
	protected T aspectContainer;
	
	public AspectContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn){
		super(screenContainer, inv, titleIn);
	}
	
	/*public GuiAspectContainer(AspectContainer inventorySlots){
		super(inventorySlots);
		aspectContainer = inventorySlots;
	}*/
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		for(AspectSlot slot : aspectContainer.getAspectSlots()){
			if(slot.getInventory().get() != null && slot.visible){
				if(slot.getAspect() != null)
					itemRenderer.renderItemAndEffectIntoGUI(Aspects.getItemStackForAspect(slot.getAspect()), slot.x, slot.y);
				if(isMouseOverSlot(mouseX, mouseY, slot)){
					GlStateManager.disableLighting();
					GuiUtils.drawGradientRect(300, slot.x, slot.y, slot.x + 16, slot.y + 16, 0x60ccfffc, 0x60ccfffc);
				}
				if(slot.getAspect() != null)
					itemRenderer.renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, Aspects.getItemStackForAspect(slot.getAspect()), slot.x - 1, slot.y + 3, slot.shouldShowAmount() ? String.valueOf(slot.getAmount()) : "");
			}
		}
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY) {
		super.renderHoveredToolTip(mouseX, mouseY);
		for(AspectSlot slot : aspectContainer.getAspectSlots()) {
			if (slot.getInventory().get() != null && slot.visible) {
				if (isMouseOverSlot(mouseX, mouseY, slot)) {
					if (slot!=null) {
						if (slot.getAspect() != Aspect.EMPTY && slot.getAspect() != null) {
							String name = Aspects.getLocalizedAspectDisplayName(slot.getAspect());
							renderTooltip(Arrays.asList(name + ((char) 20)), mouseX, mouseY);
						}
					}
				}
			}
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		if(aspectContainer.getHeldAspect() != null){
			float temp = itemRenderer.zLevel;
			itemRenderer.zLevel = 500;
			itemRenderer.renderItemAndEffectIntoGUI(Aspects.getItemStackForAspect(aspectContainer.getHeldAspect()), mouseX + 9, mouseY + 4);
			itemRenderer.renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, Aspects.getItemStackForAspect(aspectContainer.getHeldAspect()), mouseX + 9, mouseY + 7, String.valueOf(aspectContainer.getHeldCount()));
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