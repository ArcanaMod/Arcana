package net.arcanamod.aspects;

/**
 * AspectColorRange is not necessary but the code is more neat.
 */
public class ColorRange{
	private final int[] colors;

	private ColorRange(int[] colors) {
		this.colors = colors;
	}

	public static ColorRange create(int... colors) {
		return new ColorRange(colors);
	}

	public int[] get() {
		return colors;
	}
}