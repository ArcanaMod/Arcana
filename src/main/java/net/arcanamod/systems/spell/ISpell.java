package net.arcanamod.systems.spell;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

import java.util.Optional;

public interface ISpell{
	/**
	 * Cost of spell in AspectStacks.
	 * @return returns cost of spell.
	 */
	SpellCosts getSpellCosts();

	default Optional<ITextComponent> getName(CompoundNBT nbt){
		return Optional.empty();
	}
}
