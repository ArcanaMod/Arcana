package net.arcanamod.items.tools;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.items.AutoRepair;
import net.minecraft.entity.Entity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AutoRepairHoeItem extends HoeItem{
	
	public AutoRepairHoeItem(IItemTier tier, float attackSpeed, Properties builder){
		super(tier, 1, attackSpeed, builder);
	}
	
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return AutoRepair.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
	}
	
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack){
		return AutoRepair.shouldCauseBlockBreakReset(oldStack, newStack);
	}
	
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
		super.inventoryTick(stack, world, entity, itemSlot, isSelected);
		AutoRepair.inventoryTick(stack, world, entity, itemSlot, isSelected);
	}
}