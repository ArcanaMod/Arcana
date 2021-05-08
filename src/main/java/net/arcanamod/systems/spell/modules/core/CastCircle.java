package net.arcanamod.systems.spell.modules.core;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.ClientUiUtil;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.systems.spell.casts.Casts;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.circle.DoubleModifierCircle;
import net.arcanamod.systems.spell.modules.circle.SinModifierCircle;
import net.arcanamod.systems.spell.modules.circle.SingleModifierCircle;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class CastCircle extends SpellModule {
	public ICast cast = null;

	@Override
	public String getName() {
		return "cast_circle";
	}

	@Override
	public void fromNBT(CompoundNBT compound) {
		super.fromNBT(compound);
		cast = Casts.castMap.get(new ResourceLocation(compound.getString("cast")));
	}

	@Override
	public CompoundNBT toNBT(CompoundNBT compound) {
		super.toNBT(compound);
		if (cast instanceof Cast) {
			compound.putString("cast", ((Cast) cast).getId().toString());
		} else if (cast != null) {
			compound.putString("cast", cast.getSpellAspect().toResourceLocation().toString());
		} else {
			compound.putString("cast", Aspects.EMPTY.toResourceLocation().toString());
		}
		return compound;
	}

	@Override
	public boolean canConnectSpecial(SpellModule connectingModule) {
		if (connectingModule instanceof SinModifierCircle) {
			return boundSpecial.stream()
					.filter(module -> module != connectingModule)
					.noneMatch(module -> module instanceof SinModifierCircle);
		} else if (connectingModule instanceof SingleModifierCircle
					|| connectingModule instanceof DoubleModifierCircle) {
			return boundSpecial.stream()
					.filter(module -> module != connectingModule)
					.noneMatch(module -> module instanceof SingleModifierCircle
								|| module instanceof DoubleModifierCircle);
		} else {
			return false;
		}
	}

	@Override
	public Point getSpecialPoint(SpellModule module) {
		Point ret = null;
		if (canConnectSpecial(module)) {
			ret = new Point(x, y);
		}
		return ret;
	}

	@Override
	public boolean canAssign(int x, int y, Aspect aspect) {
		int relX = this.x - x;
		int relY = this.y - y;

		return (relX >= -8 && relX < 8
				&& relY >= -8 && relY < 8
				&& Aspects.getAll().contains(aspect));
	}

	@Override
	public void assign(int x, int y, Aspect aspect) {
		if (canAssign(x, y, aspect)) {
			cast = Casts.castMap.get(aspect.toResourceLocation());
		}
	}

	@Override
	public int getHeight() {
		return 32;
	}

	@Override
	public int getWidth() {
		return 32;
	}

	@Override
	public void renderUnderMouse(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating, MatrixStack stack) {
		ClientUiUtil.drawTexturedModalRect(stack, mouseX - getWidth() / 2, mouseY - getHeight() / 2, 32, 16, getWidth(), getHeight());
		if (!floating || cast == null) {
			ClientUiUtil.drawTexturedModalRect(stack, mouseX - 8, mouseY - 8, 32, 0, 16, 16);
		} else {
			itemRenderer.renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(cast.getSpellAspect()), mouseX - 8, mouseY - 8);
		}
	}

	@Override
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating, MatrixStack stack) {
		ClientUiUtil.drawTexturedModalRect(stack, x - getWidth() / 2, y - getHeight() / 2, 32, 16, getWidth(), getHeight());
		if (!floating) {
			if (cast == null) {
				ClientUiUtil.drawTexturedModalRect(stack, x - 8, y - 8, 32, 0, 16, 16);
			} else {
				itemRenderer.renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(cast.getSpellAspect()), x - 8, y - 8);
			}
		}
	}
}
