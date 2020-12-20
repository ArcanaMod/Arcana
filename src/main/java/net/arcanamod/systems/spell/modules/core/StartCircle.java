package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.StartSpellModule;
import net.minecraft.nbt.CompoundNBT;

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
	public CompoundNBT toNBT() {
		return new CompoundNBT();
	}
}
