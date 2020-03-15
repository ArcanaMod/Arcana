package net.kineticdevelopment.arcana.client.model.gui;

import com.google.common.collect.Lists;
import net.kineticdevelopment.arcana.core.research.ResearchBook;
import net.kineticdevelopment.arcana.core.research.ResearchCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ResearchBookGUI extends GuiScreen{
	
	ResearchBook book;
	List<ResearchCategory> categories;
	ResourceLocation texture;
	int tab = 0;
	
	public static final String SUFFIX = "_menu_gui.png";
	private static final int fWidth = 256, fHeight = 230;
	private static final int MAX_PAN = 64;
	
	static float xPan = 0, yPan = 0;
	
	public ResearchBookGUI(ResearchBook book){
		this.book = book;
		texture = new ResourceLocation(book.getKey().getResourceDomain(), "textures/gui/research/" + book.getPrefix() + SUFFIX);
		categories = book.getCategories();
	}
	
	public float getXOffset(){
		return width / 2f + xPan * 4f;
	}
	
	public float getYOffset(){
		return height / 2f + yPan * 4f;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		drawWorldBackground(0);
		GlStateManager.enableBlend();
		
		// draw stuff
		// 224x196 viewing area
		renderBackground();
		renderEntries();
		renderFrame();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.disableBlend();
	}
	
	public void initGui(){
		super.initGui();
		
		// add buttons
		for(int i = 0, size = categories.size(); i < size; i++){
			ResearchCategory category = categories.get(i);
			addButton(new CategoryButton(i, i, (width - fWidth) / 2 - 12, 32 + ((height - fHeight) / 2) + 20 * i, category));
		}
	}
	
	private void renderBackground(){
		// 224x196 viewing area
		// pans up to (256 - 224) x, (256 - 196) y
		// which is only 32
		// let's scale is up x2, and also pan with half speed (which is what I'd do anyways) so we get 128 pan
		// TODO: use larger (512^2) textures so I can pan more
		mc.getTextureManager().bindTexture(categories.get(tab).getBg());
		drawModalRectWithCustomSizedTexture((width - fWidth) / 2 + 16, (height - fHeight) / 2 + 17, xPan + MAX_PAN, yPan + MAX_PAN, 224, 196, 256, 256);
	}
	
	private void renderEntries(){
	
	}
	
	private void renderFrame(){
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect((width - fWidth) / 2, (height - fHeight) / 2, 0, 0, fWidth, fHeight);
	}
	
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
		// in 1.14, there's a nice _-delta as parameters
		// but here I use Mouse.getD_()
		xPan -= Mouse.getDX() / 4f;
		yPan += Mouse.getDY() / 4f;
		xPan = min(max(xPan, -MAX_PAN), MAX_PAN);
		yPan = min(max(yPan, -MAX_PAN), MAX_PAN);
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	protected void actionPerformed(GuiButton button){
		if(button instanceof CategoryButton)
			tab = min(((CategoryButton)button).categoryNum, categories.size());
	}
	
	class CategoryButton extends GuiButton{
		
		protected int categoryNum;
		protected ResearchCategory category;
		
		public CategoryButton(int buttonId, int categoryNum, int x, int y, ResearchCategory category){
			super(buttonId, x, y, 16, 16, "");
			this.categoryNum = categoryNum;
			this.category = category;
			visible = true;
		}
		
		@ParametersAreNonnullByDefault
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks){
			if(visible){
				GlStateManager.color(1, 1, 1, 1);
				RenderHelper.disableStandardItemLighting();
				
				mc.getTextureManager().bindTexture(category.getIcon());
				drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
				
				this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				if(hovered)
					GuiUtils.drawHoveringText(Lists.newArrayList(I18n.format(category.getName())), mouseX, mouseY, ResearchBookGUI.this.width, ResearchBookGUI.this.height, -1, mc.fontRenderer);
			}
		}
	}
}