package net.arcanamod.aspects;

import java.util.Set;

public interface IAspectHolder {

	/**
	 * Inserts an amount of vis of an aspect, and returns the remainder.
	 *
	 * @param stack
	 * 		Vis to insert.
	 * @param simulate
	 * 		If true, the amount of vis is not actually changed.
	 * @return The amount of vis (that would be) leftover.
	 */
	float insert(AspectStack stack, boolean simulate);

	/**
	 * Gets the current amount of vis of a given aspect stored in this handler.
	 *
	 * @return The amount of that aspect stored.
	 */
	float getCurrentVis();

	/**
	 * Drains an amount of vis of a given aspect from this handler, and returns
	 * the amount removed.
	 *
	 * @param stack
	 * 		The aspect stack to drain.
	 * @param simulate
	 * 		If true, the amount of vis is not actually changed.
	 * @return The amount of vis removed from this handler.
	 */
	float drain(AspectStack stack, boolean simulate);

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
	float getCapacity(Aspect aspect);

	/**
	 * Returns the maximum amount of vis of any type that is accepted by this
	 * handler that can exist in this handler.
	 *
	 * @return The maximum amount of vis stored by this handler.
	 */
	float getCapacity();

	/**
	 * Returns a set containing all aspects that are allowed in this handler.`canStore` will return true
	 * for all of these.
	 *
	 * @return All aspects allowed in this handler.
	 */
	Set<Aspect> getAllowedAspects();

	/**
	 * Returns an AspectStack that contains Aspect with Amount.
	 *
	 * @return AspectStack.
	 */
	AspectStack getContainedAspectStack();

	/**
	 * Returns an Aspect that Holder contains.
	 *
	 * @return Aspect.
	 */
	Aspect getContainedAspect();

	void setCapacity(float defaultCellSize);

	void clear();

	boolean isIgnoringFullness();

	void setIgnoreFullness(boolean ignoreFullness);
	
	boolean canInput();
	
	void setCanInput(boolean canInput);
}