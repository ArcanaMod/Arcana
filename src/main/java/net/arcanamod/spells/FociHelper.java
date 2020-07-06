package net.arcanamod.spells;

import net.arcanamod.aspects.Aspects;
import net.arcanamod.spells.effects.ISpellEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

/**
 * Helper for foci
 *
 * @author Merijn
 */
public class FociHelper{
	
	/**
	 * Used to generate a focus item.
	 *
	 * @param skin
	 * 		Skin variant
	 * @param effects
	 * 		Array of {@link ISpellEffect} with the effects
	 * @param power
	 * 		Integer of how strong the effect is
	 * @param core
	 * 		Value of {@link Aspects}
	 * @param name
	 * 		Name of the spell
	 * @return ItemStack with the focus properties.
	 */
	public static ItemStack generateFocus(int skin, ISpellEffect[] effects, int power, Aspects core, String name){
		ItemStack is = new ItemStack(/*ArcanaItems.FOCUS*/Items.PUMPKIN);
		CompoundNBT tag = is.getChildTag("foci");
		StringBuilder sb = new StringBuilder();
		for(ISpellEffect effect : effects)
			sb.append(effect.getName()).append(";");
		
		tag.putString("effects", sb.toString());
		tag.putInt("power", power);
		tag.putString("core", core.toString());
		tag.putString("name", name);
		CompoundNBT newtag = new CompoundNBT();
		newtag.put("foci", tag);
		newtag.putInt("variant", skin);
		
		is.setDisplayName(new StringTextComponent(name));
		is.setTag(newtag);
		
		return is;
	}
}