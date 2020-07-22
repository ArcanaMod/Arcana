package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrystalItem extends Item{
    
    public final Aspect aspect;
    
    public CrystalItem(Properties properties, Aspect aspect){
        super(properties);
        this.aspect = aspect;
    }
    
    public ITextComponent getDisplayName(ItemStack stack){
        return new TranslationTextComponent("item.arcana.crystal", new TranslationTextComponent("aspect." + aspect.name()));
    }
}