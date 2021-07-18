package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.*;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.systems.research.Icon;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.ResearchEntry;
import net.arcanamod.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientUiUtil {

    private static ResourceLocation RESEARCH_EXPERTISE = Arcana.arcLoc("research_expertise");

    public static void renderAspectStack(MatrixStack matricies, AspectStack stack, int x, int y){
        renderAspectStack(matricies, stack, x, y, UiUtil.tooltipColour(stack.getAspect()));
    }

    public static void renderAspectStack(MatrixStack matricies, AspectStack stack, int x, int y, int colour){
        renderAspectStack(matricies, stack.getAspect(), stack.getAmount(), x, y, colour);
    }

    public static void renderAspectStack(MatrixStack stack, Aspect aspect, float amount, int x, int y){
        renderAspectStack(stack, aspect, amount, x, y, UiUtil.tooltipColour(aspect));
    }

    public static void renderAspectStack(MatrixStack stack, Aspect aspect, float amount, int x, int y, int colour){
        Minecraft mc = Minecraft.getInstance();
        // render aspect
        renderAspect(stack, aspect, x, y);
        // render amount
        MatrixStack matrixstack = new MatrixStack();
        // if there is a fractional part, round it
        String s = (amount % 1 > 0) ? String.format("%.1f", amount) : String.format("%.0f", amount);
        matrixstack.translate(0, 0, mc.getItemRenderer().zLevel + 200.0F);
        IRenderTypeBuffer.Impl impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        mc.fontRenderer.renderString(s, x + 19 - mc.fontRenderer.getStringWidth(s), y + 10, colour, true, matrixstack.getLast().getMatrix(), impl, false, 0, 0xf000f0);
        impl.finish();
    }

    public static void renderAspect(MatrixStack stack, Aspect aspect, int x, int y){
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(AspectUtils.getAspectTextureLocation(aspect));
        drawModalRectWithCustomSizedTexture(stack, x, y, 0, 0, 16, 16, 16, 16);
    }

    public static void drawModalRectWithCustomSizedTexture(MatrixStack stack, int x, int y, float texX, float texY, int width, int height, int textureWidth, int textureHeight){
        int z = Minecraft.getInstance().currentScreen != null ? Minecraft.getInstance().currentScreen.getBlitOffset() : 1;
        AbstractGui.blit(stack, x, y, z, texX, texY, width, height, textureWidth, textureHeight);
    }

    public static void drawTexturedModalRect(MatrixStack stack, int x, int y, float texX, float texY, int width, int height){
        drawModalRectWithCustomSizedTexture(stack, x, y, texX, texY, width, height, 256, 256);
    }

    public static boolean shouldShowAspectIngredients(){
        // true if research expertise has been completed
        Researcher from = Researcher.getFrom(Minecraft.getInstance().player);
        ResearchEntry entry = ResearchBooks.getEntry(RESEARCH_EXPERTISE);
        // If the player is null, their researcher is null, or research expertise no longer exists, display anyways
        return entry == null || (from != null && from.entryStage(entry) >= entry.sections().size());
    }

    public static void drawAspectTooltip(MatrixStack stack, Aspect aspect, String descriptions, int mouseX, int mouseY, int screenWidth, int screenHeight){
        String name = AspectUtils.getLocalizedAspectDisplayName(aspect);

        List<ITextComponent> text = new ArrayList<>(); text.add(new StringTextComponent(name));
        if (!descriptions.equals(""))
            for (String description : descriptions.split("\n"))
                text.add(new StringTextComponent(description).setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GRAY))));

        drawAspectStyleTooltip(stack, text, mouseX, mouseY, screenWidth, screenHeight);

        if(shouldShowAspectIngredients() && Screen.hasShiftDown()){
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0, 0, 500);
            RenderSystem.color3f(1, 1, 1);
            Minecraft mc = Minecraft.getInstance();
            RenderSystem.translatef(0, 0, mc.getItemRenderer().zLevel);

            // copied from GuiUtils#drawHoveringText but without text wrapping
            FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
            int tooltipTextWidth = fontRenderer.getStringWidth(name);
            int tooltipX = mouseX + 12;
            if(tooltipX + tooltipTextWidth + 4 > screenWidth)
                tooltipX = mouseX - 16 - tooltipTextWidth;
            int tooltipY = mouseY - 12;
            if (tooltipY < 4)
                tooltipY = 4;
            else if (tooltipY + 12 > screenHeight)
                tooltipY = screenHeight - 12;

            int x = tooltipX - 4;
            int y = 10 + tooltipY + 5;
            Pair<Aspect, Aspect> combinationPairs = Aspects.COMBINATIONS.inverse().get(aspect);
            if(combinationPairs != null){
                int color = 0xa0222222;
                // 2px padding horizontally, 2px padding vertically
                GuiUtils.drawGradientRect(stack.getLast().getMatrix(), 0, x, y - 2, x + 40, y + 18, color, color);
                x += 2;
                renderAspect(stack, combinationPairs.getFirst(), x, y);
                x += 20;
                renderAspect(stack, combinationPairs.getSecond(), x, y);
            }
            RenderSystem.popMatrix();
        }
    }

    public static void drawAspectStyleTooltip(MatrixStack stack, List<ITextComponent> text, int mouseX, int mouseY, int screenWidth, int screenHeight){
        GuiUtils.drawHoveringText(stack, text, mouseX, mouseY, screenWidth, screenHeight, -1, GuiUtils.DEFAULT_BACKGROUND_COLOR, 0xFF00505F, 0xFF00282F, Minecraft.getInstance().fontRenderer);
    }

    public static void renderIcon(MatrixStack stack, Icon icon, int x, int y, int itemZLevel){
        // first, check if its an item
        if(icon.getStack() != null && !icon.getStack().isEmpty()){
            // this, uhh, doesn't work
            // ItemRenderer adds 50 automatically, so we adjust for it
            Minecraft.getInstance().getItemRenderer().zLevel = itemZLevel - 50;
            Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(icon.getStack(), x, y);
        }else{
            // otherwise, check for a texture
            Minecraft.getInstance().getTextureManager().bindTexture(icon.getResourceLocation());
            RenderSystem.enableDepthTest();
            drawModalRectWithCustomSizedTexture(stack, x, y, 0, 0, 16, 16, 16, 16);
        }
    }

    public static void renderVisCore(MatrixStack stack, Core core, int x, int y) {
        Minecraft.getInstance().getTextureManager().bindTexture(core.getGuiTexture());
        drawModalRectWithCustomSizedTexture(stack, x, y, 0, 0, 49, 49, 49, 49);
    }

    public static void renderVisMeter(MatrixStack stack, IAspectHandler aspects, int x, int y) {
        int poolOffset = 2;
        int poolSpacing = 6;
        int poolFromEdge = 24;
        // "2": distance to first vis pool
        // "+= 6": distance between vis pools
        // "24": constant distance to vis pool
        Aspect[] vertical = {Aspects.AIR, Aspects.CHAOS, Aspects.EARTH};
        Aspect[] horizontal = {Aspects.FIRE, Aspects.ORDER, Aspects.WATER};
        int offset = poolOffset;
        for (Aspect aspect : vertical) {
            IAspectHolder holder = aspects.findAspectInHolders(aspect);
            renderVisFill(stack, holder.getContainedAspectStack(), holder.getCapacity(aspect), true, x + offset, y + poolFromEdge);
            offset += poolSpacing;
        }
        offset = poolOffset;
        for (Aspect aspect : horizontal) {
            IAspectHolder holder = aspects.findAspectInHolders(aspect);
            renderVisFill(stack, holder.getContainedAspectStack(), holder.getCapacity(aspect), false, x + poolFromEdge, y + offset);
            offset += poolSpacing;
        }
    }

    public static void renderVisFill(MatrixStack stack, AspectStack aspStack, float visMax, boolean vertical, int x, int y) {
        int meterShort = 3;
        int meterLen = 16;
        int renderLen = (int)((aspStack.getAmount() * meterLen) / visMax);
        if (renderLen > 0) {
            Minecraft.getInstance().getTextureManager().bindTexture(aspStack.getAspect().getVisMeterTexture());
            if (vertical)
                drawModalRectWithCustomSizedTexture(stack, x, y, 0, 0, meterShort, renderLen, meterLen, meterLen);
            else
                drawModalRectWithCustomSizedTexture(stack, x, y, 0, 0, renderLen, meterShort, meterLen, meterLen);
        }
    }

    public static void renderVisDetailInfo(MatrixStack matrices, IAspectHandler aspects) {
        int topMargin = 0;
        for (IAspectHolder holder : aspects.getHolders()){
            Minecraft.getInstance().fontRenderer.drawString(matrices,
                    I18n.format("aspect." + holder.getContainedAspect().name().toLowerCase())+": "+holder.getCurrentVis(),
                    60,topMargin, java.awt.Color.WHITE.getRGB());
            topMargin += 10;
        }
    }
}
