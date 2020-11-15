package net.arcanamod.systems.spell.modules.circle;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.systems.spell.modules.CircleSpellModule;
import net.minecraft.nbt.CompoundNBT;

public class SingleModifierCircle extends CircleSpellModule{
	public Aspect aspect;

	@Override
	public String getName() {
		return "single_modifier_circle";
	}

	@Override
	public CompoundNBT toNBT(){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("aspect", AspectUtils.getResourceLocationFromAspect(aspect).toString());
		return compound;
	}
}
