package net.arcanamod.items.armor;

import net.arcanamod.util.GogglePriority;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;

public class GoggleBase extends ArmorItem{
	
	public GogglePriority priority;
	
	public GoggleBase(IArmorMaterial material, Properties properties, GogglePriority priority){
		super(material, EquipmentSlotType.HEAD, properties);
		this.priority = priority;
	}
}