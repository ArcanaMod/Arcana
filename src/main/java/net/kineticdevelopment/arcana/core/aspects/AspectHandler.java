package net.kineticdevelopment.arcana.core.aspects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

/**
 * Implement this interface as a capability which should handle aspects, especially storing
 * vis. This should not be used for essentia (which doesn't exist yet anyways).
 */
public interface AspectHandler extends INBTSerializable<NBTTagCompound>{
	
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
	int insert(Aspect aspect, int amount, boolean simulate);
	
	/**
	 * Gets the current amount of vis of a given aspect stored in this handler.
	 *
	 * @param aspect
	 * 		The aspect to test for.
	 * @return The amount of that aspect stored.
	 */
	int getCurrentVis(Aspect aspect);
	
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
	int drain(Aspect aspect, int amount, boolean simulate);
	
	/**
	 * Returns whether any non-zero amount of that aspect can be currently inserted.
	 *
	 * @param aspect
	 * 		The aspect to test for.
	 * @return If that aspect can be inserted.
	 */
	boolean canInsert(Aspect aspect);
	
	/**
	 * Returns whether any non-zero amount of that aspect can be stored at any point,
	 * regardless of this handler's current stored amount of that aspect.
	 *
	 * @param aspect
	 * 		The aspect to test for.
	 * @return If that aspect can be stored at any point..
	 */
	boolean canStore(Aspect aspect);
	
	/**
	 * Returns the maximum amount of vis of an aspect that can exist within
	 * this handler.
	 *
	 * @param aspect
	 * 		The aspect to test for.
	 * @return The maximum amount of that aspect that can be inserted.
	 */
	int getCapacity(Aspect aspect);
	
	/**
	 * Returns the maximum amount of vis of any type that is accepted by this
	 * handler that can exist in this handler.
	 *
	 * @return The maximum amount of vis stored by this handler.
	 */
	int getCapacity();
	
	/**
	 * Returns a set containing all aspects that are allowed in this handler.`canStore` will return true
	 * for all of these.
	 *
	 * @return All aspects allowed in this handler.
	 */
	Set<Aspect> getAllowedAspects();
	
	/**
	 * Returns a set containing all aspects that are currently stored in this handler.`getCurrentVis` will
	 * return a non-zero amount for all of these.
	 *
	 * @return All aspects allowed in this handler.
	 */
	Set<Aspect> getContainedAspects();
	
	NBTTagCompound serializeNBT();
	
	void deserializeNBT(NBTTagCompound data);
	
	static Optional<AspectHandler> getOptional(@Nonnull ICapabilityProvider holder){
		return Optional.ofNullable(holder.getCapability(AspectHandlerCapability.ASPECT_HANDLER, null));
	}
	
	static AspectHandler getFrom(@Nonnull ICapabilityProvider holder){
		return holder.getCapability(AspectHandlerCapability.ASPECT_HANDLER, null);
	}
}