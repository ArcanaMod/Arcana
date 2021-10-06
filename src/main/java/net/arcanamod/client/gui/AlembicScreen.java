package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.handlers.AspectHolder;
import net.arcanamod.containers.AlembicContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.arcanamod.Arcana.arcLoc;
import static net.arcanamod.client.gui.ClientUiUtil.drawTexturedModalRect;
import static net.arcanamod.client.gui.UiUtil.*;

@ParametersAreNonnullByDefault
public class AlembicScreen extends ContainerScreen<AlembicContainer>{
	
	private static final ResourceLocation BG = arcLoc("textures/gui/container/alembic.png");
	
	public AlembicScreen(AlembicContainer container, PlayerInventory inv, ITextComponent title){
		super(container, inv, title);
		xSize = 176;
		ySize = 222;
	}
	
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrices, float partialTicks, int x, int y){
		renderBackground(matrices);
		getMinecraft().getTextureManager().bindTexture(BG);
		drawTexturedModalRect(matrices, guiLeft, guiTop, 0, 0, xSize, ySize);
		
		// draw cell contents
		matrices.push();
		matrices.translate(guiLeft, guiTop, 0);
		List<AspectHolder> holders = container.te.aspects.getHolders();
		for(int i = 0, size = holders.size(); i < size; i++){
			AspectHolder holder = holders.get(i);
			AspectStack stack = holder.getStack();
			if(!stack.isEmpty()){
				ClientUiUtil.renderAspect(matrices, stack.getAspect(), 52 + 24 * i, 12);
				int colour = stack.getAspect().getColorRange().get(3);
				int dispHeight = (int)(58 * (stack.getAmount() / holder.getCapacity()));
				getMinecraft().getTextureManager().bindTexture(BG);
				RenderSystem.color3f(red(colour) / 255f, green(colour) / 255f, blue(colour) / 255f);
				drawTexturedModalRect(matrices, 53 + 24 * i, 60 + (58 - dispHeight), 196, 58 - dispHeight, 14, dispHeight);
				RenderSystem.color3f(1, 1, 1);
			}
		}
		matrices.pop();
	}
	
	// don't label things
	protected void drawGuiContainerForegroundLayer(MatrixStack matrices, int x, int y){}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks){
		super.render(matrices, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(matrices, mouseX, mouseY);
		List<AspectHolder> holders = container.te.aspects.getHolders();
		for(int i = 0, size = holders.size(); i < size; i++){
			AspectHolder holder = holders.get(i);
			AspectStack stack = holder.getStack();
			if(!stack.isEmpty()){
				int x = guiLeft + 52 + 24 * i, y = guiTop + 12;
				if((mouseX >= x) && (mouseX < (x + 16)) && (mouseY >= y) && (mouseY < (y + 16))){
					ClientUiUtil.drawAspectTooltip(matrices, stack.getAspect(), "", mouseX, mouseY, width, height);
					break;
				}
			}
		}
	}
}