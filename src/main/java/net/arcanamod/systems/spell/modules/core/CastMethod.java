package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.systems.spell.modules.SpellModule;

public class CastMethod extends SpellModule {
	public Aspect aspect;

	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return true;
	}
}
