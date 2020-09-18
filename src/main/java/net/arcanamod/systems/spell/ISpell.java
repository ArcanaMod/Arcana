package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.minecraft.entity.player.PlayerEntity;

/**
 * The ISpell Class
 */
public interface ISpell {

	ISpell build(Aspect[] modAspects, CastAspect[] castAspects, SpellExtraData data);

	Aspect getSpellAspect();
	Aspect[] getModAspects(); // Mod 1, Mod 2, Sin Mod
	CastAspect[] getCastAspects(); // Cast, Cast+

	AspectStack[] getAspectCosts();

	int getComplexity();

	void use(PlayerEntity player, Action action);

	enum Action{
		USE,
		ALT_USE,
		SPECIAL
	}
}
