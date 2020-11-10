package net.arcanamod.systems.spell.casts;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.systems.spell.SpellCosts;
import net.arcanamod.systems.spell.SpellData;
import net.arcanamod.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

import java.util.Optional;

/**
 * The ISpell Class
 */
public interface ICast {
	
	// TODO: improve docs, remove arrays
	
	/**
	 * Defines all variables. DON'T USE THAT IN REGISTRY!!!
	 * @param compound extra data
	 * @return this but with defined variables.
	 */
	ICast build(CompoundNBT compound);

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
	 * @param sender Sender
	 * @param action Click Action
	 * @return
	 */
	void use(PlayerEntity player, Object sender, Pair<Aspect,Aspect> cast, ICast.Action action);
	
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
