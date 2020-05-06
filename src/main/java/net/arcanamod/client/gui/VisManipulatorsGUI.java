package net.arcanamod.client.gui;

import net.arcanamod.Arcana;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class VisManipulatorsGUI extends GuiAspectContainer{// implements VisManipulatorsContainer.ScrollRefreshListener{
	
	public static final int WIDTH = 176;
	public static final int HEIGHT = 256;
	
	private static final ResourceLocation bg = new ResourceLocation(Arcana.MODID, "textures/gui/container/vis_manipulators_temp.png");
	
	int leftScroll = 0, rightScroll = 0;
	
	public VisManipulatorsGUI(Container screenContainer, PlayerInventory inv, ITextComponent titleIn){
		super(screenContainer, inv, titleIn);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
	
	}
	
	/*public VisManipulatorsGUI(VisManipulatorsContainer inventorySlots){
		super(inventorySlots);
		xSize = WIDTH;
		ySize = HEIGHT;
		
		inventorySlots.scroller = this;
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		mc.getTextureManager().bindTexture(bg);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		GlStateManager.disableLighting();
		GlStateManager.color(1f, 1f, 1f, 1f);
		
		String s = I18n.format("item.vis_manipulation_tools.name");
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
		
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(bg);
		
		float leftFrac = maxLeftScroll() > 0 ? leftScroll / (float)maxLeftScroll() : 0;
		drawTexturedModalRect(76, 44 + 73 * leftFrac, 176 + (maxLeftScroll() > 0 ? 0 : 10), 0, 10, 15);
		
		float rightFrac = maxRightScroll() > 0 ? (rightScroll / (float)maxRightScroll()) : 0;
		drawTexturedModalRect(157, 44 + 73 * rightFrac, 176 + (maxRightScroll() > 0 ? 0 : 10), 0, 10, 15);
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	public void initGui(){
		super.initGui();
		Connection.network.sendToServer(new PktRequestAspectSync());
	}
	
	void scroll(boolean right, @Nullable Boolean down){
		if(right){
			int maxScroll = maxRightScroll();
			if(maxScroll < 0)
				maxScroll = 0;
			
			if(down != null)
				if(down){
					if(rightScroll < maxScroll)
						rightScroll++;
				}else if(rightScroll > 0)
					rightScroll--;
			
			if(rightScroll > maxScroll)
				rightScroll = maxScroll;
			
			List<AspectSlot> slots = ((VisManipulatorsContainer)inventorySlots).rightScrollableSlots;
			for(int i = 0; i < slots.size(); i++){
				AspectSlot slot = slots.get(i);
				int yy = i / 3;
				int xx = i % 3;
				int x = 92 + 20 * xx;
				int y = 46 + 21 * (yy - rightScroll);
				slot.x = x;
				slot.y = y;
				slot.visible = yy >= rightScroll && yy < rightScroll + 4;
			}
		}else{
			int maxScroll = maxLeftScroll();
			if(maxScroll < 0)
				maxScroll = 0;
			
			if(down != null)
				if(down){
					if(leftScroll < maxScroll)
						leftScroll++;
				}else if(leftScroll > 0)
					leftScroll--;
			
			if(leftScroll > maxScroll)
				leftScroll = maxScroll;
			
			List<AspectSlot> slots = ((VisManipulatorsContainer)inventorySlots).leftScrollableSlots;
			for(int i = 0; i < slots.size(); i++){
				AspectSlot slot = slots.get(i);
				int yy = i / 3;
				int xx = i % 3;
				int x = 11 + 20 * xx;
				int y = 46 + 21 * (yy - leftScroll);
				slot.x = x;
				slot.y = y;
				slot.visible = yy >= leftScroll && yy < leftScroll + 4;
			}
		}
	}
	
	int maxRightScroll(){
		return (int)Math.ceil(((VisManipulatorsContainer)inventorySlots).rightScrollableSlots.size() / 3f) - 4;
	}
	
	int maxLeftScroll(){
		return (int)Math.ceil(((VisManipulatorsContainer)inventorySlots).leftScrollableSlots.size() / 3f) - 4;
	}
	
	public void handleMouseInput() throws IOException{
		super.handleMouseInput();
		int scroll = Mouse.getEventDWheel();
		int x = (Mouse.getEventX() * width / mc.displayWidth) - guiLeft;
		int y = (height - Mouse.getEventY() * height / this.mc.displayHeight - 1) - guiTop;
		if(scroll != 0)
			if(x >= 8 && y >= 43 && x < 8 + 79 && y < 43 + 90)
				scroll(false, scroll < 0);
			else if(x >= 89 && y >= 43 && x < 89 + 79 && y < 43 + 90)
				scroll(true, scroll < 0);
	}
	
	public boolean isSlotVisible(AspectSlot slot){
		return super.isSlotVisible(slot);
	}
	
	public void refreshScrolling(){
		scroll(true, null);
		scroll(false, null);
	}*/
}