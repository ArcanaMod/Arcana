package net.kineticdevelopment.arcana.common.items;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.common.objects.items.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectItem extends ItemBase{
	
	private String aspectName;
	
	public AspectItem(String name, String aspectName){
		super(name);
		this.aspectName = aspectName;
	}
	
	public String getItemStackDisplayName(ItemStack stack){
		// aspect.beast.name, for example
		return I18n.translateToLocal("aspect." + aspectName + ".name").trim();
	}
	
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		// aspect.beast.desc, for example
		tooltip.add(I18n.translateToLocal("aspect." + aspectName + ".desc").trim());
	}
	
	// getCreatorModId may be useful to override, to show who registered the aspect.
}