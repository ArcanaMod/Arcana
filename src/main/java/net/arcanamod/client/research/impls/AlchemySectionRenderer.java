package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.client.event.RenderTooltip;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.items.recipes.AlchemyRecipe;
import net.arcanamod.research.impls.AlchemySection;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

import static net.arcanamod.client.gui.ResearchEntryScreen.HEIGHT_OFFSET;
import static net.arcanamod.client.gui.ResearchEntryScreen.drawTexturedModalRect;

public class AlchemySectionRenderer extends AbstractCraftingSectionRenderer<AlchemySection>{
	
	void renderRecipe(IRecipe<?> recipe, AlchemySection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		if(recipe instanceof AlchemyRecipe){
			AlchemyRecipe alchemyRecipe = (AlchemyRecipe)recipe;
			int x = right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X, y = ResearchEntryScreen.PAGE_Y;
			
			int ulX = x + (screenWidth - 256 + ResearchEntryScreen.PAGE_WIDTH) / 2 - 35, ulY = y + (screenHeight - 181 + ResearchEntryScreen.PAGE_HEIGHT) / 2 - 10 + HEIGHT_OFFSET;
			mc().getTextureManager().bindTexture(textures);
			drawTexturedModalRect(ulX, ulY, 73, 1, 70, 70);
			drawTexturedModalRect(ulX + 19, ulY - 4, 23, 145, 17, 17);
			
			int inputX = ulX + 1, inputY = ulY - 5;
			ItemStack[] stacks = alchemyRecipe.getIngredients().get(0).getMatchingStacks();
			item(stacks[dispIndex(stacks.length, player)], inputX, inputY);
			
			List<AspectStack> aspects = alchemyRecipe.getAspects();
			int aspectsWidth = Math.min(3, aspects.size());
			int aspectStartX = ulX + 6 - (8 * (aspectsWidth - 3)), aspectStartY = ulY + 29;
			for(int i = 0, size = aspects.size(); i < size; i++){
				AspectStack aspect = aspects.get(i);
				int xx = aspectStartX + (i % aspectsWidth) * 19;
				int yy = aspectStartY + (i / aspectsWidth) * 19;
				ItemStack stack1 = new ItemStack(AspectUtils.getItemStackForAspect(aspect.getAspect()).getItem(), aspect.getAmount());
				item(stack1, xx, yy);
				MatrixStack matrixstack = new MatrixStack();
				String s = String.valueOf(aspect.getAmount());
				matrixstack.translate(0, 0, mc().getItemRenderer().zLevel + 200.0F);
				IRenderTypeBuffer.Impl impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
				mc().fontRenderer.renderString(s, xx + 19 - mc().fontRenderer.getStringWidth(s), yy + 10, 0, false, matrixstack.getLast().getMatrix(), impl, false, 0, 0xf000f0);
				mc().fontRenderer.renderString(s, xx + 20 - mc().fontRenderer.getStringWidth(s), yy + 11, 0x999999, false, matrixstack.getLast().getMatrix(), impl, false, 0, 0xf000f0);
				impl.finish();
			}
		}else
			error();
	}
	
	void renderRecipeTooltips(IRecipe<?> recipe, AlchemySection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		if(recipe instanceof AlchemyRecipe){
			AlchemyRecipe alchemyRecipe = (AlchemyRecipe)recipe;
			int x = right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X, y = ResearchEntryScreen.PAGE_Y;
			int ulX = x + (screenWidth - 256 + ResearchEntryScreen.PAGE_WIDTH) / 2 - 35, ulY = y + (screenHeight - 181 + ResearchEntryScreen.PAGE_HEIGHT) / 2 - 10 + HEIGHT_OFFSET;
			int inputX = ulX + 1, inputY = ulY - 5;
			ItemStack[] stacks = alchemyRecipe.getIngredients().get(0).getMatchingStacks();
			tooltipArea(stacks[dispIndex(stacks.length, player)], mouseX, mouseY, screenWidth, screenHeight, inputX, inputY);
			List<AspectStack> aspects = alchemyRecipe.getAspects();
			int aspectsWidth = Math.min(3, aspects.size());
			int aspectStartX = ulX + 12 - (8 * (aspectsWidth - 3)), aspectStartY = ulY + 29;
			for(int i = 0, size = aspects.size(); i < size; i++){
				AspectStack aspect = aspects.get(i);
				int xx = aspectStartX + (i % aspectsWidth) * 17;
				int yy = aspectStartY + (i / aspectsWidth) * 17;
				ItemStack stack1 = AspectUtils.getItemStackForAspect(aspect.getAspect());
				tooltipArea(stack1, mouseX, mouseY, screenWidth, screenHeight, xx, yy);
			}
		}
	}
}