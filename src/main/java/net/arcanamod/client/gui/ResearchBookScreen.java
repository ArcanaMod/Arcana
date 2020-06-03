package net.arcanamod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.network.Connection;
import net.arcanamod.research.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;
import static net.minecraft.util.math.MathHelper.clamp;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;

public class ResearchBookScreen extends Screen{
	
	ResearchBook book;
	List<ResearchCategory> categories;
	ResourceLocation texture;
	int tab = 0;
	
	// public static final String SUFFIX = "_menu_gui.png";
	public static final String SUFFIX_RESIZABLE = "_menu_resizable.png";
	public static final ResourceLocation ARROWS_AND_BASES = new ResourceLocation(Arcana.MODID, "textures/gui/research/research_bases.png");
	
	private static final int MAX_PAN = 512;
	private static final int ZOOM_MULT = 2;
	
	// drawing helper
	private final Arrows arrows = new Arrows();
	
	static float xPan = 0;
	static float yPan = 0;
	static float zoom = 0.7f;
	static boolean show_zoom = false;
	
	public ResearchBookScreen(ResearchBook book){
		super(new StringTextComponent(""));
		this.book = book;
		texture = new ResourceLocation(book.getKey().getNamespace(), "textures/gui/research/" + book.getPrefix() + SUFFIX_RESIZABLE);
		PlayerEntity player = Minecraft.getInstance().player;
		categories = book.getCategories().stream().filter(category -> {
			if(category.requirement() == null)
				return true;
			ResearchEntry entry = ResearchBooks.getEntry(category.requirement());
			if(entry == null)
				return false;
			return Researcher.getFrom(player).entryStage(entry) >= entry.sections().size();
		}).collect(Collectors.toList());
	}
	
	public float getXOffset(){
		return ((width / 2f) * (1 / zoom)) + (xPan / 2f);
	}
	
	public float getYOffset(){
		return ((height / 2f) * (1 / zoom)) - (yPan / 2f);
	}
	
	public static void drawModalRectWithCustomSizedTexture(int x, int y, float texX, float texY, int width, int height, int textureWidth, int textureHeight){
		int z = Minecraft.getInstance().currentScreen != null ? Minecraft.getInstance().currentScreen.getBlitOffset() : 1;
		AbstractGui.blit(x, y, z, texX, texY, width, height, textureWidth, textureHeight);
	}
	
	public static void drawTexturedModalRect(int x, int y, float texX, float texY, int width, int height){
		drawModalRectWithCustomSizedTexture(x, y, texX, texY, width, height, 256, 256);
	}
	
	public void render(int mouseX, int mouseY, float partialTicks){
		renderBackground();
		RenderSystem.enableBlend();
		
		// draw stuff
		// 224x196 viewing area
		int scale = (int)getMinecraft().getMainWindow().getGuiScaleFactor();//new ScaledResolution(mc).getScaleFactor();
		int x = (width - getFrameWidth()) / 2 + 16, y = (height - getFrameHeight()) / 2 + 17;
		int visibleWidth = getFrameWidth() - 32, visibleHeight = getFrameHeight() - 34;
		GL11.glScissor(x * scale, y * scale, visibleWidth * scale, visibleHeight * scale);
		GL11.glEnable(GL_SCISSOR_TEST);
		// scissors on
		
		renderResearchBackground();
		renderEntries(partialTicks);
		
		// scissors off
		GL11.glDisable(GL_SCISSOR_TEST);
		
		setBlitOffset(299);
		renderFrame();
		setBlitOffset(0);
		renderEntryTooltip(mouseX, mouseY);
		
		super.render(mouseX, mouseY, partialTicks);
		RenderSystem.enableBlend();
	}
	
	public void init(@Nonnull Minecraft mc, int p_init_2_, int p_init_3_){
		super.init(mc, p_init_2_, p_init_3_);
		
		// add buttons
		for(int i = 0, size = categories.size(); i < size; i++){
			ResearchCategory category = categories.get(i);
			addButton(new CategoryButton(i, (width - getFrameWidth()) / 2 - 12, 32 + ((height - getFrameHeight()) / 2) + 20 * i, category));
		}
	}
	
	
	private void renderResearchBackground(){
		// 224x196 viewing area
		// pans up to (256 - 224) x, (256 - 196) y            
		// which is only 32
		// let's scale is up x2, and also pan with half speed (which is what I'd do anyways) so we get 128 pan
		getMinecraft().getTextureManager().bindTexture(categories.get(tab).bg());
		drawModalRectWithCustomSizedTexture((width - getFrameWidth()) / 2 + 16, (height - getFrameHeight()) / 2 + 17, (-xPan + MAX_PAN) / 4f, (yPan + MAX_PAN) / 4f, getFrameWidth() - 32, getFrameHeight() - 34, 512, 512);
	}
	
	private void renderEntries(float partialTicks){
		RenderSystem.scaled(zoom, zoom, 1f);
		for(ResearchEntry entry : categories.get(tab).entries()){
			PageStyle style = style(entry);
			if(style != PageStyle.NONE){
				// render base
				getMinecraft().getTextureManager().bindTexture(ARROWS_AND_BASES);
				int base = base(entry);
				float mult = 1f;
				if(style == PageStyle.IN_PROGRESS)
					mult = (float)abs(sin((getMinecraft().player.ticksExisted + partialTicks) / 5f) * 0.75f) + .25f;
				else if(style == PageStyle.PENDING)
					mult = 0.1f;
				RenderSystem.color4f(mult, mult, mult, 1f);
				//noinspection IntegerDivisionInFloatingPointContext
				drawTexturedModalRect((int)((entry.x() * 30 + getXOffset() + 2)), (int)((entry.y() * 30 + getYOffset() + 2)), base % 4 * 26, base / 4 * 26, 26, 26);
				// render item
				itemRenderer.zLevel = 50;
				RenderSystem.enableDepthTest();
				RenderHelper.enableStandardItemLighting();
				RenderSystem.disableLighting();
				itemRenderer.renderItemAndEffectIntoGUI(new ItemStack(entry.icons().get((getMinecraft().player.ticksExisted / 30) % entry.icons().size())), (int)(entry.x() * 30 + getXOffset() + 7), (int)(entry.y() * 30 + getYOffset() + 7));
				RenderSystem.disableLighting();
				RenderSystem.enableRescaleNormal();
				
				// for every visible parent
				// draw an arrow & arrowhead
				RenderSystem.enableBlend();
				for(ResourceLocation parentKey : entry.parents()){
					ResearchEntry parent = ResearchBooks.getEntry(parentKey);
					if(parent != null && parent.category().equals(entry.category()) && style(parent) != PageStyle.NONE){
						getMinecraft().getTextureManager().bindTexture(ARROWS_AND_BASES);
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
						else if(xdiff < 2 || ydiff < 2){
							// from entry's POV
							if(!entry.meta().contains("reverse")){
								arrows.drawVerticalLine(entry.x(), parent.y(), entry.y());
								arrows.drawHorizontalLine(parent.y(), parent.x(), entry.x());
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
								arrows.drawHorizontalLine(entry.y(), entry.x(), parent.x());
								arrows.drawVerticalLine(parent.x(), parent.y(), entry.y());
								if(entry.x() > parent.x()){
									if(entry.y() > parent.y())
										arrows.drawRuCurve(parent.x(), entry.y());
									else
										arrows.drawRdCurve(parent.x(), entry.y());
									arrows.drawRightArrow(entry.x() - 1, entry.y());
								}else{
									if(entry.y() > parent.y()){
										arrows.drawRdCurve(entry.x(), parent.y());
										arrows.drawDownArrow(entry.x(), entry.y() - 1);
									}else{
										arrows.drawRuCurve(entry.x(), parent.y());
										arrows.drawUpArrow(entry.x(), entry.y() + 1);
									}
								}
							}
						}else{
							if(!entry.meta().contains("reverse")){
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
							}else{
								arrows.drawHorizontalLineMinus1(entry.y(), entry.x(), parent.x());
								arrows.drawVerticalLineMinus1(parent.x(), parent.y(), entry.y());
								if(entry.x() > parent.x()){
									if(entry.y() > parent.y())
										arrows.drawLargeRuCurve(parent.x(), entry.y() - 1);
									else
										arrows.drawLargeRdCurve(parent.x(), entry.y() - 1);
									arrows.drawRightArrow(entry.x() - 1, entry.y());
								}else{
									if(entry.y() > parent.y())
										arrows.drawLargeLuCurve(entry.x() + 1, entry.y() - 1);
									else
										arrows.drawLargeLdCurve(entry.x() + 1, entry.y());
									arrows.drawLeftArrow(entry.x() + 1, entry.y());
								}
							}
						}
					}
				}
			}
		}
		RenderSystem.scaled(1 / zoom, 1 / zoom, 1f);
	}
	
	
	private PageStyle style(ResearchEntry entry){
		// if the page is at full progress, its complete.
		Researcher r = Researcher.getFrom(getMinecraft().player);
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
			List<PageStyle> parentStyles = entry.parents().stream().map(ResearchBooks::getEntry).map(this::style).collect(Collectors.toList());
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
		for(ResearchEntry entry : categories.get(tab).entries()){
			PageStyle style = null;
			if(hovering(entry, mouseX, mouseY) && (style = style(entry)) == PageStyle.COMPLETE || style == PageStyle.IN_PROGRESS){
				//tooltip
				List<String> lines = Lists.newArrayList(I18n.format(entry.name()));
				if(entry.description() != null && !entry.description().equals(""))
					lines.add(TextFormatting.GRAY + I18n.format(entry.description()));
				GuiUtils.drawHoveringText(lines, mouseX, mouseY, width, height, -1, getMinecraft().fontRenderer);
				RenderHelper.disableStandardItemLighting();
				break;
			}
		}
	}
	
	private boolean hovering(ResearchEntry entry, int mouseX, int mouseY){
		int x = (int)((entry.x() * 30 + getXOffset() + 2) * zoom);
		int y = (int)((entry.y() * 30 + getYOffset() + 2) * zoom);
		int scrx = (width - getFrameWidth()) / 2 + 16, scry = (height - getFrameHeight()) / 2 + 17;
		int visibleWidth = getFrameWidth() - 32, visibleHeight = getFrameHeight() - 34;
		return mouseX >= x && mouseX <= x + (26 * zoom) && mouseY >= y && mouseY <= y + (26 * zoom) && mouseX >= scrx && mouseX <= scrx + visibleWidth && mouseY >= scry && mouseY <= scry + visibleHeight;
	}
	
	private void renderFrame(){
		getMinecraft().getTextureManager().bindTexture(texture);
		RenderSystem.enableBlend();
		// resizable gui!
		// 69x69 corners, 2px sides, then add decorations
		int width = getFrameWidth();
		int height = getFrameHeight();
		int x = (this.width - width) / 2;
		int y = (this.height - height) / 2;
		GuiUtils.drawContinuousTexturedBox(x, y, 0, 0, width, height, 140, 140, 69, getBlitOffset());
		// draw top
		drawTexturedModalRect((x + (width / 2)) - 36, y, 140, 0, 72, 17);
		// draw bottom
		drawTexturedModalRect((x + (width / 2)) - 36, (y + height) - 18, 140, 17, 72, 18);
		// draw left
		drawTexturedModalRect(x, (y + (height / 2)) - 35, 140, 35, 17, 70);
		// draw right
		drawTexturedModalRect(x + width - 17, (y + (height / 2)) - 35, 157, 35, 17, 70);
		if(show_zoom){
			MatrixStack textStack = new MatrixStack();
			textStack.translate(0.0D, 0.0D, 299);
			Matrix4f textLocation = textStack.getLast().getMatrix();
			IRenderTypeBuffer.Impl renderType = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			minecraft.fontRenderer.renderString("Zoom: " + zoom, x + 22, y + 5, -1, false, textLocation, renderType, false, 0, 15728880);
			renderType.finish();
			RenderSystem.enableDepthTest();
		}
	}
	
	private int getFrameWidth(){
		int conf = ArcanaConfig.CUSTOM_BOOK_WIDTH.get();
		if(conf == -1)
			return width - 40;
		else
			return clamp(conf, 220, width);
	}
	
	private int getFrameHeight(){
		int conf = ArcanaConfig.CUSTOM_BOOK_HEIGHT.get();
		if(conf == -1)
			return height - 20;
		else
			return clamp(conf, 220, height);
	}
	
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY){
		xPan += (deltaX * ZOOM_MULT) / zoom;
		yPan -= (deltaY * ZOOM_MULT) / zoom;
		xPan = clamp(xPan, -MAX_PAN, MAX_PAN);
		yPan = clamp(yPan, -MAX_PAN, MAX_PAN);
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int button){
		for(ResearchEntry entry : categories.get(tab).entries()){
			PageStyle style;
			if(hovering(entry, (int)mouseX, (int)mouseY)){
				if(button != 2){
					if((style = style(entry)) == PageStyle.COMPLETE || style == PageStyle.IN_PROGRESS)
						// left/right (& other) click: open page
						getMinecraft().displayGuiScreen(new ResearchEntryScreen(entry));
				}else
					// middle click: try advance
					Connection.sendTryAdvance(entry.key());
				break;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	public boolean mouseScrolled(double mouseX, double mouseY, double scroll){
		float amnt = 1.2f;
		if((scroll < 0 && zoom > 0.5) || (scroll > 0 && zoom < 1))
			zoom *= scroll > 0 ? amnt : 1 / amnt;
		if(zoom > 1f)
			zoom = 1f;
		return super.mouseScrolled(mouseX, mouseY, scroll);
	}
	
	public boolean isPauseScreen(){
		return false;
	}
	
	public boolean keyPressed(int keyCode, int scanCode, int modifiers){
		if(super.keyPressed(keyCode, scanCode, modifiers))
			return true;
		else{
			InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
			if(keyCode == 256 || getMinecraft().gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)){
				getMinecraft().player.closeScreen();
				return true;
			}
			if(keyCode == 292){
				// F3 pressed
				show_zoom = !show_zoom;
			}
			return false;
		}
	}
	
	/**
	 * Helper class containing methods to draw segments of lines. Made to avoid cluttering up the namespace.
	 * The lines texture must be bound before calling these methods.
	 */
	private final class Arrows{
		
		int gX2SX(int gX){
			return (int)((gX * 30 + getXOffset()));
		}
		
		int gY2SY(int gY){
			return (int)((gY * 30 + getYOffset()));
		}
		
		void drawHorizontalSegment(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 104, 0, 30, 30);
		}
		
		void drawVerticalSegment(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 134, 0, 30, 30);
		}
		
		void drawHorizontalLine(int y, int startGX, int endGX){
			int temp = startGX;
			// *possibly* swap them
			startGX = min(startGX, endGX);
			endGX = max(endGX, temp);
			// *exclusive*
			for(int j = startGX + 1; j < endGX; j++){
				drawHorizontalSegment(j, y);
			}
		}
		
		void drawVerticalLine(int x, int startGY, int endGY){
			int temp = startGY;
			// *possibly* swap them
			startGY = min(startGY, endGY);
			endGY = max(endGY, temp);
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
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 164, 0, 30, 30);
		}
		
		void drawRuCurve(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 194, 0, 30, 30);
		}
		
		void drawLdCurve(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 224, 0, 30, 30);
		}
		
		void drawRdCurve(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 104, 30, 30, 30);
		}
		
		void drawLargeLuCurve(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 134, 30, 60, 60);
		}
		
		void drawLargeRuCurve(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 194, 30, 60, 60);
		}
		
		void drawLargeLdCurve(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 134, 90, 60, 60);
		}
		
		void drawLargeRdCurve(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY), 194, 90, 60, 60);
		}
		
		void drawDownArrow(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY) + 1, 104, 60, 30, 30);
		}
		
		void drawUpArrow(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX), gY2SY(gY) - 1, 104, 120, 30, 30);
		}
		
		void drawLeftArrow(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX) - 1, gY2SY(gY), 104, 90, 30, 30);
		}
		
		void drawRightArrow(int gX, int gY){
			drawTexturedModalRect(gX2SX(gX) + 1, gY2SY(gY), 104, 150, 30, 30);
		}
	}
	
	class CategoryButton extends Button{
		
		protected int categoryNum;
		protected ResearchCategory category;
		
		public CategoryButton(int categoryNum, int x, int y, ResearchCategory category){
			super(x, y, 16, 16, "", button -> {
				if(Minecraft.getInstance().currentScreen instanceof ResearchBookScreen)
					((ResearchBookScreen)Minecraft.getInstance().currentScreen).tab = categoryNum;
			});
			this.categoryNum = categoryNum;
			this.category = category;
			visible = true;
		}
		
		@ParametersAreNonnullByDefault
		public void renderButton(int mouseX, int mouseY, float partialTicks){
			if(visible){
				RenderSystem.color3f(1, 1, 1);
				RenderHelper.disableStandardItemLighting();
				
				Minecraft.getInstance().getTextureManager().bindTexture(category.icon());
				drawModalRectWithCustomSizedTexture(x - (categoryNum == tab ? 6 : (isHovered) ? 4 : 0), y, 0, 0, 16, 16, 16, 16);
				
				isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
				if(isHovered)
					GuiUtils.drawHoveringText(Lists.newArrayList(I18n.format(category.name())), mouseX, mouseY, ResearchBookScreen.this.width, ResearchBookScreen.this.height, -1, Minecraft.getInstance().fontRenderer);
			}
		}
	}
}