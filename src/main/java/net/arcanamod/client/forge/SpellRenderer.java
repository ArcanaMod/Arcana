package net.arcanamod.client.forge;

import net.arcanamod.Arcana;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.systems.spell.Spell;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class SpellRenderer {

    private static final ResourceLocation SPELL_RESOURCES = new ResourceLocation(Arcana.MODID, "textures/gui/container/foci_forge_minigame.png");

    private float x = 0;
    private float y = 0;

    private boolean clickActive = false;

    private void move(float x, float y) {
        this.x += x;
        this.y += y;
    }

    // Return x mod 16 and guarantee negative
    // -15 <= ret <= 0
    private static float toRenderStart(float val) {
        if (val < 0) {
            return val % 16;
        } else {
            return (val % 16) - 16;
        }
    }

    public boolean mouseClicked(double x, double y, int button) {
        clickActive = true;
        return clickActive;
    }


    public void mouseDragged(double x, double y, int button, double move_x, double move_y) {
        move((float)move_x, (float)move_y);
    }

    public void mouseReleased(double x, double y, int button) {

    }

    public void render(Spell spell, int left, int top, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(SPELL_RESOURCES);
        GL11.glPushMatrix();
        GL11.glTranslatef(left, top, 0);

        double gui_scale = mc.getMainWindow().getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int)(gui_scale * left), (int)(gui_scale * (top + 85)), (int)(gui_scale * width), (int)(gui_scale * height));
        int bg_texX = (spell == null ? 16 : 0);

        float start_x = toRenderStart(this.x);
        float start_y = toRenderStart(this.y);
        for (float bg_x = start_x; bg_x < width + 16; bg_x += 16) {
            for (float bg_y = start_y; bg_y < height + 16; bg_y += 16) {
                UiUtil.drawTexturedModalRect((int)Math.floor(bg_x), (int)Math.floor(bg_y), bg_texX, 0, 16, 16);
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }
}
