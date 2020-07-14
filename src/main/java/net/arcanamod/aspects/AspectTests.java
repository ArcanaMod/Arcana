package net.arcanamod.aspects;

/**
 * Avoids org.junit.Test Crashes
 */
public class AspectTests{

	/**
	 * Enable this if you are using junit tests
	 */
	public static final boolean test = false;

	private static int pNextAspectId = 0;

	@Deprecated
	public static int nextAspectId() {
		return pNextAspectId++;
	}
}