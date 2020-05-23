package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.items.attachment.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

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
	
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		if(isInGroup(group)){
			// iron/wooden, silver/dair, gold/greatwood, thaumium/silverwood, void/arcanium
			items.add(withCapAndCore("iron_cap", "wood_wand"));
			items.add(withCapAndCore("silver_cap", "dair_wand"));
			items.add(withCapAndCore("gold_cap", "greatwood_wand"));
			items.add(withCapAndCore("void_cap", "arcanium_wand"));
		}
	}
	
	// change for recipes
	private ItemStack withCapAndCore(String cap, String core){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("cap", "arcana:" + cap);
		nbt.putString("core", "arcana:" + core);
		ItemStack stack = new ItemStack(this, 1);
		stack.setTag(nbt);
		return stack;
	}
}