package net.arcanamod.systems.spell.modules.core;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.client.gui.ClientUiUtil;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.client.renderer.ItemRenderer;

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
	public void renderUnderMouse(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating, MatrixStack stack) {
		ClientUiUtil.drawTexturedModalRect(stack, mouseX - getWidth() / 2, mouseY - getHeight() / 2, 208, 0, getWidth(), getHeight());
	}

	@Override
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating, MatrixStack stack) {
		if (startMarked) {
			ClientUiUtil.drawTexturedModalRect(stack, x - getWidth() / 2, y - getHeight() / 2, 192, 0, getWidth(), getHeight());
		}
	}
}
