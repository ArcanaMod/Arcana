package net.arcanamod.aspects.aspectstest;

import javax.annotation.Nonnull;

public interface IAspectTank {

	/**
	 * @return AspectStack representing the aspect in the tank, null if the tank is empty.
	 */
	@Nonnull
	AspectStack getAspect();

	/**
	 * @return Current amount of aspect in the tank.
	 */
	int getAspectAmount();

	/**
	 * @return Capacity of this aspect tank.
	 */
	int getCapacity();

	/**
	 * @param stack Aspectstack holding the Aspect to be queried.
	 * @return If the tank can hold the aspect (EVER, not at the time of query).
	 */
	boolean isAspectValid(AspectStack stack);

	/**
	 * @param resource AspectStack attempting to fill the tank.
	 * @param action   If SIMULATE, the fill will only be simulated.
	 * @return Amount of aspect that was accepted (or would be, if simulated) by the tank.
	 */
	int fill(AspectStack resource, IAspectHandler.AspectAction action);

	/**
	 * @param maxDrain Maximum amount of aspect to be removed from the container.
	 * @param action   If SIMULATE, the drain will only be simulated.
	 * @return Amount of aspect that was removed (or would be, if simulated) from the tank.
	 */
	@Nonnull
	AspectStack drain(int maxDrain, IAspectHandler.AspectAction action);

	/**
	 * @param resource Maximum amount of aspect to be removed from the container.
	 * @param action   If SIMULATE, the drain will only be simulated.
	 * @return AspectStack representing aspect that was removed (or would be, if simulated) from the tank.
	 */
	@Nonnull
	AspectStack drain(AspectStack resource, IAspectHandler.AspectAction action);
}
