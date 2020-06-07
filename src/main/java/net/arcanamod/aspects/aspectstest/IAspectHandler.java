package net.arcanamod.aspects.aspectstest;


import javax.annotation.Nonnull;

public interface IAspectHandler {
	enum AspectAction {
		EXECUTE, SIMULATE;

		public boolean execute() {
			return this == EXECUTE;
		}

		public boolean simulate() {
			return this == SIMULATE;
		}
	}

	/**
	 * Returns the number of aspect storage units ("tanks") available
	 *
	 * @return The number of tanks available
	 */
	int getTanks();

	/**
	 * Returns the AspectStack in a given tank.
	 *
	 * <p>
	 * <strong>IMPORTANT:</strong> This AspectStack <em>MUST NOT</em> be modified. This method is not for
	 * altering internal contents. Any implementers who are able to detect modification via this method
	 * should throw an exception. It is ENTIRELY reasonable and likely that the stack returned here will be a copy.
	 * </p>
	 *
	 * <p>
	 * <strong><em>SERIOUSLY: DO NOT MODIFY THE RETURNED FLUIDSTACK</em></strong>
	 * </p>
	 *
	 * @param tank Tank to query.
	 * @return AspectStack in a given tank. AspectStack.EMPTY if the tank is empty.
	 */
	@Nonnull
	AspectStack getAspectInTank(int tank);

	/**
	 * Retrieves the maximum aspect amount for a given tank.
	 *
	 * @param tank Tank to query.
	 * @return     The maximum aspect amount held by the tank.
	 */
	int getTankCapacity(int tank);

	/**
	 * This function is a way to determine which aspects can exist inside a given handler. General purpose tanks will
	 * basically always return TRUE for this.
	 *
	 * @param tank  Tank to query for validity
	 * @param stack Stack to test with for validity
	 * @return TRUE if the tank can hold the AspectStack, not considering current state.
	 * (Basically, is a given aspect EVER allowed in this tank?) Return FALSE if the answer to that question is 'no.'
	 */
	boolean isAspectValid(int tank, @Nonnull AspectStack stack);

	/**
	 * Fills aspect into internal tanks, distribution is left entirely to the IAspectHandler.
	 *
	 * @param resource AspectStack representing the Aspect and maximum amount of aspect to be filled.
	 * @param action   If SIMULATE, fill will only be simulated.
	 * @return Amount of resource that was (or would have been, if simulated) filled.
	 */
	int fill(AspectStack resource, IAspectHandler.AspectAction action);

	/**
	 * Drains aspect out of internal tanks, distribution is left entirely to the IAspectHandler.
	 *
	 * @param resource AspectStack representing the Aspect and maximum amount of aspect to be drained.
	 * @param action   If SIMULATE, drain will only be simulated.
	 * @return AspectStack representing the Aspect and amount that was (or would have been, if
	 * simulated) drained.
	 */
	@Nonnull
	AspectStack drain(AspectStack resource, IAspectHandler.AspectAction action);

	/**
	 * Drains aspect out of internal tanks, distribution is left entirely to the IAspectHandler.
	 * <p/>
	 * This method is not Aspect-sensitive.
	 *
	 * @param maxDrain Maximum amount of aspect to drain.
	 * @param action   If SIMULATE, drain will only be simulated.
	 * @return AspectStack representing the Aspect and amount that was (or would have been, if
	 * simulated) drained.
	 */
	@Nonnull
	AspectStack drain(int maxDrain, IAspectHandler.AspectAction action);
}
