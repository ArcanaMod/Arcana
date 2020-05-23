package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.items.attachment.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

// DynamicBucketModel
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WandItem extends Item{
	
	public WandItem(Properties properties){
		super(properties);
	}
	
	public static Cap getCap(ItemStack stack){
		int cap = stack.getOrCreateTag().getInt("cap");
		return Cap.getCapOrError(cap);
	}
	
	public static Focus getFocus(ItemStack stack){
		int cap = stack.getOrCreateTag().getInt("focus");
		return Focus.getFocusById(cap).orElse(ArcanaItems.ERROR_FOCUS);
	}
	
	public static Core getCore(ItemStack stack){
		int cap = stack.getOrCreateTag().getInt("core");
		return Core.getCoreOrError(cap);
	}
	
	public ITextComponent getDisplayName(ItemStack stack){
		return new TranslationTextComponent(getCore(stack).getCoreTranslationKey(), new TranslationTextComponent(getCap(stack).getPrefixTranslationKey()));
	}
}