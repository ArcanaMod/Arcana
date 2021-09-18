package net.arcanamod.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import net.arcanamod.aspects.*;
import net.arcanamod.client.gui.ClientUiUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AspectIngredient extends AspectStack implements Cloneable {
	public static final IIngredientType<AspectIngredient> TYPE = () -> AspectIngredient.class;
	
	public boolean isUndecided = false;
	
	public AspectIngredient(Aspect aspect, float amount, boolean isUndecided){
		this.isEmpty = amount <= 0 || aspect == Aspects.EMPTY;
		
		this.aspect = isEmpty ? Aspects.EMPTY : aspect;
		this.amount = isEmpty ? 0 : amount;
		
		this.isUndecided = isUndecided;
	}
	
	public static AspectIngredient fromStack(AspectStack stack) {
		return new AspectIngredient(stack.getAspect(), stack.getAmount(),false);
	}
	
	public static AspectIngredient fromUndecidedStack(UndecidedAspectStack stack) {
		return new AspectIngredient(stack.stack.getAspect(), stack.stack.getAmount(),stack.any);
	}
	
	public static AspectIngredient fromSingleton(Aspect aspect) {
		return new AspectIngredient(aspect, 1, false);
	}
	
	public static class Helper implements IIngredientHelper<AspectIngredient> {
		
		/**
		 * @param ingredients
		 * @param destIngredient
		 * @deprecated
		 */
		@Nullable
		@Override
		public AspectIngredient getMatch(Iterable<AspectIngredient> ingredients, AspectIngredient destIngredient) {
			for (AspectIngredient ingredient : ingredients) {
				if (ingredient.getAspect() == destIngredient.getAspect()) {
					return ingredient;
				}
			}
			return null;
		}
		
		@Override
		public String getDisplayName(AspectIngredient aspectIngredient) {
			return AspectUtils.getLocalizedAspectDisplayName(aspectIngredient.getAspect());
		}
		
		/**
		 * @param aspectIngredient
		 * @deprecated
		 */
		@Override
		public String getUniqueId(AspectIngredient aspectIngredient) {
			if (aspectIngredient.getAspect().getId()==32768)return "arcana:32768"+ new Random().nextInt(32768)+aspectIngredient.getAmount();
			return getAspectLoc(aspectIngredient).toString();
		}
		
		@Override
		public String getModId(AspectIngredient aspectIngredient) {
			if (aspectIngredient.getAspect().getId()==32768)return "arcana:32768"+ new Random().nextInt(32768)+aspectIngredient.getAmount();
			return getAspectLoc(aspectIngredient).getNamespace();
		}
		
		@Override
		public String getResourceId(AspectIngredient aspectIngredient) {
			if (aspectIngredient.getAspect().getId()==32768)return "arcana:32768"+ new Random().nextInt(32768)+aspectIngredient.getAmount();
			return getAspectLoc(aspectIngredient).getPath();
		}
		
		@Override
		public AspectIngredient copyIngredient(AspectIngredient aspectIngredient) {
			try {
				return (AspectIngredient) aspectIngredient.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		public String getErrorInfo(@Nullable AspectIngredient aspectIngredient) {
			if (aspectIngredient == null) {
				return "Aspect Ingredient is null";
			}
			return "Aspect Ingredient errored: "+aspectIngredient.toString();
		}
		
		private ResourceLocation getAspectLoc(AspectIngredient aspectIngredient){
			return aspectIngredient.getAspect().toResourceLocation();
		}
	}
	
	public static class Renderer implements IIngredientRenderer<AspectIngredient> {
		
		@Override
		public void render(MatrixStack matrixStack, int x, int y, @Nullable AspectIngredient aspectIngredient) {
			if (aspectIngredient == null) return;
			ClientUiUtil.renderAspectStack(matrixStack, aspectIngredient, x, y);
		}
		
		@Override
		public List<ITextComponent> getTooltip(AspectIngredient aspectIngredient, ITooltipFlag iTooltipFlag) {
			return Collections.singletonList(
					new StringTextComponent(AspectUtils.getLocalizedAspectDisplayName(aspectIngredient.getAspect()))
			);
		}
	}
}
