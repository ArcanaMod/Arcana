package net.arcanamod.client.gui;

import net.arcanamod.Arcana;
import net.arcanamod.client.research.PuzzleRenderer;
import net.arcanamod.containers.AspectSlot;
import net.arcanamod.containers.ResearchTableContainer;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.network.Connection;
import net.arcanamod.network.inventory.PktRequestAspectSync;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.ResearchBooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public class ResearchTableGUI extends GuiAspectContainer{
	
	public static final int WIDTH = 376;
	public static final int HEIGHT = 280;
	
	private static final ResourceLocation BG = new ResourceLocation(Arcana.MODID, "textures/gui/container/gui_researchbook.png");
	private static final ResourceLocation NO_INK = new ResourceLocation(Arcana.MODID, "textures/gui/research/no_ink_overlay.png");
	
	ResearchTableTileEntity te;
	int page = 0;
	
	Button leftArrow, rightArrow;
	
	public ResearchTableGUI(ResearchTableTileEntity te, ResearchTableContainer container){
		super(container);
		this.te = te;
		xSize = WIDTH;
		ySize = HEIGHT;
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		mc.getTextureManager().bindTexture(BG);
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT, 378, 378);
		if(!te.note().isEmpty() && te.note().getItem() == ArcanaItems.RESEARCH_NOTE){
			CompoundNBT compound = te.note().getTagCompound();
			if(compound != null){
				Puzzle puzzle = ResearchBooks.puzzles.get(new ResourceLocation(compound.getString("puzzle")));
				PuzzleRenderer.get(puzzle).render(puzzle, ((ResearchTableContainer)aspectContainer).puzzleSlots, ((ResearchTableContainer)aspectContainer).puzzleItemSlots, width, height, mouseX, mouseY, mc.player);
				if(te.ink().isEmpty()){
					// tell them "no u cant do research without a pen"
					mc.getTextureManager().bindTexture(NO_INK);
					drawModalRectWithCustomSizedTexture(guiLeft + 137, guiTop + 31, 0, 0, 223, 143, 223, 143);
					String noInk = I18n.format("researchTable.ink_needed");
					mc.fontRenderer.drawString(noInk, guiLeft + 141 + (213 - mc.fontRenderer.getStringWidth(noInk)) / 2, guiTop + 35 + (134 - mc.fontRenderer.FONT_HEIGHT) / 2, -1);
				}
			}
		}
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	public void initGui(){
		super.initGui();
		
		leftArrow = addButton(new ChangeAspectPageButton(0, 11, 183, false));
		rightArrow = addButton(new ChangeAspectPageButton(0, 112, 183, true));
		
		Connection.network.sendToServer(new PktRequestAspectSync());
	}
	
	protected void actionPerformed(@Nonnull Button button) throws IOException{
		super.actionPerformed(button);
		if(button == leftArrow && page > 0)
			page--;
		ResearchTableContainer container = (ResearchTableContainer)aspectContainer;
		if(button == rightArrow && container.scrollableSlots.size() > 30 * (page + 1))
			page++;
		
		refreshSlotVisibility();
	}
	
	protected void refreshSlotVisibility(){
		ResearchTableContainer container = (ResearchTableContainer)aspectContainer;
		List<AspectSlot> slots = container.scrollableSlots;
		for(int i = 0; i < slots.size(); i++){
			AspectSlot slot = slots.get(i);
			slot.visible = i >= 30 * page && i < 30 * (page + 1);
		}
	}
	
	class ChangeAspectPageButton extends Button{
		
		boolean right;
		
		public ChangeAspectPageButton(int buttonId, int x, int y, boolean right){
			super(buttonId, x, y, 15, 11, "");
			this.right = right;
		}
		
		public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks){
			if(visible){
				hovered = mouseX >= guiLeft + x && mouseY >= guiTop + y && mouseX < guiLeft + x + width && mouseY < guiTop + y + height;
				int teX = right ? 120 : 135;
				int teY = 307;
				ResearchTableContainer container = (ResearchTableContainer)aspectContainer;
				// first check if there are multiple pages
				if(container.scrollableSlots.size() > 30)
					if(right){
						// if I am not on the last page
						if(container.scrollableSlots.size() > 30 * (page + 1)){
							teY -= 11;
							if(hovered)
								teY -= 11;
						}
					}else{
						// if I am not on the first page
						if(page > 0){
							teY -= 11;
							if(hovered)
								teY -= 11;
						}
					}
				// then just draw
				mc.getTextureManager().bindTexture(BG);
				GlStateManager.disableLighting();
				GlStateManager.color(1f, 1f, 1f);
				drawModalRectWithCustomSizedTexture(guiLeft + x, guiTop + y, teX, teY, width, height, 378, 378);
			}
		}
		
		public boolean mousePressed(@Nonnull Minecraft mc, int mouseX, int mouseY){
			return mouseX >= guiLeft + x && mouseY >= guiTop + y && mouseX < guiLeft + x + width && mouseY < guiTop + y + height;
		}
	}
}