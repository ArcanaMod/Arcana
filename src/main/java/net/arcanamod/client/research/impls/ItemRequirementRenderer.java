package net.arcanamod.client.research.impls;

import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.research.impls.ItemRequirement;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class ItemRequirementRenderer implements RequirementRenderer<ItemRequirement>{
	
	public void render(int x, int y, ItemRequirement requirement, int ticks, float partialTicks, PlayerEntity player){
		//RenderHelper.enableGUIStandardItemLighting();
		//GlStateManager.disableLighting();
		//Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(requirement.getStack(), x, y);
	}
	
	public List<String> tooltip(ItemRequirement requirement, PlayerEntity player){
		return null;
	}
	
	/*public List<String> tooltip(ItemRequirement requirements, PlayerEntity player){
		List<ITextComponent> tooltip = requirements.getStack().getTooltip(Minecraft.getInstance().player, Minecraft.getInstance().gameSettings.advancedItemTooltips ? ADVANCED : NORMAL);
		if(requirements.getAmount() != 0)
			tooltip.set(0, requirements.getAmount() + "x " + tooltip.get(0));
		else
			tooltip.set(0, I18n.format("requirement.item.have") + " " + tooltip.get(0));
		return tooltip;
	}*/
}