package net.arcanamod.items.tools;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.systems.taint.Taint;
import net.minecraft.block.BlockState;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SilverHoeItem extends HoeItem{
	
	public SilverHoeItem(IItemTier tier, float attackSpeed, Properties builder){
		super(tier, 1, attackSpeed, builder);
	}
	
	public float getDestroySpeed(ItemStack stack, BlockState state){
		float speed = super.getDestroySpeed(stack, state);
		return Taint.isTainted(state.getBlock()) ? speed * 2 : speed;
	}
}