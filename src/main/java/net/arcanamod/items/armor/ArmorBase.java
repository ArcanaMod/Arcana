package net.arcanamod.items.armor;

import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.IHasModel;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

/**
 * Basic Armor, all Armor should either use this, or extend it
 * <p>
 * I am screaming because it is Armour WITH A U!
 *
 * @author Wilkon
 */

public class ArmorBase extends ArmorItem implements IHasModel{
	public ArmorBase(String name, ArmorMaterial materialIn, int renderIndexIn, EquipmentSlotType equipmentSlotIn){
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
