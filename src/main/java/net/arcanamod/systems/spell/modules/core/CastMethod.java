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
	public int getOutputAmount(){
		return 4;
	}

	@Override
	public boolean canConnect(SpellModule module, boolean special) {
		return (super.canConnect(module, special)
				&& (!special
					|| (module instanceof CastMethodSin
		 				&& this.boundSpecial.size() == 0)));
	}

	@Override
	public boolean canConnectSpecial(SpellModule connectingModule) {
		return (boundSpecial.contains(connectingModule)
				|| (boundSpecial.size() == 0 && connectingModule instanceof CastMethodSin));
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
				&& (aspect == Aspects.EMPTY
					|| Arrays.asList(AspectUtils.primalAspects).contains(aspect)));
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
	public void renderUnderMouse(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating) {
		UiUtil.drawTexturedModalRect(mouseX - getWidth() / 2, mouseY - getHeight() / 2, 94, 54, getWidth(), getHeight());
		if (!floating || aspect == Aspects.EMPTY) {
			UiUtil.drawTexturedModalRect(mouseX - 8, mouseY - 8, 48, 0, 16, 16);
		} else {
			itemRenderer.renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(aspect), mouseX - 8, mouseY - 8);
		}
	}

	@Override
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating) {
		UiUtil.drawTexturedModalRect(x - getWidth() / 2, y - getHeight() / 2, 94, 54, getWidth(), getHeight());
		if (!floating) {
			if (aspect == Aspects.EMPTY) {
				UiUtil.drawTexturedModalRect(x - 8, y - 8, 48, 0, 16, 16);
			} else {
				itemRenderer.renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(aspect), x - 8, y - 8);
			}
		}
	}

	@Override
	public Point getConnectionRenderStart() {
		SpellModule sin = boundSpecial.stream().filter(module -> module instanceof CastMethodSin).findFirst().orElse(null);
		if (sin != null) {
			return new Point(x + 36, y);
		} else {
			return new Point(this.x, this.y);
		}
	}

}
