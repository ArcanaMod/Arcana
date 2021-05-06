package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.Arcana;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScribbledNoteScreen extends Screen {
    
    public static final ResourceLocation SCRIBBLED_NOTE_TEXTURE = new ResourceLocation(Arcana.MODID, "textures/gui/research/scribbled_notes.png");

    public ScribbledNoteScreen(ITextComponent component){
        super(component);
    }
    
    @Override
    public void render(MatrixStack stack, int p_render_1_, int p_render_2_, float p_render_3_){
        String text = I18n.format("scribbledNote.text");
        getMinecraft().fontRenderer.drawString(stack, text, (width - getMinecraft().fontRenderer.getStringWidth(text)) / 2f, height / 2f, 1);
        getMinecraft().getTextureManager().bindTexture(SCRIBBLED_NOTE_TEXTURE);
    }
    
    public boolean isPauseScreen(){
        return false;
    }
}