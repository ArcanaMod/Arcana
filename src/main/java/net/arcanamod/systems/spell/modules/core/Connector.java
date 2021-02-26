package net.arcanamod.systems.spell.modules.core;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.client.renderer.ItemRenderer;
import org.lwjgl.opengl.GL11;

public class Connector extends SpellModule {
	public boolean startMarked = false;
	@Override
	public String getName() {
		return "connector";
	}

	@Override
	public int getInputAmount() {
		return 1;
	}

	@Override
	public boolean canConnect(SpellModule connectingModule, boolean special) {
		return true;
	}

	@Override
	public int getOutputAmount() {
		return 1;
	}

	@Override
	public int getWidth() {
		return 16;
	}

	@Override
	public int getHeight() {
		return 16;
	}

	@Override
	public void renderUnderMouse(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating) {
		UiUtil.drawTexturedModalRect(mouseX - getWidth() / 2, mouseY - getHeight() / 2, 208, 0, getWidth(), getHeight());
	}
}
