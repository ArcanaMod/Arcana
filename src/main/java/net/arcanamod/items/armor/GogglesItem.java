package net.arcanamod.items.armor;

import net.arcanamod.items.settings.GogglePriority;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;

public class GogglesItem extends ArmorItem{
	
	public GogglePriority priority;
	
	public GogglesItem(IArmorMaterial material, Properties properties, GogglePriority priority){
		super(material, EquipmentSlotType.HEAD, properties);
		this.priority = priority;
	}
}