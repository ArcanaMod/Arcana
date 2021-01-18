package net.arcanamod.systems.spell.modules.core;

import net.minecraft.nbt.CompoundNBT;

public class SplitConnector extends Connector {
	@Override
	public String getName() {
		return "split_connector";
	}

	@Override
	public int getOutputAmount() {
		return 2;
	}

	@Override
	public CompoundNBT toNBT() {
		return new CompoundNBT();
	}
}
