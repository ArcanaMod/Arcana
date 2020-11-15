package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.nbt.CompoundNBT;

public class CastMethod extends SpellModule {
	public Aspect aspect;

	@Override
	public String getName() {
		return "cast_method";
	}

	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return true;
	}

	@Override
	public CompoundNBT toNBT(){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("aspect", AspectUtils.getResourceLocationFromAspect(aspect).toString());
		return compound;
	}
}
