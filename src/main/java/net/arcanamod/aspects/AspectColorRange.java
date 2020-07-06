package net.arcanamod.aspects;

/**
 * AspectColorRange is not necessary but the code is more neat.
 */
public class AspectColorRange {
	private final int[] colors;

	private AspectColorRange(int[] colors) {
		this.colors = colors;
	}

	public static AspectColorRange create(int... colors) {
		return new AspectColorRange(colors);
	}

	public int[] get() {
		return colors;
	}
}