package net.arcanamod.systems.taint;

import net.minecraft.util.DamageSource;

public class TaintDamageSource extends DamageSource {
	public static TaintDamageSource TAINT = new TaintDamageSource();

	public TaintDamageSource() {
		super("taint");
	}

	@Override
	public boolean isUnblockable() {
		return true;
	}
}
