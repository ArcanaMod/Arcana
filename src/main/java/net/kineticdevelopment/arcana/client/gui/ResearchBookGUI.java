package net.kineticdevelopment.arcana.client.gui;

import com.google.common.collect.Lists;
import net.kineticdevelopment.arcana.core.research.ResearchBook;
import net.kineticdevelopment.arcana.core.research.ResearchCategory;
import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;

public class ResearchBookGUI extends GuiScreen{
	
	ResearchBook book;
	List<ResearchCategory> categories;
	ResourceLocation texture;
	int tab = 0;
	
	public static final String SUFFIX = "_menu_gui.png";
	private static final int fWidth = 256, fHeight = 230;
	private static final int MAX_PAN = 512;
	
	static float xPan = 0, yPan = 0;
	
	public ResearchBookGUI(ResearchBook book){
		this.book = book;
		texture = new ResourceLocation(book.getKey().getResourceDomain(), "textures/gui/research/" + book.getPrefix() + SUFFIX);
		categories = book.getCategories();
	}
	
	public float getXOffset(){
		return (width / 2f) + (xPan / 2f);
	}
	
	public float getYOffset(){
		return (height / 2f) - (yPan / 2f);
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		drawWorldBackground(0);
		GlStateManager.enableBlend();
		
		// draw stuff
		
		// 224x196 viewing area
		int scale = new ScaledResolution(mc).getScaleFactor();
		int x = (width - fWidth) / 2 + 16, y = (height - fHeight) / 2 + 17;
		int visibleWidth = 224, visibleHeight = 196;
		GL11.glScissor(x * scale, y * scale, visibleWidth * scale, visibleHeight * scale);
		GL11.glEnable(GL_SCISSOR_TEST);
		
		renderBackground();
		renderEntries();
		
		GL11.glDisable(GL_SCISSOR_TEST);
		
		zLevel = 400;
		renderFrame();
		zLevel = -1;
		renderEntryTooltip(mouseX, mouseY);
		
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
		drawModalRectWithCustomSizedTexture((width - fWidth) / 2 + 16, (height - fHeight) / 2 + 17, -xPan / 4f + MAX_PAN, yPan / 4f + MAX_PAN, 224, 196, 256, 256);
	}
	
	private void renderEntries(){
		for(ResearchEntry entry : categories.get(tab).getEntries()){
			// render base
			itemRender.renderItemAndEffectIntoGUI(new ItemStack(entry.icons().get((mc.player.ticksExisted / 30) % entry.icons().size())), (int)(entry.x() * 30 + getXOffset() + 15), (int)(entry.y() * 30 + getYOffset()) + 15);
			// for every visible parent
				// draw an arrow & arrowhead
		}
	}
	
	private void renderEntryTooltip(int mouseX, int mouseY){
		for(ResearchEntry entry : categories.get(tab).getEntries()){
			if(hovering(entry, mouseX, mouseY)){
				//tooltip
				List<String> lines = Lists.newArrayList(I18n.format(entry.name()));
				if(entry.description() != null && !entry.description().equals(""))
					lines.add(TextFormatting.GRAY + I18n.format(entry.description()));
				GuiUtils.drawHoveringText(lines, mouseX, mouseY, width, height, -1, mc.fontRenderer);
				RenderHelper.disableStandardItemLighting();
				break;
			}
		}
	}
	
	private boolean hovering(ResearchEntry entry, int mouseX, int mouseY){
		int x = (int)(entry.x() * 30 + getXOffset() + 15);
		int y = (int)(entry.y() * 30 + getYOffset() + 15);
		// 224x196 viewing area
		int scrx = (width - fWidth) / 2 + 16, scry = (height - fHeight) / 2 + 17;
		return mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16
				&& mouseX >= scrx && mouseX <= scrx + 224 && mouseY >= scry && mouseY <= scry + 196;
	}
	
	private void renderFrame(){
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect((width - fWidth) / 2, (height - fHeight) / 2, 0, 0, fWidth, fHeight);
	}
	
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
		// in 1.14, there's a nice _-delta as parameters
		// but here I use Mouse.getD_()
		// TODO: the mouse and panning offset aren't fully in sync, why? calculate delta manually?
		xPan += Mouse.getDX();
		yPan += Mouse.getDY();
		xPan = min(max(xPan, -MAX_PAN), MAX_PAN);
		yPan = min(max(yPan, -MAX_PAN), MAX_PAN);
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
		for(ResearchEntry entry : categories.get(tab).getEntries()){
			if(hovering(entry, mouseX, mouseY)){
				// open page
				mc.displayGuiScreen(new ResearchEntryGUI(entry));
				break;
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
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
				drawModalRectWithCustomSizedTexture(x - (hovered ? 5 : 0), y, 0, 0, 16, 16, 16, 16);
				
				this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				if(hovered)
					GuiUtils.drawHoveringText(Lists.newArrayList(I18n.format(category.getName())), mouseX, mouseY, ResearchBookGUI.this.width, ResearchBookGUI.this.height, -1, mc.fontRenderer);
			}
		}
	}
}