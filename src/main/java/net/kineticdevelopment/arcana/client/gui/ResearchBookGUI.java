package net.kineticdevelopment.arcana.client.gui;

import com.google.common.collect.Lists;
import net.kineticdevelopment.arcana.client.research.ClientBooks;
import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.ResearchBook;
import net.kineticdevelopment.arcana.core.research.ResearchCategory;
import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;

public class ResearchBookGUI extends GuiScreen{
	
	ResearchBook book;
	List<ResearchCategory> categories;
	ResourceLocation texture;
	int tab = 0;
	
	public static final String SUFFIX = "_menu_gui.png";
	public static final ResourceLocation ARROWS_AND_BASES = new ResourceLocation(Main.MODID, "textures/gui/research/research_bases.png");
	
	private static final int fWidth = 256, fHeight = 230;
	private static final int MAX_PAN = 512;
	
	// drawing helper
	private final Arrows arrows = new Arrows();
	
	// TODO: reset button
	static float xPan = 0;
	static float yPan = 0;
	static float zoom = 1f;
	
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
		drawDefaultBackground();
		GlStateManager.enableBlend();
		
		// draw stuff
		
		// 224x196 viewing area
		int scale = new ScaledResolution(mc).getScaleFactor();
		int x = (width - fWidth) / 2 + 16, y = (height - fHeight) / 2 + 17;
		int visibleWidth = 224, visibleHeight = 196;
		GL11.glScissor(x * scale, y * scale, visibleWidth * scale, visibleHeight * scale);
		GL11.glEnable(GL_SCISSOR_TEST);
		// scissors on
		
		renderBackground();
		renderEntries(partialTicks);
		
		// scissors off
		GL11.glDisable(GL_SCISSOR_TEST);
		
		zLevel = 400;
		renderFrame();
		zLevel = 0;
		renderEntryTooltip(mouseX, mouseY);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.enableBlend();
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
		// TODO: use larger (512^2) textures
		mc.getTextureManager().bindTexture(categories.get(tab).getBg());
		drawModalRectWithCustomSizedTexture((width - fWidth) / 2 + 16, (height - fHeight) / 2 + 17, -xPan / 4f + MAX_PAN, yPan / 4f + MAX_PAN, 224, 196, 256, 256);
	}
	
	private void renderEntries(float partialTicks){
		for(ResearchEntry entry : categories.get(tab).getEntries()){
			PageStyle style = style(entry);
			if(style != PageStyle.NONE){
				// render base
				mc.getTextureManager().bindTexture(ARROWS_AND_BASES);
				int base = base(entry);
				float mult = 1f;
				if(style == PageStyle.IN_PROGRESS)
					mult = (float)abs(sin((Minecraft.getMinecraft().player.ticksExisted + partialTicks) / 5f) * 0.75f) + .25f;
				else if(style == PageStyle.PENDING)
					mult = 0.1f;
				GlStateManager.color(mult, mult, mult, 1f);
				//drawTexturedModalRect((int)((entry.x() * 30 + getXOffset() + 2) * zoom), (int)((entry.y() * 30 + getYOffset() + 2) * zoom), base % 4 * 26, base / 4 * 26, (int)(26 * zoom), (int)(26 * zoom));
				drawScaledCustomSizeModalRect((int)((entry.x() * 30 + getXOffset() + 2) * zoom), (int)((entry.y() * 30 + getYOffset() + 2) * zoom), base % 4 * 26, base / 4 * 26, 26, 26, (int)(26 * zoom), (int)(26 * zoom), 256, 256);
				// render item
				itemRender.zLevel = 50;
				GlStateManager.scale(zoom, zoom, 1f);
				GlStateManager.enableDepth();
				RenderHelper.enableGUIStandardItemLighting();
				GlStateManager.disableLighting();
				itemRender.renderItemAndEffectIntoGUI(new ItemStack(entry.icons().get((mc.player.ticksExisted / 30) % entry.icons().size())), (int)(entry.x() * 30 + getXOffset() + 7), (int)(entry.y() * 30 + getYOffset() + 7));
				GlStateManager.disableLighting();
				GlStateManager.scale(1 / zoom, 1 / zoom, 1f);
				GlStateManager.enableRescaleNormal();
				
				// for every visible parent
				// draw an arrow & arrowhead
				// THIS CODE IS NOT VERY GOOD, BY THE WAY
				// PLEASE BE CAREFUL
				for(ResourceLocation parentKey : entry.parents()){
					ResearchEntry parent = ClientBooks.getEntry(parentKey);
					if(parent != null && parent.category().equals(entry.category()) && style(parent) != PageStyle.NONE){
						mc.getTextureManager().bindTexture(ARROWS_AND_BASES);
						int xdiff = abs(entry.x() - parent.x());
						int ydiff = abs(entry.y() - parent.y());
						// if there is a y-difference or x-difference of 0, draw a line
						if(xdiff == 0 || ydiff == 0)
							if(xdiff == 0){
								arrows.drawVerticalLine(parent.x(), entry.y(), parent.y());
								if(parent.y() > entry.y())
									arrows.drawUpArrow(entry.x(), entry.y() + 1);
								else
									arrows.drawDownArrow(entry.x(), entry.y() - 1);
							}else{
								arrows.drawHorizontalLine(parent.y(), entry.x(), parent.x());
								if(parent.x() > entry.x())
									arrows.drawLeftArrow(entry.x() + 1, entry.y());
								else
									arrows.drawRightArrow(entry.x() - 1, entry.y());
							}
						// if there is a y-difference & x-difference of 1, draw a small curve
						else if(xdiff == 1 && ydiff == 1)
							// from entry's POV
							if(entry.x() > parent.x())
								if(entry.y() > parent.y()){
									arrows.drawLdCurve(entry.x(), parent.y());
									arrows.drawDownArrow(entry.x(), entry.y() - 1);
								}else{
									arrows.drawLuCurve(entry.x(), parent.y());
									arrows.drawUpArrow(entry.x(), entry.y() + 1);
								}
							else{
								if(entry.y() > parent.y()){
									arrows.drawRdCurve(entry.x(), parent.y());
									arrows.drawDownArrow(entry.x(), entry.y() - 1);
								}else{
									arrows.drawRuCurve(entry.x(), parent.y());
									arrows.drawUpArrow(entry.x(), entry.y() + 1);
								}
							}
						// else if there is a y-difference or x-difference of 1, draw a line then small curve
						else if(xdiff == 1 || ydiff == 1)
							// from entry's POV
							if(xdiff == 1){
								arrows.drawVerticalLine(entry.x(), parent.y(), entry.y());
								if(entry.x() > parent.x()){
									if(entry.y() > parent.y()){
										arrows.drawLdCurve(entry.x(), parent.y());
										arrows.drawDownArrow(entry.x(), entry.y() - 1);
									}else{
										arrows.drawLuCurve(entry.x(), parent.y());
										arrows.drawUpArrow(entry.x(), entry.y() + 1);
									}
								}else{
									if(entry.y() > parent.y()){
										arrows.drawRdCurve(entry.x(), parent.y());
										arrows.drawDownArrow(entry.x(), entry.y() - 1);
									}else{
										arrows.drawRuCurve(entry.x(), parent.y());
										arrows.drawUpArrow(entry.x(), entry.y() + 1);
									}
								}
							}else{
								arrows.drawHorizontalLine(parent.y(), parent.x(), entry.x());
								if(entry.x() > parent.x())
									if(entry.y() > parent.y()){
										arrows.drawLdCurve(entry.x(), parent.y());
										arrows.drawDownArrow(entry.x(), entry.y() - 1);
									}else{
										arrows.drawLuCurve(entry.x(), parent.y());
										arrows.drawUpArrow(entry.x(), entry.y() + 1);
									}
								else{
									if(entry.y() > parent.y()){
										arrows.drawRdCurve(entry.x(), parent.y());
										arrows.drawDownArrow(entry.x(), entry.y() - 1);
									}else{
										arrows.drawRuCurve(entry.x(), parent.y());
										arrows.drawUpArrow(entry.x(), entry.y() + 1);
									}
								}
							}
						// else if there is a y-difference & x-difference of 2, draw a large curve
						else if(xdiff == 2 && ydiff == 2){
							// from entry's POV
							if(entry.x() > parent.x())
								if(entry.y() > parent.y()){
									arrows.drawLargeLdCurve(entry.x() - 1, parent.y());
									arrows.drawDownArrow(entry.x(), entry.y() - 1);
								}else{
									arrows.drawLargeLuCurve(entry.x() - 1, parent.y() - 1);
									arrows.drawUpArrow(entry.x(), entry.y() + 1);
								}
							else{
								if(entry.y() > parent.y()){
									arrows.drawLargeRdCurve(entry.x(), parent.y());
									arrows.drawDownArrow(entry.x(), entry.y() - 1);
								}else{
									arrows.drawLargeRuCurve(entry.x(), parent.y() - 1);
									arrows.drawUpArrow(entry.x(), entry.y() + 1);
								}
							}
						}
						// else if there is a y-difference or x-difference of 2, draw a line then large curve
						else if(xdiff == 2 || ydiff == 2)
							// from entry's POV
							if(xdiff == 2){
								arrows.drawVerticalLineMinus1(entry.x(), entry.y(), parent.y());
								if(entry.x() > parent.x()){
									if(entry.y() > parent.y()){
										arrows.drawLargeLdCurve(entry.x() - 1, parent.y());
										arrows.drawDownArrow(entry.x(), entry.y() - 1);
									}else{
										arrows.drawLargeLuCurve(entry.x() - 1, parent.y() - 1);
										arrows.drawUpArrow(entry.x(), entry.y() + 1);
									}
								}else{
									if(entry.y() > parent.y()){
										arrows.drawLargeRdCurve(entry.x(), parent.y());
										arrows.drawDownArrow(entry.x(), entry.y() - 1);
									}else{
										arrows.drawLargeRuCurve(entry.x(), parent.y() - 1);
										arrows.drawUpArrow(entry.x(), entry.y() + 1);
									}
								}
							}else{
								arrows.drawHorizontalLineMinus1(parent.y(), parent.x(), entry.x());
								if(entry.x() > parent.x())
									if(entry.y() > parent.y()){
										arrows.drawLargeLdCurve(entry.x(), parent.y());
										arrows.drawDownArrow(entry.x(), entry.y() - 1);
									}else{
										arrows.drawLargeLuCurve(entry.x(), parent.y());
										arrows.drawUpArrow(entry.x(), entry.y() + 1);
									}
								else{
									if(entry.y() > parent.y()){
										arrows.drawLargeRdCurve(entry.x(), parent.y());
										arrows.drawDownArrow(entry.x(), entry.y() - 1);
									}else{
										arrows.drawLargeRuCurve(entry.x(), parent.y());
										arrows.drawUpArrow(entry.x(), entry.y() + 1);
									}
								}
							}
						// else (x-difference & y-difference > 2), draw a line, then large curve, then line
						else{
							arrows.drawHorizontalLineMinus1(parent.y(), parent.x(), entry.x());
							arrows.drawVerticalLineMinus1(entry.x(), entry.y(), parent.y());
							if(entry.x() > parent.x()){
								if(entry.y() > parent.y()){
									arrows.drawLargeLdCurve(entry.x() - 1, parent.y());
									arrows.drawDownArrow(entry.x(), entry.y() - 1);
								}else{
									arrows.drawLargeLuCurve(entry.x() - 1, parent.y() - 1);
									arrows.drawUpArrow(entry.x(), entry.y() + 1);
								}
							}else{
								if(entry.y() > parent.y()){
									arrows.drawLargeRdCurve(entry.x(), parent.y());
									arrows.drawDownArrow(entry.x(), entry.y() - 1);
								}else{
									arrows.drawLargeRuCurve(entry.x(), parent.y() - 1);
									arrows.drawUpArrow(entry.x(), entry.y() + 1);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private PageStyle style(ResearchEntry entry){
		
		// if the page is at full progress, its complete.
		Researcher r = Researcher.getFrom(mc.player);
		if(r.entryStage(entry) >= entry.sections().size())
			return PageStyle.COMPLETE;
		// if its progress is greater than zero, then its in progress.
		if(r.entryStage(entry) > 0)
			return PageStyle.IN_PROGRESS;
		// if it has no parents *and* the "root" tag, its available to do and in progress.
		if(entry.meta().contains("root") && entry.parents().size() == 0)
			return PageStyle.IN_PROGRESS;
		// if it does not have the "hidden" tag:
		if(!entry.meta().contains("hidden")){
			List<PageStyle> parentStyles = entry.parents().stream().map(ClientBooks::getEntry).map(this::style).collect(Collectors.toList());
			// if all of its parents are complete, it is available to do and in progress.
			if(parentStyles.stream().allMatch(PageStyle.COMPLETE::equals))
				return PageStyle.IN_PROGRESS;
			// if at least one of its parents are in progress, its pending.
			if(parentStyles.stream().anyMatch(PageStyle.IN_PROGRESS::equals))
				return PageStyle.PENDING;
		}
		// otherwise, its invisible
		return PageStyle.NONE;
	}
	
	private int base(ResearchEntry entry){
		int base = 8;
		if(entry.meta().contains("purple_base"))
			base = 0;
		else if(entry.meta().contains("yellow_base"))
			base = 4;
		else if(entry.meta().contains("no_base"))
			return 12;
		
		if(entry.meta().contains("round_base"))
			return base + 1;
		else if(entry.meta().contains("square_base"))
			return base + 2;
		else if(entry.meta().contains("hexagon_base"))
			return base + 3;
		else if(entry.meta().contains("spiky_base"))
			return base;
		return base + 2;
	}
	
	private void renderEntryTooltip(int mouseX, int mouseY){
		for(ResearchEntry entry : categories.get(tab).getEntries()){
			PageStyle style = null;
			if(hovering(entry, mouseX, mouseY) && (style = style(entry)) == PageStyle.COMPLETE || style == PageStyle.IN_PROGRESS){
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
		int x = (int)((entry.x() * 30 + getXOffset() + 2) * zoom);
		int y = (int)((entry.y() * 30 + getYOffset() + 2) * zoom);
		// 224x196 viewing area
		int scrx = (width - fWidth) / 2 + 16, scry = (height - fHeight) / 2 + 17;
		return mouseX >= x && mouseX <= x + (26 * zoom) && mouseY >= y && mouseY <= y + (26 * zoom)
				&& mouseX >= scrx && mouseX <= scrx + 224 && mouseY >= scry && mouseY <= scry + 196;
	}
	
	private void renderFrame(){
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect((width - fWidth) / 2, (height - fHeight) / 2, 0, 0, fWidth, fHeight);
	}
	
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
		// in 1.14, there's a nice _-delta as parameters
		// but here I use Mouse.getD_()
		// TODO: the mouse and panning offset aren't fully in sync, why? --> calculate delta manually
		xPan += Mouse.getDX();
		yPan += Mouse.getDY();
		xPan = min(max(xPan, -MAX_PAN), MAX_PAN);
		yPan = min(max(yPan, -MAX_PAN), MAX_PAN);
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
		for(ResearchEntry entry : categories.get(tab).getEntries()){
			PageStyle style;
			if(hovering(entry, mouseX, mouseY)){
				if(mouseButton != 2){
					if((style = style(entry)) == PageStyle.COMPLETE || style == PageStyle.IN_PROGRESS)
						// left/right (& other) click: open page
						mc.displayGuiScreen(new ResearchEntryGUI(entry));
				}else
					// middle click: try advance
					Connection.sendTryAdvance(entry);
				break;
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	public void handleMouseInput() throws IOException{
		super.handleMouseInput();
		float amnt = 1.5f;
		int scroll = Mouse.getEventDWheel();
		if((scroll < 0 && zoom > 0.125) || (scroll > 0 && zoom < 8))
			zoom *= scroll > 0 ? amnt : 1 / amnt;
	}
	
	protected void actionPerformed(@Nonnull GuiButton button){
		if(button instanceof CategoryButton)
			tab = min(((CategoryButton)button).categoryNum, categories.size());
	}
	
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	protected void keyTyped(char typedChar, int keyCode) throws IOException{
		if(this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)){
			mc.displayGuiScreen(null);
			if(mc.currentScreen == null)
				mc.setIngameFocus();
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	/**
	 * Helper class containing methods to draw segments of lines. Made to avoid cluttering up the namespace.
	 * The lines texture must be bound before calling these methods.
	 */
	private final class Arrows extends Gui{
		
		int gX2SX(int gX){
			return (int)((gX * 30 + getXOffset()) *  zoom);
		}
		
		int gY2SY(int gY){
			return (int)((gY * 30 + getYOffset()) * zoom);
		}
		
		void drawHorizontalSegment(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 104, 0, 30, 30);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY), 104, 0, 30, 30, (int)(30 * zoom), (int)(30 * zoom), 256, 256);
		}
		
		void drawVerticalSegment(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 134, 0, 30, 30);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY), 134, 0, 30, 30, (int)(30 * zoom), (int)(30 * zoom), 256, 256);
		}
		
		void drawHorizontalLine(int y, int startGX, int endGX){
			int temp = startGX;
			// *possibly* swap them
			startGX = Math.min(startGX, endGX);
			endGX = Math.max(endGX, temp);
			// *exclusive*
			for(int j = startGX + 1; j < endGX; j++){
				drawHorizontalSegment(j, y);
			}
		}
		
		void drawVerticalLine(int x, int startGY, int endGY){
			int temp = startGY;
			// *possibly* swap them
			startGY = Math.min(startGY, endGY);
			endGY = Math.max(endGY, temp);
			// *exclusive*
			for(int j = startGY + 1; j < endGY; j++)
				drawVerticalSegment(x, j);
		}
		
		void drawHorizontalLineMinus1(int y, int startGX, int endGX){
			int temp = startGX;
			// take one
			if(startGX > endGX)
				endGX++;
			else
				endGX--;
			// *possibly* swap them
			startGX = min(startGX, endGX);
			endGX = max(endGX, temp);
			// *exclusive*
			for(int j = startGX + 1; j < endGX; j++)
				drawHorizontalSegment(j, y);
		}
		
		void drawVerticalLineMinus1(int x, int startGY, int endGY){
			int temp = startGY;
			// take one
			if(startGY > endGY)
				endGY++;
			else
				endGY--;
			// *possibly* swap them
			startGY = min(startGY, endGY);
			endGY = max(endGY, temp);
			// *exclusive*
			for(int j = startGY + 1; j < endGY; j++)
				drawVerticalSegment(x, j);
		}
		
		void drawLuCurve(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 164, 0, 30, 30);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY), 164, 0, 30, 30, (int)(30 * zoom), (int)(30 * zoom), 256, 256);
		}
		
		void drawRuCurve(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 194, 0, 30, 30);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY), 194, 0, 30, 30, (int)(30 * zoom), (int)(30 * zoom), 256, 256);
		}
		
		void drawLdCurve(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 224, 0, 30, 30);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY), 224, 0, 30, 30, (int)(30 * zoom), (int)(30 * zoom), 256, 256);
		}
		
		void drawRdCurve(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 104, 30, 30, 30);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY), 104, 30, 30, 30, (int)(30 * zoom), (int)(30 * zoom), 256, 256);
		}
		
		void drawLargeLuCurve(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 134, 30, 60, 60);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY), 134, 30, 60, 60, (int)(60 * zoom), (int)(60 * zoom), 256, 256);
		}
		
		void drawLargeRuCurve(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 194, 30, 60, 60);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY), 194, 30, 60, 60, (int)(60 * zoom), (int)(60 * zoom), 256, 256);
		}
		
		void drawLargeLdCurve(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 134, 90, 60, 60);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY), 134, 90, 60, 60, (int)(60 * zoom), (int)(60 * zoom), 256, 256);
		}
		
		void drawLargeRdCurve(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 194, 90, 60, 60);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY), 194, 90, 60, 60, (int)(60 * zoom), (int)(60 * zoom), 256, 256);
		}
		
		void drawDownArrow(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY) + 1, 104, 60, 30, 30);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY) + 1, 104, 60, 30, 30, (int)(30 * zoom), (int)(30 * zoom), 256, 256);
		}
		
		void drawUpArrow(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX), gY2SY(gY) - 1, 104, 120, 30, 30);
			drawScaledCustomSizeModalRect(gX2SX(gX), gY2SY(gY) - 1, 104, 120, 30, 30, (int)(30 * zoom), (int)(30 * zoom), 256, 256);
		}
		
		void drawLeftArrow(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX) - 1, gY2SY(gY), 104, 90, 30, 30);
			drawScaledCustomSizeModalRect(gX2SX(gX) - 1, gY2SY(gY), 104, 90, 30, 30, (int)(30 * zoom), (int)(30 * zoom), 256, 256);
		}
		
		void drawRightArrow(int gX, int gY){
			//drawTexturedModalRect(gX2SX(gX) + 1, gY2SY(gY), 104, 150, 30, 30);
			drawScaledCustomSizeModalRect(gX2SX(gX) + 1, gY2SY(gY), 104, 150, 30, 30, (int)(30 * zoom), (int)(30 * zoom), 256, 256);
		}
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