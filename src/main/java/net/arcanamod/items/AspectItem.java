package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectItem extends Item{
	
	private String aspectName;
	
	public AspectItem(String aspectName){
		super(new Properties());
		if(aspectName.startsWith("aspect_"))
			aspectName = aspectName.substring(7);
		this.aspectName = aspectName;
	}
	
	public ITextComponent getDisplayName(ItemStack stack){
		return new TranslationTextComponent("aspect." + aspectName).mergeStyle(TextFormatting.AQUA);
	}
	
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
		tooltip.add(new TranslationTextComponent("aspect." + aspectName + ".desc"));
	}
	
	// getCreatorModId may be useful to override, to show who registered the aspect.
}