package net.arcanamod.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Helper class that implements auto-repair functionality, used by void metal tools.
 *
 * @author Luna
 * @see net.arcanamod.items.armor.AutoRepairArmorItem
 * @see net.arcanamod.items.tools.AutoRepairSwordItem
 * @see net.arcanamod.items.tools.AutoRepairShovelItem
 * @see net.arcanamod.items.tools.AutoRepairPickaxeItem
 * @see net.arcanamod.items.tools.AutoRepairHoeItem
 * @see net.arcanamod.items.tools.AutoRepairAxeItem
 */
@ParametersAreNonnullByDefault
public class AutoRepair{
	
	private static final String TAG = "arcana:repair_timer";
	private static final int FULL_TIMER = 70;
	
	public static boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return slotChanged;
	}
	
	public static boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack){
		return newStack.getItem() != oldStack.getItem();
	}
	
	public static void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
		CompoundNBT tag = stack.getOrCreateTag();
		if(!tag.contains(TAG))
			tag.putInt(TAG, FULL_TIMER);
		if(tag.getInt(TAG) > 0)
			tag.putInt(TAG, tag.getInt(TAG) - 1);
		else{
			tag.putInt(TAG, FULL_TIMER);
			stack.damageItem(-1, (LivingEntity)entity, __ -> {
			});
		}
	}
}