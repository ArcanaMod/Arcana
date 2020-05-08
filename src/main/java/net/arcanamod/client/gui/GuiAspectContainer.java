package net.arcanamod.client.gui;

import net.arcanamod.containers.AspectContainer;
import net.arcanamod.containers.AspectSlot;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

import java.io.IOException;

public abstract class GuiAspectContainer extends ContainerScreen{
	
	protected AspectContainer aspectContainer;
	
	public GuiAspectContainer(Container screenContainer, PlayerInventory inv, ITextComponent titleIn){
		super(screenContainer, inv, titleIn);
	}
	
	/*public GuiAspectContainer(AspectContainer inventorySlots){
		super(inventorySlots);
		aspectContainer = inventorySlots;
	}*/
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		/*super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		for(AspectSlot slot : aspectContainer.getAspectSlots()){
			if(slot.getInventory().get() != null && slot.visible){
				if(slot.getAspect() != null)
					itemRender.renderItemAndEffectIntoGUI(Aspects.getItemStackForAspect(slot.getAspect()), slot.x, slot.y);
				if(isMouseOverSlot(mouseX, mouseY, slot)){
					GlStateManager.disableLighting();
					GuiUtils.drawGradientRect(300, slot.x, slot.y, slot.x + 16, slot.y + 16, 0x60ccfffc, 0x60ccfffc);
				}
				if(slot.getAspect() != null)
					itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Aspects.getItemStackForAspect(slot.getAspect()), slot.x - 1, slot.y + 3, slot.shouldShowAmount() ? String.valueOf(slot.getAmount()) : "");
			}
		}*/
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		/*super.drawScreen(mouseX, mouseY, partialTicks);
		if(aspectContainer.getHeldAspect() != null){
			float temp = itemRender.zLevel;
			itemRender.zLevel = 500;
			itemRender.renderItemAndEffectIntoGUI(Aspects.getItemStackForAspect(aspectContainer.getHeldAspect()), mouseX + 9, mouseY + 4);
			itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Aspects.getItemStackForAspect(aspectContainer.getHeldAspect()), mouseX + 9, mouseY + 7, String.valueOf(aspectContainer.getHeldCount()));
			itemRender.zLevel = temp;
		}*/
	}
	
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		aspectContainer.handleClick(mouseX, mouseY, mouseButton, this);
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