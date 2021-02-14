package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.systems.spell.SpellState;
import net.arcanamod.client.gui.UiUtil;
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
		CompoundNBT compound = new CompoundNBT();
		compound.putInt("x", x);
		compound.putInt("y", y);
		return compound;
	}
}
