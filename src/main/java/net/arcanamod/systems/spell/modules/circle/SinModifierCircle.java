package net.arcanamod.systems.spell.modules.circle;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.systems.spell.modules.CircleSpellModule;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.nbt.CompoundNBT;

import java.util.Arrays;

public class SinModifierCircle extends CircleSpellModule {
	public Aspect aspect = Aspects.EMPTY;

	@Override
	public String getName() {
		return "sin_modifier_circle";
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

		return (relX >= 39 && relX < 55
				&& relY >= -8 && relY < 8
				&& Arrays.asList(AspectUtils.sinAspects).contains(aspect));
	}

	@Override
	public void assign(int x, int y, Aspect aspect) {
		if (canAssign(x, y, aspect)) {
			this.aspect = aspect;
		}
	}

	@Override
	public int getHeight() {
		return 112;
	}

	@Override
	public int getWidth() {
		return 112;
	}

	@Override
	public void renderUnderMouse(int mouseX, int mouseY) {
		UiUtil.drawTexturedModalRect(mouseX - getWidth() / 2, mouseY - getHeight() / 2, 0, 128, getWidth(), getHeight());
		UiUtil.drawTexturedModalRect(mouseX - 8, mouseY - 55, 64, 0, 16, 16);
	}

	@Override
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer) {
		UiUtil.drawTexturedModalRect(mouseX - getWidth() / 2, mouseY - getHeight() / 2, 0, 128, getWidth(), getHeight());
		if (aspect == Aspects.EMPTY) {
			UiUtil.drawTexturedModalRect(x - 8, y - 55, 64, 0, 16, 16);
		} else {
			itemRenderer.renderItemAndEffectIntoGUI(AspectUtils.getItemStackForAspect(aspect), x - 8, y - 55);
		}
	}
}
