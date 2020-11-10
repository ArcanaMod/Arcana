package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.spell.modules.SpellModule;

public class CastCircle extends SpellModule {
	public ICast cast;

	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return true;
	}
}
