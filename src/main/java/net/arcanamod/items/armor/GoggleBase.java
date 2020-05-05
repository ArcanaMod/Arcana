package net.arcanamod.items.armor;

import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.GogglePriority;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class GoggleBase extends ArmorItem{
	
	public GogglePriority priority;
	
	public GoggleBase(String name, ArmorMaterial materialIn, int renderIndexIn, GogglePriority priority){
		
		super(materialIn, renderIndexIn, EquipmentSlotType.HEAD);
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Arcana.TAB_ARCANA);
		this.priority = priority;
		ArcanaItems.ITEMS.add(this);
		Arcana.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
}
