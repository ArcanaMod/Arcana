package net.kineticdevelopment.arcana.api.exception;

/**
 * A Throw for when an aspect cannot be found
 * @author Atlas
 *
 */
public class AspectNotFoundException extends Exception {
	private static final long serialVersionUID = 2519768298187590267L;
	
	public AspectNotFoundException() {
	}
	
	public AspectNotFoundException(String message) {
		super(message);
	}
}
