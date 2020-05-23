package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.items.attachment.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
		String cap = stack.getOrCreateTag().getString("cap");
		return Cap.getCapOrError(new ResourceLocation(cap));
	}
	
	public static Focus getFocus(ItemStack stack){
		int focus = stack.getOrCreateTag().getInt("focus");
		return Focus.getFocusById(focus).orElse(Focus.NO_FOCUS);
	}
	
	public static Core getCore(ItemStack stack){
		String core = stack.getOrCreateTag().getString("core");
		return Core.getCoreOrError(new ResourceLocation(core));
	}
	
	public ITextComponent getDisplayName(ItemStack stack){
		return new TranslationTextComponent(getCore(stack).getCoreTranslationKey(), new TranslationTextComponent(getCap(stack).getPrefixTranslationKey()));
	}
}