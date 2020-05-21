package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.items.attachment.*;
import net.arcanamod.wand.EnumAttachmentType;
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
		return Cap.getCapById(cap).orElse(ArcanaItems.ERROR_CAP);
	}
	
	public static Focus getFocus(ItemStack stack){
		int cap = stack.getOrCreateTag().getInt("focus");
		return Focus.getFocusById(cap).orElse(ArcanaItems.ERROR_FOCUS);
	}
	
	public static Core getCore(ItemStack stack){
		int cap = stack.getOrCreateTag().getInt("core");
		return Core.getCoreById(cap).orElse(ArcanaItems.ERROR_WAND_CORE);
	}
	
	public ITextComponent getDisplayName(ItemStack stack){
		return new TranslationTextComponent(getTranslationKey(stack), new TranslationTextComponent(getCap(stack).getTranslationKey() + ".prefix"));
	}
}