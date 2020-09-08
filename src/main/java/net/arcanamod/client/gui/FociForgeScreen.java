package net.arcanamod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;

public class FociForgeScreen extends Screen {
	public FociForgeScreen() {
		super(new TranslationTextComponent("screen.fociforge"));
	}

	public void render(int mouseX, int mouseY, float partialTicks){
		renderBackground();
		RenderSystem.enableBlend();

		setBlitOffset(299);
		setBlitOffset(0);

		super.render(mouseX, mouseY, partialTicks);
		RenderSystem.enableBlend();
	}
}
