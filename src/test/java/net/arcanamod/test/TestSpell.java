package net.arcanamod.test;

import net.arcanamod.systems.spell.Spell;
import net.minecraft.nbt.CompoundNBT;
import org.junit.Assert;
import org.junit.Test;

public class TestSpell {
	@Test
	public void testAndCreateSimpleSpell(){
		Assert.assertNotNull(Spell.Samples.createBasicSpell());
	}

	@Test
	public void testAndCreateAdvancedSpell(){
		Assert.assertNotNull(Spell.Samples.createAdvancedSpell());
	}

	@Test
	public void testSpellSerializer(){
		Spell spell = Spell.Samples.createAdvancedSpell();
		Spell.Serializer serializer = new Spell.Serializer();
		CompoundNBT output = serializer.serializeNBT(spell,new CompoundNBT());
		Assert.assertNotNull(output);
	}

	@Test
	public void testSpellDeserializer(){
		Spell spell = Spell.Samples.createAdvancedSpell();
		Spell.Serializer serializer = new Spell.Serializer();
		CompoundNBT input = serializer.serializeNBT(spell,new CompoundNBT());
		serializer = new Spell.Serializer();
		Spell output = serializer.deserializeNBT(input);
		Assert.assertTrue(output==spell);
	}
}
