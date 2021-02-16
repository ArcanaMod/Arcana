package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.systems.spell.casts.Casts;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.spell.modules.CircleSpellModule;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.circle.DoubleModifierCircle;
import net.arcanamod.systems.spell.modules.circle.SinModifierCircle;
import net.arcanamod.systems.spell.modules.circle.SingleModifierCircle;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Arrays;

public class CastCircle extends SpellModule {
	public ICast cast = null;

	@Override
	public String getName() {
		return "cast_circle";
	}

	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return true;
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
		} else {
			compound.putString("cast", cast.getSpellAspect().toResourceLocation().toString());
		}
		return compound;
	}

	@Override
	public boolean canConnectSpecial(SpellModule connectingModule) {
		boolean alreadyConnected = false;
		for (SpellModule module : getBoundModules()) {
			if (module != connectingModule
					&& (connectingModule instanceof SingleModifierCircle
						|| connectingModule instanceof DoubleModifierCircle)) {
				alreadyConnected = true;
				break;
			}
		}
		return (!alreadyConnected
				&& (connectingModule instanceof SingleModifierCircle
					|| connectingModule instanceof DoubleModifierCircle));
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
				&& Aspects.getWithoutEmpty().contains(aspect));
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
	public void renderUnderMouse(int mouseX, int mouseY) {
		UiUtil.drawTexturedModalRect(mouseX - getWidth() / 2, mouseY - getHeight() / 2, 32, 16, getWidth(), getHeight());
		UiUtil.drawTexturedModalRect(mouseX - 8, mouseY - 8, 32, 0, 16, 16);
	}

	@Override
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer) {
		UiUtil.drawTexturedModalRect(x - getWidth() / 2, y - getHeight() / 2, 32, 16, getWidth(), getHeight());
		if (cast == null) {
			UiUtil.drawTexturedModalRect(x - 8, y - 8, 32, 0, 16, 16);
		} else {
			itemRenderer.renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(cast.getSpellAspect()), x - 8, y - 8);
		}
	}
}
