package net.arcanamod.systems.spell.modules;

import net.arcanamod.systems.spell.modules.core.CastCircle;

public abstract class CircleSpellModule extends SpellModule {
	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return connectingModule instanceof CastCircle;
	}
}
