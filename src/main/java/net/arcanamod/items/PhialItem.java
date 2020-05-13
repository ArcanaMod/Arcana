package net.arcanamod.items;

import net.arcanamod.Arcana;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PhialItem extends Item
{
    //private String aspectName;

    public PhialItem(String aspectName){
        super(new Properties().group(Arcana.ITEMS));
        /*if(aspectName.startsWith("phial_"))
            aspectName = aspectName.substring(6);
        this.aspectName = aspectName;*/
    }

    /*public ITextComponent getDisplayName(ItemStack stack){
        return new TranslationTextComponent("phial." + aspectName).applyTextStyle(TextFormatting.AQUA);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
        tooltip.add(new TranslationTextComponent("aspect." + aspectName + ".desc"));
        tooltip.add(new StringTextComponent("x8"));
    }*/
}
