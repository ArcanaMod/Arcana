package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.nbt.CompoundNBT;

public class CommentBlock extends SpellModule {

	public String comment = "";

	@Override
	public String getName() {
		return "comment";
	}

	@Override
	public int getInputAmount() {
		return 0;
	}

	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return false;
	}

	@Override
	public int getOutputAmount() {
		return 0;
	}

	@Override
	public CompoundNBT toNBT() {
		CompoundNBT ret = new CompoundNBT();
		ret.putString("comment", comment);
		return ret;
	}
}
