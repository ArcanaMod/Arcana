package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;

/**
 * With CastAspect you can create Cast Combos
 */
public class CastAspect {
	public Aspect primaryAspect;
	public Aspect comboAspect;

	public CastAspect(Aspect primaryAspect,Aspect comboAspect){
		this.primaryAspect = primaryAspect;
		this.comboAspect = comboAspect;
	}

	public CastAspect(Aspect primaryAspect){
		this(primaryAspect, Aspects.EMPTY);
	}

	public static CastAspect getEmpty(){
		return new CastAspect(Aspects.EMPTY,Aspects.EMPTY);
	}

	public boolean isEmpty() {
		return primaryAspect == Aspects.EMPTY && comboAspect == Aspects.EMPTY;
	}
}
