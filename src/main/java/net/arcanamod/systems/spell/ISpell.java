package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;

public interface ISpell {
	/**
	 * Use of that spell.
	 * @param player Player
	 * @param sender Sender
	 * @param action Click Action
	 * @return
	 */
	void use(PlayerEntity player, Object sender, Pair<Aspect,Aspect> cast, IOldSpell.Action action);
}
