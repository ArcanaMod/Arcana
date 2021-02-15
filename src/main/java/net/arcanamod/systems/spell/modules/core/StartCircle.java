package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.systems.spell.SpellState;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.StartSpellModule;

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
		return state.currentSpell.mainModule == this && getBoundModules().size() == 0;
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
	public void renderUnderMouse(int mouseX, int mouseY) {
		UiUtil.drawTexturedModalRect(mouseX - 16, mouseY - 16, 0, 16, 32, 32);
	}

	@Override
	public void renderInMinigame(int mouseX, int mouseY) {
		UiUtil.drawTexturedModalRect(this.x - 16, this.y - 16, 0, 16, 32, 32);
	}
}
