package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.tiles.AspectCrystallizerTileEntity;
import net.arcanamod.containers.AspectCrystallizerContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import static net.arcanamod.Arcana.arcLoc;
import static net.arcanamod.client.gui.ClientUiUtil.drawTexturedModalRect;

public class AspectCrystallizerScreen extends ContainerScreen<AspectCrystallizerContainer> {

    private static final ResourceLocation BG = arcLoc("textures/gui/container/aspect_crystallizer.png");

    public AspectCrystallizerScreen(AspectCrystallizerContainer screenContainer, PlayerInventory inv, ITextComponent title) {
        super(screenContainer, inv, title);
    }

    protected void drawGuiContainerBackgroundLayer(MatrixStack matrices, float partialTicks, int mouseX, int mouseY) {
        renderBackground(matrices);
        getMinecraft().getTextureManager().bindTexture(BG);
        drawTexturedModalRect(matrices, guiLeft, guiTop, 0, 0, xSize, ySize);
        Aspect aspect = Aspects.EMPTY;
        int pixHeight = 0;
        int pixProgress = 0;
        if (container.te != null) {
            pixHeight = (int) ((container.te.vis.getHolder(0).getCurrentVis() / 100f) * 52);
            aspect = container.te.vis.getHolder(0).getContainedAspect();
            pixProgress = (int) (((float) container.te.progress / AspectCrystallizerTileEntity.MAX_PROGRESS) * 22);
        }
        int colour = aspect.getColorRange().get(1);
        RenderSystem.color3f(((colour & 0xff0000) >> 16) / 255f, ((colour & 0xff00) >> 8) / 255f, (colour & 0xff) / 255f);
        drawTexturedModalRect(matrices, guiLeft + 48, guiTop + 69 - pixHeight, 176, 64, 16, pixHeight);

        RenderSystem.color3f(1, 1, 1);
        drawTexturedModalRect(matrices, guiLeft + 72, guiTop + 35, 176, 0, pixProgress, 16);
    }

    public void render(MatrixStack matricies, int mouseX, int mouseY, float partialTicks) {
        super.render(matricies, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matricies, mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(MatrixStack matricies, int mouseX, int mouseY) {
        String s = title.getString();
        font.drawString(matricies, s, (float) (xSize / 2 - font.getStringWidth(s) / 2), 6.0F, 4210752);
        font.drawString(matricies, playerInventory.getDisplayName().getString(), 8.0F, (float) (ySize - 96 + 2), 4210752);
    }
}