package net.kineticdevelopment.arcana.client.gui;

import net.kineticdevelopment.arcana.common.containers.VisManipulatorsContainer;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.aspects.AspectHandler;
import net.kineticdevelopment.arcana.core.aspects.VisBattery;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class VisManipulatorsGUI extends GuiAspectContainer{
	
	public static final int WIDTH = 176;
	public static final int HEIGHT = 256;
	
	private static final ResourceLocation bg = new ResourceLocation(Main.MODID, "textures/gui/container/vis_manipulators_temp.png");
	
	protected VisBattery storeSlots = new VisBattery();
	protected List<AspectSlot> leftScrollableSlots = new ArrayList<>();
	protected List<AspectSlot> rightScrollableSlots = new ArrayList<>();
	
	public VisManipulatorsGUI(Container inventorySlots){
		super(inventorySlots);
		xSize = WIDTH;
		ySize = HEIGHT;
		
		((VisManipulatorsContainer)inventorySlots).addChangeListener(this::refreshAspectSlots);
	}
	
	protected void initSlots(){
		aspectSlots.add(new AspectSlot(null, () -> storeSlots, 44, 147, true));
		aspectSlots.add(new AspectSlot(null, () -> storeSlots, 116, 147, true));
	}
	
	protected void refreshAspectSlots(){
		aspectSlots.subList(3, aspectSlots.size()).clear();
		
		Supplier<AspectHandler> left = () -> AspectHandler.getFrom(inventorySlots.getSlot(0).getStack());
		if(left.get() != null){
			Aspect[] values = left.get().getAllowedAspects().toArray(new Aspect[0]);
			for(int i = 0; i < values.length; i++){
				Aspect aspect = values[i];
				int yy = i / 3;
				int xx = i % 3;
				int x = 11 + 20 * xx;
				int y = 46 + 21 * yy;
				// the scroll wheel handles the rest
				if(yy >= 5)
					break;
				AspectSlot slot = new AspectSlot(aspect, left, x, y);
				aspectSlots.add(slot);
				leftScrollableSlots.add(slot);
			}
		}
		
		Supplier<AspectHandler> right = () -> AspectHandler.getFrom(inventorySlots.getSlot(1).getStack());
		if(right.get() != null){
			Aspect[] values = right.get().getAllowedAspects().toArray(new Aspect[0]);
			for(int i = 0; i < values.length; i++){
				Aspect aspect = values[i];
				int yy = i / 3;
				int xx = i % 3;
				int x = 92 + 20 * xx;
				int y = 46 + 20 * yy;
				// the scroll wheel handles the rest
				if(yy >= 5)
					break;
				AspectSlot slot = new AspectSlot(aspect, right, x, y);
				aspectSlots.add(slot);
				rightScrollableSlots.add(slot);
			}
		}
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		mc.getTextureManager().bindTexture(bg);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String s = I18n.format("item.vis_manipulation_tools.name");
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
}