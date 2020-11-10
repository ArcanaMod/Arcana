package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.StartSpellModule;

public class StartCircle extends SpellModule implements StartSpellModule {
	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return true;
	}
}
