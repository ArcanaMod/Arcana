package net.arcanamod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.containers.QuaesitumContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.HoverChecker;

@OnlyIn(Dist.CLIENT)
public class QuaesitumScreen extends ContainerScreen<QuaesitumContainer>
{
    public static final ResourceLocation texture = Arcana.arcLoc("textures/gui/container/quaesitum.png");
    private boolean field_214090_m;
    private QuaesitumContainer sc;

    public QuaesitumScreen(QuaesitumContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        sc = screenContainer;
        ySize = 187;
    }

    public void init() {
        super.init();
        this.field_214090_m = this.width < 379;
    }

    public void tick() {
        super.tick();
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        if (this.field_214090_m) {
            this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        } else {
            super.render(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int id)
    {
        int marginHorizontal = (width - xSize) / 2;
        int marginVertical = (height - ySize) / 2;

        if (mouseX - marginHorizontal >= 9 && mouseX - marginHorizontal <= 20)
        {
            if (mouseY - marginVertical >= 9 && mouseY - marginVertical <= 20)
            {
                if (sc.ints.get(1) == 2)
                {
                    sc.ints.set(1, 0);
                } else {
                    sc.ints.set(1, sc.ints.get(1)+1);
                }
                //Networking.INSTANCE.sendToServer(new PacketButtonRedstone(sc.getBlockPos(),sc.world.getDimension().getType().getId()));
            }
        }
        return super.mouseClicked(mouseX, mouseY, id);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.title.getFormattedText();
        this.font.drawString(s, (float)(this.xSize / 2 - this.font.getStringWidth(s) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);

        String redstone_tooltip = "Mode: Ignore Redstone";
        switch (sc.ints.get(1))
        {
            case 0:
                redstone_tooltip = "Mode: Ignore Redstone";
                break;
            case 1:
                redstone_tooltip = "Mode: Redstone off";
                break;
            case 2:
                redstone_tooltip = "Mode: Redstone on";
                break;
        }

        int marginHorizontal = (width - xSize) / 2;
        int marginVertical = (height - ySize) / 2;
        HoverChecker checker = new HoverChecker(marginVertical+9,marginVertical+20,marginHorizontal+9,marginHorizontal+20,0);
        if (checker.checkHover(mouseX,mouseY, true))
        {
            renderTooltip(redstone_tooltip,mouseX-marginHorizontal+4,mouseY-marginVertical+4);
        }
        renderHoveredToolTip(mouseX-marginHorizontal+4,mouseY-marginVertical+4);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(this.texture);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        if (((QuaesitumContainer)this.container).func_217061_l()) {
            int k = ((QuaesitumContainer)this.container).getBurnLeftScaled()*2;
            if (k >= 300)
                k = 299;
            //Z Y T-Z T-Y W H
            this.blit(i + 151, j + 65 + 12 - k, 203, 118 - k, 9, k + 1);
        }
        //int m = ((QuaesitumContainer)this.container).getLiquidScaled();
        //Z Y T-Z T-Y W H
        //this.blit(i + 160, j + 65 + 13 - m, 212, 120 - m, 9, m + 1);

        int marginHorizontal = (width - xSize) / 2;
        int marginVertical = (height - ySize) / 2;
        switch (sc.ints.get(1))
        {
            case 0:
                blit(marginHorizontal+9, marginVertical+9, 176, 128, 12, 12);
                break;
            case 1:
                blit(marginHorizontal+9, marginVertical+9, 176, 141, 12, 12);
                break;
            case 2:
                blit(marginHorizontal+9, marginVertical+9, 176, 154, 12, 12);
                break;
        }
        switch (sc.ints.get(4))
        {
            case 0:
                blit(marginHorizontal+25, marginVertical+9, 192, 128, 12, 12);
                break;
            case 1:
                blit(marginHorizontal+25, marginVertical+9, 192, 141, 12, 12);
                break;
            case 2:
                blit(marginHorizontal+25, marginVertical+9, 192, 154, 12, 12);
                break;
        }

        int l = ((QuaesitumContainer)this.container).getCookProgressionScaled();
        this.blit(i + 53, j + 69, 176, 0, l + 1, 16);
        int n = ((QuaesitumContainer)this.container).getSuccessChangeScaled();
        this.blit(i + 81, j + 28, 176, 40, n + 1, 4);
        int m = ((QuaesitumContainer)this.container).getFailureChangeScaled();
        this.blit(i + 81, j + 40, 176, 40, m + 1, 4);
        int k = ((QuaesitumContainer)this.container).getLossChangeScaled();
        this.blit(i + 81, j + 52, 176, 40, k + 1, 4);
    }
}