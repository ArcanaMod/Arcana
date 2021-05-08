package net.arcanamod.items.armor;

import net.arcanamod.items.ArcanaItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public enum ArcanaArmourMaterials implements IArmorMaterial{
	GOGGLES("arcana:goggles_of_revealing", 5, new int[]{0, 0, 0, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0, Ingredient::fromItems),
	ARCANIUM("arcana:arcanium_armor", 20, new int[]{2, 5, 7, 3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1, () -> Ingredient.fromItems(ArcanaItems.ARCANIUM_INGOT.get())),
	VOID_METAL("arcana:void_metal_armor", 17, new int[]{3, 6, 8, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2, () -> Ingredient.fromItems(ArcanaItems.VOID_METAL_INGOT.get()))
	;
	
	private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
	private final String name;
	private final int maxDamageFactor;
	private final int[] damageReductionAmountArray;
	private final int enchantability;
	private final SoundEvent soundEvent;
	private final float toughness;
	private final LazyValue<Ingredient> repairMaterial;
	
	ArcanaArmourMaterials(String name, int maxDamageFactor, int[] damageReductionAmounts, int enchantability, SoundEvent equipSound, float toughness, Supplier<Ingredient> repairMaterial){
		this.name = name;
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReductionAmounts;
		this.enchantability = enchantability;
		this.soundEvent = equipSound;
		this.toughness = toughness;
		this.repairMaterial = new LazyValue<>(repairMaterial);
	}
	
	public int getDurability(EquipmentSlotType slotIn){
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
	}
	
	public int getDamageReductionAmount(EquipmentSlotType slotIn){
		return damageReductionAmountArray[slotIn.getIndex()];
	}
	
	public int getEnchantability(){
		return enchantability;
	}
	
	@Nonnull
	public SoundEvent getSoundEvent(){
		return soundEvent;
	}
	
	public Ingredient getRepairMaterial(){
		return repairMaterial.getValue();
	}
	
	@Nonnull
	@OnlyIn(Dist.CLIENT)
	public String getName(){
		return name;
	}
	
	public float getToughness(){
		return toughness;
	}
	
	public float getKnockbackResistance(){
		return 0;
	}
}