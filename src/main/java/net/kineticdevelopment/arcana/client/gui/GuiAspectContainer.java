package net.kineticdevelopment.arcana.client.gui;

import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.aspects.Aspects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GuiAspectContainer extends GuiContainer{
	
	List<AspectSlot> aspectSlots = new ArrayList<>();
	Aspect heldAspect = null;
	int heldCount = 0;
	
	public GuiAspectContainer(Container inventorySlotsIn){
		super(inventorySlotsIn);
	}
	
	public void initGui(){
		super.initGui();
		initSlots();
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		for(AspectSlot slot : aspectSlots){
			if(slot.inventory.get() != null){
				if(slot.aspect != null)
					itemRender.renderItemAndEffectIntoGUI(Aspects.getItemStackForAspect(slot.aspect), slot.x, slot.y);
				if(isMouseOverSlot(mouseX, mouseY, slot)){
					GlStateManager.disableLighting();
					GuiUtils.drawGradientRect(300, slot.x, slot.y, slot.x + 16, slot.y + 16, 0x60ccfffc, 0x60ccfffc);
				}
				if(slot.aspect != null)
					itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Aspects.getItemStackForAspect(slot.aspect), slot.x - 1, slot.y + 3, String.valueOf(slot.getAmount()));
			}
		}
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		super.drawScreen(mouseX, mouseY, partialTicks);
		if(heldAspect != null){
			float temp = itemRender.zLevel;
			itemRender.zLevel = 500;
			itemRender.renderItemAndEffectIntoGUI(Aspects.getItemStackForAspect(heldAspect), mouseX + 9, mouseY + 4);
			itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Aspects.getItemStackForAspect(heldAspect), mouseX + 9, mouseY + 7, String.valueOf(heldCount));
			itemRender.zLevel = temp;
		}
	}
	
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for(AspectSlot slot : aspectSlots){
			if(slot.inventory.get() != null && isMouseOverSlot(mouseX, mouseY, slot)){
				if(mouseButton == 0 && (heldAspect == null || heldAspect == slot.aspect) && slot.getAmount() > 0){
					heldAspect = slot.aspect;
					int drain = isShiftKeyDown() ? slot.getAmount() : 1;
					heldCount += slot.inventory.get().drain(slot.aspect, drain, false);
					if(slot.getAmount() <= 0 && slot.storeSlot)
						slot.aspect = null;
					slot.markDirty();
				}else if(mouseButton == 1 && heldAspect != null && heldCount > 0 && (slot.aspect == heldAspect || slot.aspect == null)){
					int drain = isShiftKeyDown() ? heldCount : 1;
					if(slot.aspect == null && slot.storeSlot)
						slot.aspect = heldAspect;
					heldCount -= drain - slot.inventory.get().insert(slot.aspect, drain, false);
					if(heldCount <= 0){
						heldCount = 0;
						heldAspect = null;
					}
					slot.markDirty();
				}
				break;
			}
		}
	}
	
	protected boolean isMouseOverSlot(int mouseX, int mouseY, AspectSlot slot){
		return mouseX >= guiLeft + slot.x && mouseY >= guiTop + slot.y && mouseX < guiLeft + slot.x + 16 && mouseY < guiTop + slot.y + 16;
	}
	
	protected abstract void initSlots();
}