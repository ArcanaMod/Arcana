package net.kineticdevelopment.arcana.utilities;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.common.items.ItemAttachment;
import net.kineticdevelopment.arcana.common.items.ItemWand;
import net.kineticdevelopment.arcana.core.wand.CapType;
import net.kineticdevelopment.arcana.core.wand.CoreType;
import net.kineticdevelopment.arcana.core.wand.EnumAttachmentType;
import net.minecraft.item.ItemStack;

public class WandUtil {

    public static CoreType getCore(ItemStack wandStack){
        CoreType core = CoreType.ERROR;
        if(wandStack.getItem() instanceof ItemWand){
            ItemWand wand = (ItemWand)wandStack.getItem();
            if(wand == ItemInit.WOOD_WAND){
                core = CoreType.WOOD;
            }else if(wand == ItemInit.GREATWOOD_WAND){
                core = CoreType.GREATWOOD;
            }else if(wand == ItemInit.SILVERWOOD_WAND){
                core = CoreType.SILVERWOOD;
            }else if(wand == ItemInit.TAINTED_WAND){
                core = CoreType.TAINTED;
            }else if(wand == ItemInit.DAIR_WAND){
                core = CoreType.DAIR;
            }else if(wand == ItemInit.HAWTHORN_WAND){
                core = CoreType.HAWTHORN;
            }else if(wand == ItemInit.ARCANIUM_WAND){
                core = CoreType.ARCANIUM;
            }
        }
        return core;
    }

    public static CapType getCap(ItemStack wandStack){
        CapType cap = CapType.ERROR;
        if(wandStack.getItem() instanceof ItemWand){
            ItemWand wand = (ItemWand) wandStack.getItem();
            ItemAttachment capAttachment = wand.getAttachment(wandStack, EnumAttachmentType.CAP);
            if(capAttachment == ItemInit.COPPER_CAP){
                cap = CapType.COPPER;
            }else if(capAttachment == ItemInit.ELEMENTIUM_CAP){
                cap = CapType.COPPER;
            }else if(capAttachment == ItemInit.GOLD_CAP){
                cap = CapType.GOLD;
            }else if(capAttachment == ItemInit.IRON_CAP){
                cap = CapType.IRON;
            }else if(capAttachment == ItemInit.MANASTEEL_CAP){
                cap = CapType.MANASTEEL;
            }else if(capAttachment == ItemInit.SILVER_CAP){
                cap = CapType.SILVER;
            }else if(capAttachment == ItemInit.TERRASTEEL_CAP){
                cap = CapType.TERRASTEEL;
            }else if(capAttachment == ItemInit.THAUMIUM_CAP){
                cap = CapType.THAUMIUM;
            }else if(capAttachment == ItemInit.VOID_CAP){
                cap = CapType.VOID;
            }
        }
        return cap;
    }
}