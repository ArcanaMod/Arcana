package net.arcanamod.systems.spell.modules.circle;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.modules.CircleSpellModule;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.nbt.CompoundNBT;

import java.awt.*;
import java.util.Arrays;

public class DoubleModifierCircle extends CircleSpellModule{
	public Aspect firstAspect = Aspects.EMPTY;
	public Aspect secondAspect = Aspects.EMPTY;

	@Override
	public String getName() {
		return "double_modifier_circle";
	}

	@Override
	public void fromNBT(CompoundNBT compound) {
		super.fromNBT(compound);
		firstAspect = AspectUtils.getAspect(compound, "aspecta");
		secondAspect = AspectUtils.getAspect(compound, "aspectb");
	}

	@Override
	public CompoundNBT toNBT(CompoundNBT compound) {
		super.toNBT(compound);
		AspectUtils.putAspect(compound, "aspecta", firstAspect);
		AspectUtils.putAspect(compound, "aspectb", secondAspect);
		return compound;
	}

	@Override
	public boolean canConnectSpecial(SpellModule connectingModule) {
		return (connectingModule instanceof SinModifierCircle);
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

		return (((relX >= 19 && relX < 35 && relY >= -8 && relY < 8)
					|| (relX >= -35 && relX < -19 && relY >= -8 && relY < 8))
				&& Arrays.asList(AspectUtils.primalAspects).contains(aspect));
	}

	@Override
	public void assign(int x, int y, Aspect aspect) {
		int relX = this.x - x;
		int relY = this.y - y;
		if (canAssign(x, y, aspect)) {
			if (relX >= 19 && relX < 35 && relY >= -8 && relY < 8) {
				firstAspect = aspect;
			} else if (relX >= -35 && relX < -19 && relY >= -8 && relY < 8) {
				secondAspect = aspect;
			}
		}
	}

	@Override
	public int getHeight() {
		return 72;
	}

	@Override
	public int getWidth() {
		return 72;
	}
}
