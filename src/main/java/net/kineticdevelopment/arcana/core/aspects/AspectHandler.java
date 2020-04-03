package net.kineticdevelopment.arcana.core.aspects;

import net.kineticdevelopment.arcana.core.aspects.Aspect.AspectType;
import net.kineticdevelopment.arcana.core.research.impls.ResearcherCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Implement this interface as a capability which should handle aspects, especially storing
 * vis. This should not be used for essentia (which doesn't exist yet anyways).
 */
public interface AspectHandler{
	
	/**
	 * Inserts an amount of vis of an aspect, and returns the remainder.
	 *
	 * @param aspect
	 * 		The type of vis to insert.
	 * @param amount
	 * 		The amount of vis to insert.
	 * @param simulate
	 * 		If true, the amount of vis is not actually changed.
	 * @return The amount of vis (that would be) leftover.
	 */
	int insert(AspectType aspect, int amount, boolean simulate);
	
	/**
	 * Gets the current amount of vis of a given aspect stored in this handler.
	 *
	 * @param aspect
	 * 		The aspect to test for.
	 * @return The amount of that aspect stored.
	 */
	int getCurrentVis(AspectType aspect);
	
	/**
	 * Drains an amount of vis of a given aspect from this handler, and returns
	 * the amount removed.
	 *
	 * @param aspect
	 * 		The aspect to remove.
	 * @param amount
	 * 		The maximum amount to remove.
	 * @param simulate
	 * 		If true, the amount of vis is not actually changed.
	 * @return The amount of vis removed from this handler.
	 */
	int drain(AspectType aspect, int amount, boolean simulate);
	
	/**
	 * Returns whether any non-zero amount of that aspect can be currently inserted.
	 *
	 * @param aspect
	 * 		The aspect to test for.
	 * @return If that aspect can be inserted.
	 */
	boolean canInsert(AspectType aspect);
	
	/**
	 * Returns whether any non-zero amount of that aspect can be stored at any point,
	 * regardless of this handler's current stored amount of that aspect.
	 *
	 * @param aspect
	 * 		The aspect to test for.
	 * @return If that aspect can be stored at any point..
	 */
	boolean canStore(AspectType aspect);
	
	/**
	 * Returns the maximum amount of vis of an aspect that can exist within
	 * this handler.
	 *
	 * @param aspect
	 * 		The aspect to test for.
	 * @return The maximum amount of that aspect that can be inserted.
	 */
	int getCapacity(AspectType aspect);
	
	/**
	 * Returns the maximum amount of vis of any type that is accepted by this
	 * handler that can exist in this handler.
	 *
	 * @return The maximum amount of vis stored by this handler.
	 */
	int getCapacity();
	
	NBTTagCompound serialize();
	void deserialize(NBTTagCompound data);
	
	static Optional<AspectHandler> getFrom(@Nonnull ICapabilityProvider holder){
		return Optional.ofNullable(holder.getCapability(AspectHandlerCapability.ASPECT_HANDLER, null));
	}
}