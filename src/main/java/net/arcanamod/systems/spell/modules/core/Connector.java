package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.client.gui.UiUtil;
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
	public boolean canConnect(SpellModule connectingModule) {
		return true;
	}

	@Override
	public int getOutputAmount() {
		return 1;
	}

	@Override
	public void renderUnderMouse(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating) {
		UiUtil.drawTexturedModalRect(mouseX - getWidth() / 2, mouseY - getHeight() / 2, 0, 16, getWidth(), getHeight());
	}

	@Override
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating) {
		UiUtil.drawTexturedModalRect(x - getWidth() / 2, y - getHeight() / 2, 0, 16, getWidth(), getHeight());
	}
}
