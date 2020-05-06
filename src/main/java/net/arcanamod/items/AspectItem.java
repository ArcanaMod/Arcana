package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
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
		super(new Properties().group(Arcana.ASPECTS));
		this.aspectName = aspectName;
	}
	
	public String getItemStackDisplayName(ItemStack stack){
		// aspect.beast.name, for example
		return I18n.format("aspect." + aspectName + ".name").trim();
	}
	
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
		// aspect.beast.desc, for example
		//tooltip.add(I18n.translateToLocal("aspect." + aspectName + ".desc").trim());
		tooltip.add(new TranslationTextComponent("aspect." + aspectName + ".desc"));
	}
	
	// getCreatorModId may be useful to override, to show who registered the aspect.
}