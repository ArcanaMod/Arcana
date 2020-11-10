package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.systems.spell.modules.SpellModule;

public class CastMethodSin extends SpellModule {
	public Aspect aspect;

	@Override
	public int getInputAmount() {
		return 1;
	}

	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return connectingModule instanceof CastMethod;
	}
}
