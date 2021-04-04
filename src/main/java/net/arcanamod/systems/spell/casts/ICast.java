package net.arcanamod.systems.spell.casts;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

/**
 * The ISpell Class
 */
public interface ICast {
	
	// TODO: improve docs

	/**
	 * Core aspect in spell.
	 * @return returns core aspect.
	 */
	Aspect getSpellAspect();

	int getSpellDuration();

	/**
	 * Use of that spell.
	 * @param player Player
	 * @param sender Sender
	 * @param action Click Action
	 * @return
	 */
	void use(UUID uuid, World world, PlayerEntity player, Object sender, Pair<Aspect,Aspect> cast, ICast.Action action);

	default Optional<ITextComponent> getName(CompoundNBT nbt){
		return Optional.empty();
	}
	
	/**
	 * Click action
	 *
	 * USE -> Right click
	 * ALT_USE -> Left Click
	 * SPECIAL -> Shift + Right Click
	 */
	enum Action{
		USE,
		ALT_USE,
		SPECIAL
	}
}
