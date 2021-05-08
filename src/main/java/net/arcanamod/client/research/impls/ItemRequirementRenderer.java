package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.systems.research.impls.ItemRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

import static net.minecraft.client.util.ITooltipFlag.TooltipFlags.ADVANCED;
import static net.minecraft.client.util.ITooltipFlag.TooltipFlags.NORMAL;

public class ItemRequirementRenderer implements RequirementRenderer<ItemRequirement>{
	
	public void render(MatrixStack matrices, int x, int y, ItemRequirement requirement, int ticks, float partialTicks, PlayerEntity player){
		RenderHelper.enableStandardItemLighting();
		RenderSystem.disableLighting();
		ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
		renderer.renderItemAndEffectIntoGUI(requirement.getStack(), x, y);
	}
	
	public List<ITextComponent> tooltip(ItemRequirement requirement, PlayerEntity player){
		List<ITextComponent> tooltip = requirement.getStack().getTooltip(Minecraft.getInstance().player, Minecraft.getInstance().gameSettings.advancedItemTooltips ? ADVANCED : NORMAL);
		if(requirement.getAmount() != 0)
			tooltip.set(0, new TranslationTextComponent("requirement.item.num", requirement.getAmount(), tooltip.get(0)));
		else
			tooltip.set(0, new TranslationTextComponent("requirement.item.have", tooltip.get(0)));
		return tooltip;
	}
	
	public boolean shouldDrawTickOrCross(ItemRequirement requirement, int amount){
		return amount == 0;
	}
}