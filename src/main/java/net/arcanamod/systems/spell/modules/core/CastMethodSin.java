package net.arcanamod.systems.spell.modules.core;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.ClientUiUtil;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.nbt.CompoundNBT;

import java.util.Arrays;

public class CastMethodSin extends SpellModule {
	public Aspect aspect = Aspects.EMPTY;

	@Override
	public boolean isCastModifier() {
		return true;
	}

	@Override
	public String getName() {
		return "cast_method_sin";
	}

	@Override
	public int getInputAmount() {
		return 1;
	}

	@Override
	public SpellModule getConnectionStart() {
		if (parent != null) {
			return parent.getConnectionStart();
		} else {
			return null;
		}
	}

	@Override
	public SpellModule getConnectionEnd(boolean special) {
		if (parent != null) {
			return parent.getConnectionEnd(special);
		} else if (special) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public void fromNBT(CompoundNBT compound) {
		super.fromNBT(compound);
		aspect = AspectUtils.getAspect(compound, "aspect");
	}

	@Override
	public CompoundNBT toNBT(CompoundNBT compound) {
		super.toNBT(compound);
		AspectUtils.putAspect(compound, "aspect", aspect);
		return compound;
	}

	@Override
	public boolean canAssign(int x, int y, Aspect aspect) {
		int relX = this.x - x;
		int relY = this.y - y;

		return (relX >= -3 && relX < 13
			&& relY >= -8 && relY < 8
			&& (aspect == Aspects.EMPTY
				|| Arrays.asList(AspectUtils.sinAspects).contains(aspect)));
	}

	@Override
	public void assign(int x, int y, Aspect aspect) {
		if (canAssign(x, y, aspect)) {
			this.aspect = aspect;
		}
	}

	@Override
	public int getHeight() {
		return 36;
	}

	@Override
	public int getWidth() {
		return 46;
	}

	@Override
	public void renderUnderMouse(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating, MatrixStack stack) {
		ClientUiUtil.drawTexturedModalRect(stack, mouseX - getWidth() / 2, mouseY - getHeight() / 2, 153, 54, getWidth(), getHeight());
		if (!floating || aspect == Aspects.EMPTY) {
			ClientUiUtil.drawTexturedModalRect(stack, mouseX - 3, mouseY - 8, 64, 0, 16, 16);
		} else {
			itemRenderer.renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(aspect), mouseX - 3, mouseY - 8);
		}
	}

	@Override
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating, MatrixStack stack) {
		ClientUiUtil.drawTexturedModalRect(stack, x - getWidth() / 2, y - getHeight() / 2, 153, 54, getWidth(), getHeight());
		if (!floating) {
			if (aspect == Aspects.EMPTY) {
				ClientUiUtil.drawTexturedModalRect(stack, x - 3, y - 8, 64, 0, 16, 16);
			} else {
				itemRenderer.renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(aspect), x - 3, y - 8);
			}
		}
	}
}
