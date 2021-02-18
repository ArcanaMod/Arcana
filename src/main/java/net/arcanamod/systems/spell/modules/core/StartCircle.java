package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.systems.spell.SpellState;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.StartSpellModule;
import net.minecraft.client.renderer.ItemRenderer;

public class StartCircle extends SpellModule implements StartSpellModule {

	@Override
	public String getName() {
		return "start_circle";
	}

	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return true;
	}

	@Override
	public boolean canRaise(SpellState state) {
		return state.currentSpell.mainModule == this
				&& state.isolated.size() == 0
				&& getBoundModules().size() == 0;
	}

	@Override
	public int getHeight() {
		return 32;
	}

	@Override
	public int getWidth() {
		return 32;
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
