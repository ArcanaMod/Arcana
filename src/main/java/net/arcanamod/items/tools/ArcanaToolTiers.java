package net.arcanamod.items.tools;

import net.arcanamod.items.ArcanaItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum ArcanaToolTiers implements IItemTier{
	ARCANIUM(3, 1125, 7.0F, 2.5F, 17, () -> Ingredient.fromItems(ArcanaItems.ARCANIUM_INGOT.get())),
	SILVER(1, 125, 10F, 1F, 5, () -> Ingredient.fromItems(ArcanaItems.ARCANIUM_INGOT.get())),
	VOID_METAL(3, 300, 8.0F, 3.5F, 10, () -> Ingredient.fromItems(ArcanaItems.VOID_METAL_INGOT.get()));

	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final LazyValue<Ingredient> repairMaterial;
	
	ArcanaToolTiers(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial){
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiency = efficiency;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairMaterial = new LazyValue<>(repairMaterial);
	}
	
	public int getMaxUses(){
		return maxUses;
	}
	
	public float getEfficiency(){
		return efficiency;
	}
	
	public float getAttackDamage(){
		return attackDamage;
	}
	
	public int getHarvestLevel(){
		return harvestLevel;
	}
	
	public int getEnchantability(){
		return enchantability;
	}
	
	public Ingredient getRepairMaterial(){
		return repairMaterial.getValue();
	}
}