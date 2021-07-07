package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.systems.research.impls.ItemTagRequirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.util.ITooltipFlag.TooltipFlags.ADVANCED;
import static net.minecraft.client.util.ITooltipFlag.TooltipFlags.NORMAL;

public class ItemTagRequirementRenderer implements RequirementRenderer<ItemTagRequirement>{
	
	public void render(MatrixStack matrices, int x, int y, ItemTagRequirement requirement, int ticks, float partialTicks, PlayerEntity player){
		// pick an item
		List<Item> items = new ArrayList<>(requirement.getTag().getAllElements());
		ItemStack stack = new ItemStack(items.get((ticks / 30) % items.size()));
		
		RenderHelper.enableStandardItemLighting();
		RenderSystem.disableLighting();
		ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
		renderer.renderItemAndEffectIntoGUI(stack, x, y);
	}
	
	public List<ITextComponent> tooltip(ItemTagRequirement requirement, PlayerEntity player){
		// pick an item
		List<Item> items = new ArrayList<>(requirement.getTag().getAllElements());
		ItemStack stack = new ItemStack(items.get((player.ticksExisted / 30) % items.size()));
		
		List<ITextComponent> tooltip = stack.getTooltip(Minecraft.getInstance().player, Minecraft.getInstance().gameSettings.advancedItemTooltips ? ADVANCED : NORMAL);
		if(requirement.getAmount() != 0)
			tooltip.set(0, new TranslationTextComponent("requirement.item.num", requirement.getAmount(), tooltip.get(0)));
		else
			tooltip.set(0, new TranslationTextComponent("requirement.item.have", tooltip.get(0)));
		tooltip.add(new TranslationTextComponent("requirement.tag.accepts_any", requirement.getTagName().toString()).mergeStyle(TextFormatting.DARK_GRAY));
		return tooltip;
	}
	
	public boolean shouldDrawTickOrCross(ItemTagRequirement requirement, int amount){
		return amount == 0;
	}
}