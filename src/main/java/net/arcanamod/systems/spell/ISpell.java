package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

/**
 * The ISpell Class
 */
public interface ISpell {

	/**
	 * Defines all variables. DON'T USE THAT IN REGISTRY!!!
	 * @param modAspects Modifier Aspects
	 * @param castAspects Cast Aspects
	 * @param data extra data
	 * @return this but with defined variables.
	 */
	ISpell build(Aspect[] modAspects, CastAspect[] castAspects, SpellExtraData data);

	/**
	 * Core aspect in spell.
	 * @return returns core aspect.
	 */
	Aspect getSpellAspect();

	/**
	 * Returns spell modifiers.
	 * @return Mod 1, Mod 2, Sin Mod
	 */
	Aspect[] getModAspects();

	/**
	 * Return CastAspects that are "neat" modifiers and are used in combos.
	 * "neat" is mod by Vazkii. Why I'm writing that in documentation???
	 * @return returns CastAspect array.
	 */
	CastAspect[] getCastAspects(); // Cast, Cast+

	/**
	 * Cost of spell in AspectStacks.
	 * @return returns cost of spell.
	 */
	AspectStack[] getAspectCosts();

	/**
	 * How spell is complex to use / create
	 * @return returns spell complexity.
	 */
	int getComplexity();

	int getSpellDuration();

	/**
	 * Use of that spell.
	 * @param player Player
	 * @param action Click Action
	 */
	void use(PlayerEntity player, Action action);

	/**
	 * Click action
	 */
	enum Action{
		USE,
		ALT_USE,
		SPECIAL
	}
}
