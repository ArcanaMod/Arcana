package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.RequirementRenderer;
import net.kineticdevelopment.arcana.core.research.impls.ItemRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

import static net.minecraft.client.util.ITooltipFlag.TooltipFlags.ADVANCED;
import static net.minecraft.client.util.ITooltipFlag.TooltipFlags.NORMAL;

public class ItemRequirementRenderer implements RequirementRenderer<ItemRequirement>{
	
	public void render(int x, int y, ItemRequirement requirement, int ticks, float partialTicks, EntityPlayer player){
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(requirement.getStack(), x, y);
	}
	
	public List<String> tooltip(ItemRequirement requirements, EntityPlayer player){
		List<String> tooltip = requirements.getStack().getTooltip(Minecraft.getMinecraft().player, Minecraft.getMinecraft().gameSettings.advancedItemTooltips ? ADVANCED : NORMAL);
		tooltip.set(0, requirements.getAmount() + "x " + tooltip.get(0));
		return tooltip;
	}
}