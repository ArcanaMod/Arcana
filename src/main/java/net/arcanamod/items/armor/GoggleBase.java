package net.arcanamod.items.armor;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.util.GogglePriority;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import javax.annotation.ParametersAreNonnullByDefault;

public class GoggleBase extends ArmorItem{
	
	public GogglePriority priority;
	public static final IArmorMaterial GOGGLE_MATERIAL = new GogglesArmourMaterial();
	
	public GoggleBase(IArmorMaterial material, Properties properties, GogglePriority priority){
		super(material, EquipmentSlotType.HEAD, properties);
		this.priority = priority;
	}
	
	@ParametersAreNonnullByDefault
	@MethodsReturnNonnullByDefault
	public static class GogglesArmourMaterial implements IArmorMaterial{
		
		public int getDurability(EquipmentSlotType slot){
			return 50;
		}
		
		public int getDamageReductionAmount(EquipmentSlotType slot){
			return 2;
		}
		
		public int getEnchantability(){
			return 10;
		}
		
		public SoundEvent getSoundEvent(){
			return SoundEvents.ITEM_ARMOR_EQUIP_GOLD;
		}
		
		public Ingredient getRepairMaterial(){
			// nothing
			return Ingredient.fromItems();
		}
		
		public String getName(){
			return "arcana:goggles_of_revealing";
		}
		
		public float getToughness(){
			return 0;
		}
	}
}