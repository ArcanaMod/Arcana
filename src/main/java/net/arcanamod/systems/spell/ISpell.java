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
	 * @param data spell data
	 * @param compound extra data
	 * @return this but with defined variables.
	 */
	ISpell build(SpellData data, CompoundNBT compound);

	/**
	 * Core aspect in spell.
	 * @return returns core aspect.
	 */
	Aspect getSpellAspect();

	/**
	 * Returns spell modifiers and returns Cast Aspects that are neat modifiers and are used in combos.
	 * @return Mod 1, Mod 2, Sin Mod, Cast, Cast+
	 */
	SpellData getSpellData();

	/**
	 * Cost of spell in AspectStacks.
	 * @return returns cost of spell.
	 */
	SpellCosts getSpellCosts();

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
