package net.arcanamod.aspects;

/**
 * Stores a set of colours.
 */
public class ColorRange{
	
	private final int[] colors;

	private ColorRange(int[] colors) {
		this.colors = colors;
	}

	public static ColorRange create(int... colors) {
		return new ColorRange(colors);
	}
	
	public int get(int colour){
		return colors[Math.min(colour, colors.length - 1)];
	}
}