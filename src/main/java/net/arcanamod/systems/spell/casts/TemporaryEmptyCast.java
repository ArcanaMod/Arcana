package net.arcanamod.systems.spell.casts;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;

// Temporary class
public class TemporaryEmptyCast implements ICast {

	@Override
	public Aspect getSpellAspect() {
		return Aspects.EMPTY;
	}

	@Override
	public int getSpellDuration() {
		return 0;
	}
	
	public void use(PlayerEntity player, Object sender, Pair<Aspect, Aspect> cast, Action action){
	
	}
	
	public ActionResultType use(PlayerEntity player, Object sender, Action action) {
		return null;
	}
}
