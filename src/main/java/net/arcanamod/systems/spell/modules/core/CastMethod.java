package net.arcanamod.systems.spell.modules.core;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.nbt.CompoundNBT;

import java.awt.*;
import java.util.Arrays;

public class CastMethod extends SpellModule {
	public Aspect aspect = Aspects.EMPTY;

	@Override
	public String getName() {
		return "cast_method";
	}

	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return true;
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
	public boolean canConnectSpecial(SpellModule connectingModule) {
		boolean alreadyConnected = false;
		for (SpellModule module : getBoundModules()) {
			if (module != connectingModule && connectingModule instanceof CastMethodSin) {
				alreadyConnected = true;
				break;
			}
		}
		return (!alreadyConnected && connectingModule instanceof CastMethodSin);
	}

	@Override
	public Point getSpecialPoint(SpellModule module) {
		Point ret = null;
		if (canConnectSpecial(module)) {
			ret = new Point(x + 36, y);
		}
		return ret;
	}

	@Override
	public boolean canAssign(int x, int y, Aspect aspect) {
		int relX = this.x - x;
		int relY = this.y - y;

		return (relX >= -8 && relX < 8
				&& relY >= -8 && relY < 8
				&& Arrays.asList(AspectUtils.primalAspects).contains(aspect));
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
		return 36;
	}

	@Override
	public void renderUnderMouse(int mouseX, int mouseY) {
		UiUtil.drawTexturedModalRect(mouseX - getWidth() / 2, mouseY - getHeight() / 2, 94, 54, getWidth(), getHeight());
		UiUtil.drawTexturedModalRect(mouseX - 8, mouseY - 8, 48, 0, 16, 16);
	}

	@Override
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer) {
		UiUtil.drawTexturedModalRect(x - getWidth() / 2, y - getHeight() / 2, 94, 54, getWidth(), getHeight());
		if (aspect == Aspects.EMPTY) {
			UiUtil.drawTexturedModalRect(x - 8, y - 8, 48, 0, 16, 16);
		} else {
			itemRenderer.renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(aspect), x - 8, y - 8);
		}
	}
}
