package net.arcanamod.systems.spell.modules.impl;

import net.arcanamod.systems.spell.modules.SpellModule;

public class CastCircle extends SpellModule {
	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return true;
	}
}
