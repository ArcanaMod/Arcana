package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.nbt.CompoundNBT;

public class CastCircle extends SpellModule {
	public ICast cast;

	@Override
	public String getName() {
		return "cast_circle";
	}

	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return true;
	}

	@Override
	public CompoundNBT toNBT(){
		CompoundNBT compound = new CompoundNBT();
		if (cast instanceof Cast)
			compound.putString("cast", ((Cast)cast).getId().toString());
		else compound.putString("cast", cast.getSpellAspect().toResourceLocation().toString());
		return compound;
	}
}
