package net.arcanamod.systems.spell.modules.impl;

import net.arcanamod.systems.spell.modules.SpellModule;

public class CastMethodSin extends SpellModule {
	@Override
	public int getInputAmount() {
		return 1;
	}

	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return connectingModule instanceof CastMethod;
	}
}
