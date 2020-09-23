package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.Optional;

/**
 * The ISpell Class
 */
public interface ISpell {
	
	// TODO: improve docs, remove arrays
	
	/**
	 * Defines all variables. DON'T USE THAT IN REGISTRY!!!
	 * @param modAspects Modifier Aspects
	 * @param castAspects Cast Aspects
	 * @param compound extra data
	 * @return this but with defined variables.
	 */
	ISpell build(List<Aspect> modAspects, List<CastAspect> castAspects, CompoundNBT compound);

	/**
	 * Core aspect in spell.
	 * @return returns core aspect.
	 */
	Aspect getSpellAspect();

	/**
	 * Returns spell modifiers.
	 * @return Mod 1, Mod 2, Sin Mod
	 */
	List<Aspect> getModAspects();

	/**
	 * Return CastAspects that are neat modifiers and are used in combos.
	 * Cast, Cast+
	 * @return returns CastAspect array.
	 */
	List<CastAspect> getCastAspects();

	/**
	 * Cost of spell in AspectStacks.
	 * @return returns cost of spell.
	 */
	List<AspectStack> getAspectCosts();

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
	
	default Optional<ITextComponent> getName(CompoundNBT nbt){
		return Optional.empty();
	}
	
	/**
	 * Click action
	 */
	enum Action{
		USE,
		ALT_USE,
		SPECIAL
	}
}
