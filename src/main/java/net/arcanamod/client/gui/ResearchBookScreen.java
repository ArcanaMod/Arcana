package net.arcanamod.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkModifyPins;
import net.arcanamod.systems.research.*;
import net.arcanamod.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.Math.*;
import static net.arcanamod.client.gui.ClientUiUtil.drawModalRectWithCustomSizedTexture;
import static net.arcanamod.client.gui.ClientUiUtil.drawTexturedModalRect;
import static net.minecraft.util.math.MathHelper.clamp;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;

public class ResearchBookScreen extends Screen {
	ResearchBook book;
	List<ResearchCategory> categories;
	ResourceLocation texture;
	int tab = 0;
	Screen parentScreen;
	List<TooltipButton> tooltipButtons = new ArrayList<>();
	List<PinButton> pinButtons = new ArrayList<>();
	ItemStack sender;

	// public static final String SUFFIX = "_menu_gui.png";
	public static final String SUFFIX_RESIZABLE = "_menu_resizable.png";
	public static final ResourceLocation ARROWS_AND_BASES = new ResourceLocation(Arcana.MODID, "textures/gui/research/research_bases.png");

	private static final int MAX_PAN = 512;
	private static final int ZOOM_MULTIPLIER = 2;

	// drawing helper
	private final Arrows arrows = new Arrows();

	static float xPan = 0;
	static float yPan = 0;
	static float zoom = 0.7f;
	static boolean showZoom = false;

	public ResearchBookScreen(ResearchBook book, Screen parentScreen, ItemStack sender){
		super(new StringTextComponent(""));
		this.sender = sender;
		this.parentScreen = parentScreen;
		this.book = book;
		texture = new ResourceLocation(book.getKey().getNamespace(), "textures/gui/research/" + book.getPrefix() + SUFFIX_RESIZABLE);
		PlayerEntity player = Minecraft.getInstance().player;
		categories = book.getCategories().stream().filter(category -> {
			// has no requirement
			if(category.requirement() == null)
				return true;
			// has an invalid requirement
			ResearchEntry entry = ResearchBooks.getEntry(category.requirement());
			if(entry == null)
				return false;
			// has a valid requirement - check if unlocked
			return Researcher.getFrom(player).entryStage(entry) >= entry.sections().size();
		}).collect(Collectors.toList());
	}

	public float getXOffset(){
		return ((width / 2f) * (1 / zoom)) + (xPan / 2f);
	}

	public float getYOffset(){
		return ((height / 2f) * (1 / zoom)) - (yPan / 2f);
	}

	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks){
		renderBackground(stack);
		RenderSystem.enableBlend();
		super.render(stack, mouseX, mouseY, partialTicks);

		// draw stuff
		// 224x196 viewing area
		int scale = (int)getMinecraft().getMainWindow().getGuiScaleFactor();
		int x = (width - getFrameWidth()) / 2 + 16, y = (height - getFrameHeight()) / 2 + 17;
		int visibleWidth = getFrameWidth() - 32, visibleHeight = getFrameHeight() - 34;
		GL11.glScissor(x * scale, y * scale, visibleWidth * scale, visibleHeight * scale);
		GL11.glEnable(GL_SCISSOR_TEST);
		// scissors on

		renderResearchBackground(stack);
		renderEntries(stack, partialTicks);

		// scissors off
		GL11.glDisable(GL_SCISSOR_TEST);

		setBlitOffset(299);
		renderFrame(stack);
		setBlitOffset(0);
		renderEntryTooltip(stack, mouseX, mouseY);

		tooltipButtons.forEach(pin -> pin.renderAfter(stack, mouseX, mouseY));
		RenderSystem.enableBlend();
	}

	public void init(@Nonnull Minecraft mc, int mouseX, int mouseY){
		super.init(mc, mouseX, mouseY);

		// add buttons
		for(int i = 0, size = categories.size(); i < size; i++){
			ResearchCategory category = categories.get(i);
			CategoryButton categoryButton = new CategoryButton(i, (width - getFrameWidth()) / 2 - 12, 32 + ((height - getFrameHeight()) / 2) + 20 * i, category);
			addButton(categoryButton);
			tooltipButtons.add(categoryButton);
		}

		refreshPinButtons(mc);
	}

	void refreshPinButtons(Minecraft mc){
		tooltipButtons.removeAll(pinButtons);
		buttons.removeAll(pinButtons);
		children.removeAll(pinButtons);
		pinButtons.clear();
		AtomicInteger i = new AtomicInteger();
		Researcher.getFrom(mc.player).getPinned().forEach((entryKey, pins) -> pins.forEach(pin -> {
			// create Pin object
			ResearchEntry entry = ResearchBooks.getEntry(entryKey);
			if(entry != null && entry.category().book().equals(book)){
				PinButton pinButton = new PinButton((width + getFrameWidth()) / 2 + 1, 32 + ((height - getFrameHeight()) / 2) + i.get() * 22, new Pin(entry, pin, mc.world));
				addButton(pinButton);
				tooltipButtons.add(pinButton);
				pinButtons.add(pinButton);
			}
			i.incrementAndGet();
		}));
	}

	private void renderResearchBackground(MatrixStack stack){
		getMinecraft().getTextureManager().bindTexture(categories.get(tab).bg());
		drawModalRectWithCustomSizedTexture(stack, (width - getFrameWidth()) / 2 + 16, (height - getFrameHeight()) / 2 + 17, (-xPan + MAX_PAN) / 4f, (yPan + MAX_PAN) / 4f, getFrameWidth() - 32, getFrameHeight() - 34, (int)(width*1.2f), (int)(width*1.2f));
	}

	private void renderEntries(MatrixStack stack, float partialTicks){
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
				drawTexturedModalRect(stack, (int)((entry.x() * 30 + getXOffset() + 2)), (int)((entry.y() * 30 + getYOffset() + 2)), base % 4 * 26, base / 4 * 26, 26, 26);
				// render texture
				if(entry.icons().size() > 0){
					Icon icon = entry.icons().get((getMinecraft().player.ticksExisted / 30) % entry.icons().size());
					int x = (int)(entry.x() * 30 + getXOffset() + 7);
					int y = (int)(entry.y() * 30 + getYOffset() + 7);
					ClientUiUtil.renderIcon(stack, icon, x, y, 100);
				}

				// for every visible parent
				// draw an arrow & arrowhead
				RenderSystem.enableBlend();
				for(Parent parent : entry.parents()){
					ResearchEntry parentEntry = ResearchBooks.getEntry(parent.getEntry());
					if(parentEntry != null && parent.shouldShowLine() && parentEntry.category().equals(entry.category()) && style(parentEntry) != PageStyle.NONE){
						getMinecraft().getTextureManager().bindTexture(ARROWS_AND_BASES);
						int xdiff = abs(entry.x() - parentEntry.x());
						int ydiff = abs(entry.y() - parentEntry.y());
						// if there is a y-difference or x-difference of 0, draw a line
						if(xdiff == 0 || ydiff == 0)
							if(xdiff == 0){
								arrows.drawVerticalLine(stack, parentEntry.x(), entry.y(), parentEntry.y());
								if(parent.shouldShowArrowhead())
									if(parentEntry.y() > entry.y())
										arrows.drawUpArrow(stack, entry.x(), entry.y() + 1);
									else
										arrows.drawDownArrow(stack, entry.x(), entry.y() - 1);
							}else{
								arrows.drawHorizontalLine(stack, parentEntry.y(), entry.x(), parentEntry.x());
								if(parent.shouldShowArrowhead())
									if(parentEntry.x() > entry.x())
										arrows.drawLeftArrow(stack, entry.x() + 1, entry.y());
									else
										arrows.drawRightArrow(stack, entry.x() - 1, entry.y());
							}
						else{
							boolean unreversed = !(entry.meta().contains("reverse") || parent.shouldReverseLine());
							if(xdiff < 2 || ydiff < 2){
								// from entry's POV
								if(unreversed){
									arrows.drawVerticalLine(stack, entry.x(), parentEntry.y(), entry.y());
									arrows.drawHorizontalLine(stack, parentEntry.y(), parentEntry.x(), entry.x());
									if(entry.x() > parentEntry.x()){
										if(entry.y() > parentEntry.y()){
											arrows.drawLdCurve(stack, entry.x(), parentEntry.y());
											if(parent.shouldShowArrowhead())
												arrows.drawDownArrow(stack, entry.x(), entry.y() - 1);
										}else{
											arrows.drawLuCurve(stack, entry.x(), parentEntry.y());
											if(parent.shouldShowArrowhead())
												arrows.drawUpArrow(stack, entry.x(), entry.y() + 1);
										}
									}else{
										if(entry.y() > parentEntry.y()){
											arrows.drawRdCurve(stack, entry.x(), parentEntry.y());
											if(parent.shouldShowArrowhead())
												arrows.drawDownArrow(stack, entry.x(), entry.y() - 1);
										}else{
											arrows.drawRuCurve(stack, entry.x(), parentEntry.y());
											if(parent.shouldShowArrowhead())
												arrows.drawUpArrow(stack, entry.x(), entry.y() + 1);
										}
									}
									// RDs and RUs are called at the same time (when LDs and LUs should)
								}else{
									arrows.drawHorizontalLine(stack, entry.y(), entry.x(), parentEntry.x());
									arrows.drawVerticalLine(stack, parentEntry.x(), parentEntry.y(), entry.y());
									if(entry.x() > parentEntry.x()){
										if(entry.y() > parentEntry.y()){
											// ru
											arrows.drawRuCurve(stack, parentEntry.x(), entry.y());
											if(parent.shouldShowArrowhead())
												arrows.drawRightArrow(stack, entry.x() - 1, entry.y());
										}else{
											// rd
											arrows.drawRdCurve(stack, entry.x() - 1, parentEntry.y() - 1);
											if(parent.shouldShowArrowhead())
												arrows.drawRightArrow(stack, entry.x() - 1, entry.y());
										}
									}else{
										if(entry.y() > parentEntry.y()){
											// lu
											arrows.drawLuCurve(stack, entry.x() + 1, entry.y());
											if(parent.shouldShowArrowhead())
												arrows.drawLeftArrow(stack, entry.x() + 1, entry.y());
										}else{
											// ld
											arrows.drawLdCurve(stack, entry.x() + 1, parentEntry.y() - 1);
											if(parent.shouldShowArrowhead())
												arrows.drawLeftArrow(stack, entry.x() + 1, entry.y());
										}
									}
								}
							}else{
								if(unreversed){
									arrows.drawHorizontalLineMinus1(stack, parentEntry.y(), parentEntry.x(), entry.x());
									arrows.drawVerticalLineMinus1(stack, entry.x(), entry.y(), parentEntry.y());
									if(entry.x() > parentEntry.x()){
										if(entry.y() > parentEntry.y()){
											arrows.drawLargeLdCurve(stack, entry.x() - 1, parentEntry.y());
											if(parent.shouldShowArrowhead())
												arrows.drawDownArrow(stack, entry.x(), entry.y() - 1);
										}else{
											arrows.drawLargeLuCurve(stack, entry.x() - 1, parentEntry.y() - 1);
											if(parent.shouldShowArrowhead())
												arrows.drawUpArrow(stack, entry.x(), entry.y() + 1);
										}
									}else{
										if(entry.y() > parentEntry.y()){
											arrows.drawLargeRdCurve(stack, entry.x(), parentEntry.y());
											if(parent.shouldShowArrowhead())
												arrows.drawDownArrow(stack, entry.x(), entry.y() - 1);
										}else{
											arrows.drawLargeRuCurve(stack, entry.x(), parentEntry.y() - 1);
											if(parent.shouldShowArrowhead())
												arrows.drawUpArrow(stack, entry.x(), entry.y() + 1);
										}
									}
								}else{
									arrows.drawHorizontalLineMinus1(stack, entry.y(), entry.x(), parentEntry.x());
									arrows.drawVerticalLineMinus1(stack, parentEntry.x(), parentEntry.y(), entry.y());
									if(entry.x() > parentEntry.x()){
										if(entry.y() > parentEntry.y())
											arrows.drawLargeRuCurve(stack, parentEntry.x(), entry.y() - 1);
										else
											arrows.drawLargeRdCurve(stack, parentEntry.x(), entry.y());
										if(parent.shouldShowArrowhead())
											arrows.drawRightArrow(stack, entry.x() - 1, entry.y());
									}else{
										if(entry.y() > parentEntry.y())
											arrows.drawLargeLuCurve(stack, entry.x() + 1, entry.y() - 1);
										else
											arrows.drawLargeLdCurve(stack, entry.x() + 1, entry.y());
										if(parent.shouldShowArrowhead())
											arrows.drawLeftArrow(stack, entry.x() + 1, entry.y());
									}
								}
							}
						}
					}
				}
			}
		}
		RenderSystem.scaled(1 / zoom, 1 / zoom, 1f);
	}

	public PageStyle style(ResearchEntry entry){
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
			List<PageStyle> parentStyles = entry.parents().stream().map(parent -> Pair.of(ResearchBooks.getEntry(parent.getEntry()), parent)).map(p -> parentStyle(p.getFirst(), p.getSecond())).collect(Collectors.toList());
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

	public PageStyle parentStyle(ResearchEntry entry, Parent parent){
		// if the parent is greater than required, consider it complete
		Researcher r = Researcher.getFrom(getMinecraft().player);
		if(parent.getStage() == -1){
			if(r.entryStage(entry) >= entry.sections().size())
				return PageStyle.COMPLETE;
		}else if(r.entryStage(entry) >= parent.getStage())
			return PageStyle.COMPLETE;
		// if its progress is greater than zero, then its in progress.
		if(r.entryStage(entry) > 0)
			return PageStyle.IN_PROGRESS;
		// if it has no parents *and* the "root" tag, its available to do and in progress.
		if(entry.meta().contains("root") && entry.parents().size() == 0)
			return PageStyle.IN_PROGRESS;
		// if it does not have the "hidden" tag:
		if(!entry.meta().contains("hidden")){
			List<PageStyle> parentStyles = entry.parents().stream().map(p -> Pair.of(ResearchBooks.getEntry(p.getEntry()), p)).map(p -> parentStyle(p.getFirst(), p.getSecond())).collect(Collectors.toList());
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

	private void renderEntryTooltip(MatrixStack stack, int mouseX, int mouseY){
		for(ResearchEntry entry : categories.get(tab).entries()){
			PageStyle style = null;
			if(hovering(entry, mouseX, mouseY) && (style = style(entry)) == PageStyle.COMPLETE || style == PageStyle.IN_PROGRESS){
				//tooltip
				List<String> lines = Lists.newArrayList(I18n.format(entry.name()));
				if(entry.description() != null && !entry.description().equals(""))
					lines.add(TextFormatting.GRAY + I18n.format(entry.description()));
				GuiUtils.drawHoveringText(stack, lines.stream().map(StringTextComponent::new).collect(Collectors.toList()), mouseX, mouseY, width, height, -1, getMinecraft().fontRenderer);
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

	private void renderFrame(MatrixStack stack){
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
		drawTexturedModalRect(stack, (x + (width / 2)) - 36, y, 140, 0, 72, 17);
		// draw bottom
		drawTexturedModalRect(stack, (x + (width / 2)) - 36, (y + height) - 18, 140, 17, 72, 18);
		// draw left
		drawTexturedModalRect(stack, x, (y + (height / 2)) - 35, 140, 35, 17, 70);
		// draw right
		drawTexturedModalRect(stack, x + width - 17, (y + (height / 2)) - 35, 157, 35, 17, 70);
		if(showZoom){
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
			return width - 60;
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
		xPan += (deltaX * ZOOM_MULTIPLIER) / zoom;
		yPan -= (deltaY * ZOOM_MULTIPLIER) / zoom;
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
						getMinecraft().displayGuiScreen(new ResearchEntryScreen(entry, this));
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
		if(super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		}
		InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
		if(keyCode == 256 || getMinecraft().gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)){
			onClose();
			Minecraft.getInstance().displayGuiScreen(parentScreen);
			return true;
		}else if(keyCode == 292){
			// F3 pressed
			showZoom = !showZoom;
		}
		return false;
	}
	
	public void onClose(){
		if(sender != null)
			sender.getOrCreateTag().putBoolean("open", false);
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

		void drawHorizontalSegment(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY), 104, 0, 30, 30);
		}

		void drawVerticalSegment(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY), 134, 0, 30, 30);
		}

		void drawHorizontalLine(MatrixStack stack, int y, int startGX, int endGX){
			int temp = startGX;
			// *possibly* swap them
			startGX = min(startGX, endGX);
			endGX = max(endGX, temp);
			// *exclusive*
			for(int j = startGX + 1; j < endGX; j++){
				drawHorizontalSegment(stack, j, y);
			}
		}

		void drawVerticalLine(MatrixStack stack, int x, int startGY, int endGY){
			int temp = startGY;
			// *possibly* swap them
			startGY = min(startGY, endGY);
			endGY = max(endGY, temp);
			// *exclusive*
			for(int j = startGY + 1; j < endGY; j++)
				drawVerticalSegment(stack, x, j);
		}

		void drawHorizontalLineMinus1(MatrixStack stack, int y, int startGX, int endGX){
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
				drawHorizontalSegment(stack, j, y);
		}

		void drawVerticalLineMinus1(MatrixStack stack, int x, int startGY, int endGY){
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
				drawVerticalSegment(stack, x, j);
		}

		void drawLuCurve(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY), 164, 0, 30, 30);
		}

		void drawRuCurve(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY), 194, 0, 30, 30);
		}

		void drawLdCurve(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY), 224, 0, 30, 30);
		}

		void drawRdCurve(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY), 104, 30, 30, 30);
		}

		void drawLargeLuCurve(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY), 134, 30, 60, 60);
		}

		void drawLargeRuCurve(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY), 194, 30, 60, 60);
		}

		void drawLargeLdCurve(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY), 134, 90, 60, 60);
		}

		void drawLargeRdCurve(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY), 194, 90, 60, 60);
		}

		void drawDownArrow(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY) + 1, 104, 60, 30, 30);
		}

		void drawUpArrow(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX), gY2SY(gY) - 1, 104, 120, 30, 30);
		}

		void drawLeftArrow(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX) - 1, gY2SY(gY), 104, 90, 30, 30);
		}

		void drawRightArrow(MatrixStack stack, int gX, int gY){
			drawTexturedModalRect(stack, gX2SX(gX) + 1, gY2SY(gY), 104, 150, 30, 30);
		}
	}

	interface TooltipButton{

		void renderAfter(MatrixStack stack, int mouseX, int mouseY);
	}

	class CategoryButton extends Button implements TooltipButton{

		protected int categoryNum;
		protected ResearchCategory category;

		public CategoryButton(int categoryNum, int x, int y, ResearchCategory category){
			super(x, y, 16, 16, new StringTextComponent(""), button -> {
				if(Minecraft.getInstance().currentScreen instanceof ResearchBookScreen)
					((ResearchBookScreen)Minecraft.getInstance().currentScreen).tab = categoryNum;
			});
			this.categoryNum = categoryNum;
			this.category = category;
			visible = true;
		}

		@ParametersAreNonnullByDefault
		public void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks){
			if(visible){
				RenderSystem.color3f(1, 1, 1);
				RenderHelper.disableStandardItemLighting();

				Minecraft.getInstance().getTextureManager().bindTexture(category.icon());
				drawModalRectWithCustomSizedTexture(stack, x - (categoryNum == tab ? 6 : (isHovered) ? 4 : 0), y, 0, 0, 16, 16, 16, 16);

				isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			}
		}

		public void renderAfter(MatrixStack stack, int mouseX, int mouseY){
			if(isHovered){
				int completion = (category.entries().size() > 0) ? ((category.streamEntries().mapToInt(x -> Researcher.getFrom(getMinecraft().player).entryStage(x) >= x.sections().size() ? 1 : 0).sum() * 100) / category.entries().size()) : 100;
				GuiUtils.drawHoveringText(stack, Lists.newArrayList(new StringTextComponent(I18n.format(category.name()).trim() + " (" + completion + "%)")), mouseX, mouseY, ResearchBookScreen.this.width, ResearchBookScreen.this.height, -1, Minecraft.getInstance().fontRenderer);
			}
		}
	}

	class PinButton extends Button implements TooltipButton {

		Pin pin;

		public PinButton(int x, int y, Pin pin) {
			super(x, y, 18, 18, new StringTextComponent(""), b -> {
				if(Screen.hasControlDown()){
					// unpin
					Researcher from = Researcher.getFrom(Minecraft.getInstance().player);
					List<Integer> pinned = from.getPinned().get(pin.getEntry().key());
					if(pinned != null){
						from.removePinned(pin.getEntry().key(), pin.getStage());
						Connection.sendModifyPins(pin, PkModifyPins.Diff.unpin);
					}
					// and remove this button
					ResearchBookScreen thisScreen = (ResearchBookScreen)Minecraft.getInstance().currentScreen;
					thisScreen.refreshPinButtons(Minecraft.getInstance());
				}else{
					// lets jump over to the specific stage
					// first check if you even have the research
					ResearchEntry entry = pin.getEntry();
					if(entry != null && Researcher.getFrom(Minecraft.getInstance().player).entryStage(entry) >= pin.getStage()){
						ResearchEntryScreen in = new ResearchEntryScreen(entry, Minecraft.getInstance().currentScreen);
						int stageIndex = in.indexOfStage(pin.getStage());
						in.index = stageIndex % 2 == 0 ? stageIndex : stageIndex - 1;
						Minecraft.getInstance().displayGuiScreen(in);
					}
				}
			});
			this.pin = pin;
		}

		public void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
			if(visible){
				RenderSystem.color3f(1, 1, 1);

				int xOffset = isHovered ? 3 : 0;

				getMinecraft().getTextureManager().bindTexture(texture);
				RenderSystem.color4f(1f, 1f, 1f, 1f);
				drawTexturedModalRect(stack, x - 2, y - 1, 6 - xOffset, 140, 34 - (6 - xOffset), 18);

				ClientUiUtil.renderIcon(stack, pin.getIcon(), x + xOffset, y - 1, 0);
			}
		}

		public void renderAfter(MatrixStack stack, int mouseX, int mouseY) {
			isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if(isHovered)
				GuiUtils.drawHoveringText(stack, Lists.newArrayList(new StringTextComponent(pin.getIcon().getStack().getDisplayName().getString()), new StringTextComponent(TextFormatting.AQUA + I18n.format("researchBook.jump_to_pin")), new StringTextComponent(TextFormatting.AQUA + I18n.format("researchEntry.unpin"))), mouseX, mouseY, ResearchBookScreen.this.width, ResearchBookScreen.this.height, -1, Minecraft.getInstance().fontRenderer);
		}
	}
}