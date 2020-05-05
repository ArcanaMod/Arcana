package net.arcanamod.items.armor;

import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.IHasModel;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

/**
 * Basic Armor, all Armor should either use this, or extend it
 * <p>
 * I am screaming because it is Armour WITH A U!
 *
 * @author Wilkon
 */

public class ArmorBase extends ItemArmor implements IHasModel{
	public ArmorBase(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn){
		super(materialIn, renderIndexIn, equipmentSlotIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		
		ArcanaItems.ITEMS.add(this);
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
