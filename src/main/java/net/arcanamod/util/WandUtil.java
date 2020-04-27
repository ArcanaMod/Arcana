package net.arcanamod.util;

import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.ItemAttachment;
import net.arcanamod.items.ItemWand;
import net.arcanamod.wand.CapType;
import net.arcanamod.wand.CoreType;
import net.arcanamod.wand.EnumAttachmentType;
import net.minecraft.item.ItemStack;

public class WandUtil{
	
	public static CoreType getCore(ItemStack wandStack){
		CoreType core = CoreType.ERROR;
		if(wandStack.getItem() instanceof ItemWand){
			ItemWand wand = (ItemWand)wandStack.getItem();
			if(wand == ArcanaItems.WOOD_WAND){
				core = CoreType.WOOD;
			}else if(wand == ArcanaItems.GREATWOOD_WAND){
				core = CoreType.GREATWOOD;
			}else if(wand == ArcanaItems.SILVERWOOD_WAND){
				core = CoreType.SILVERWOOD;
			}else if(wand == ArcanaItems.TAINTED_WAND){
				core = CoreType.TAINTED;
			}else if(wand == ArcanaItems.DAIR_WAND){
				core = CoreType.DAIR;
			}else if(wand == ArcanaItems.HAWTHORN_WAND){
				core = CoreType.HAWTHORN;
			}else if(wand == ArcanaItems.ARCANIUM_WAND){
				core = CoreType.ARCANIUM;
			}
		}
		return core;
	}
	
	public static CapType getCap(ItemStack wandStack){
		CapType cap = CapType.ERROR;
		if(wandStack.getItem() instanceof ItemWand){
			ItemWand wand = (ItemWand)wandStack.getItem();
			ItemAttachment capAttachment = wand.getAttachment(wandStack, EnumAttachmentType.CAP);
			if(capAttachment == ArcanaItems.COPPER_CAP){
				cap = CapType.COPPER;
            /*}else if(capAttachment == ItemInit.ELEMENTIUM_CAP){
                cap = CapType.COPPER;*/
			}else if(capAttachment == ArcanaItems.GOLD_CAP){
				cap = CapType.GOLD;
			}else if(capAttachment == ArcanaItems.IRON_CAP){
				cap = CapType.IRON;
            /*}else if(capAttachment == ItemInit.MANASTEEL_CAP){
                cap = CapType.MANASTEEL;*/
			}else if(capAttachment == ArcanaItems.SILVER_CAP){
				cap = CapType.SILVER;
            /*}else if(capAttachment == ItemInit.TERRASTEEL_CAP){
                cap = CapType.TERRASTEEL;*/
			}else if(capAttachment == ArcanaItems.THAUMIUM_CAP){
				cap = CapType.THAUMIUM;
			}else if(capAttachment == ArcanaItems.VOID_CAP){
				cap = CapType.VOID;
			}
		}
		return cap;
	}
}