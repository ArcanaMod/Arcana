package net.arcanamod.items.armor;

import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.GogglePriority;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class GoggleBase extends ItemArmor{
	
	public GogglePriority priority;
	
	public GoggleBase(String name, ArmorMaterial materialIn, int renderIndexIn, GogglePriority priority){
		
		super(materialIn, renderIndexIn, EntityEquipmentSlot.HEAD);
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Arcana.TAB_ARCANA);
		this.priority = priority;
		ArcanaItems.ITEMS.add(this);
		Arcana.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
}
