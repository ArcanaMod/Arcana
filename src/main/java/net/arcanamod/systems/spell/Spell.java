package net.arcanamod.systems.spell;

import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import static net.arcanamod.util.Pair.of;
import static net.arcanamod.aspects.AspectUtils.deserializeAspect;

public class Spell {
	public SpellModule mainModule = null;

	public static ISpell deserializeNBT(CompoundNBT compound){
		if (compound.contains("Spell")) {

			return Spells.spellMap.get(new ResourceLocation(compound.getString("Spell"))).build(
					new SpellData(deserializeAspect(compound, "FirstModifier"),
							deserializeAspect(compound, "SecondModifier"),
							deserializeAspect(compound, "SinModifier"),
							of(deserializeAspect(compound, "FirstPrimaryCast"),
									deserializeAspect(compound, "SecondPrimaryCast")),
							of(deserializeAspect(compound, "FirstPlusCast"),
									deserializeAspect(compound, "SecondPlusCast"))
					),
					new CompoundNBT());
		} else return null;
	}

	public static CompoundNBT serializeNBT(ISpell spell){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("Spell",((Cast)spell).getId().toString()); // <-- Hardcoded here, fixes needed.

		compound.putString("FirstModifier", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().firstModifier).toString());
		compound.putString("SecondModifier", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().secondModifier).toString());
		compound.putString("SinModifier", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().sinModifier).toString());

		compound.putString("FirstPrimaryCast", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().primaryCast.getFirst()).toString());
		compound.putString("SecondPrimaryCast", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().primaryCast.getSecond()).toString());

		compound.putString("FirstPlusCast", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().plusCast.getFirst()).toString());
		compound.putString("SecondPlusCast", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().plusCast.getSecond()).toString());

		return compound;
	}
}
