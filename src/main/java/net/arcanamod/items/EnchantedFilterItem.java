package net.arcanamod.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class EnchantedFilterItem extends Item{
	
	// Alembic efficiency, Pump speck size
	public int efficiencyBoost;
	// Alembic time per distill, Pump speck speed
	public int speedBoost;
	
	public EnchantedFilterItem(Properties properties, int efficiencyBoost, int speedBoost){
		super(properties);
		this.efficiencyBoost = efficiencyBoost;
		this.speedBoost = speedBoost;
	}
	
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag){
		super.addInformation(stack, world, tooltip, flag);
		if(efficiencyBoost != 0)
			tooltip.add(plusMinus("item.arcana.enchanted_filter.efficiency_desc", efficiencyBoost));
		if(speedBoost != 0)
			tooltip.add(plusMinus("item.arcana.enchanted_filter.speed_desc", speedBoost));
	}
	
	private static TextComponent plusMinus(String key, int count){
		if(count >= 0)
			return (TextComponent)new TranslationTextComponent(key, new StringTextComponent(repeat("+", count))).mergeStyle(TextFormatting.GREEN);
		return (TextComponent)new TranslationTextComponent(key, new StringTextComponent(repeat("-", -count))).mergeStyle(TextFormatting.RED);
	}
	
	@Nonnull
	private static String repeat(@Nullable String base, int count){
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < count; i++)
			builder.append(base);
		return builder.toString();
	}
}