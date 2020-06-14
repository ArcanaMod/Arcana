package net.arcanamod.items.tools;

import net.arcanamod.items.ArcanaItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum ArcanaToolTiers implements IItemTier{
	ARCANIUM(3, 1125, 7.0F, 2.5F, 17, () -> Ingredient.fromItems(ArcanaItems.ARCANIUM_INGOT.get())),
	SILVER(2, 400, 6.5F, 2.5F, 16, () -> Ingredient.fromItems(ArcanaItems.ARCANIUM_INGOT.get())),
	VOID_METAL(3, 300, 8.0F, 3.5F, 10, () -> Ingredient.fromItems(ArcanaItems.VOID_METAL_INGOT.get()));
	//Silver has the correct HarvestLevel and MaxUses right now

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
		return this.maxUses;
	}
	
	public float getEfficiency(){
		return this.efficiency;
	}
	
	public float getAttackDamage(){
		return this.attackDamage;
	}
	
	public int getHarvestLevel(){
		return this.harvestLevel;
	}
	
	public int getEnchantability(){
		return this.enchantability;
	}
	
	public Ingredient getRepairMaterial(){
		return this.repairMaterial.getValue();
	}
}
