package net.arcanamod.aspects;

import net.arcanamod.Arcana;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

import static net.arcanamod.Arcana.arcLoc;

public class Aspect {

	private final int id;
	private final String aspectName;
	private final ColorRange colors;
	private final Consumer<Object> aspectTickConsumer;

	public Aspect(String name, ColorRange colors, Consumer<Object> aspectTickConsumer){
		this.aspectName = name;
		this.id = AspectTests.nextAspectId();
		this.colors = colors;
		this.aspectTickConsumer = aspectTickConsumer;
	}

	public void aspectTick(Object sender){
		if (aspectTickConsumer!=null)
			aspectTickConsumer.accept(sender);
	}

	public int getId() {
		return id;
	}

	public ColorRange getColorRange() {
		return colors;
	}

	public String name() {
		return aspectName;
	}

	@Override
	public String toString() {
		return "Aspect: "+aspectName+" ("+id+")";
	}

	// Static Methods

	public static Aspect create(String name, ColorRange colors, Consumer<Object> aspectTickConsumer) {
		Aspect aspect = new Aspect(name, colors,aspectTickConsumer);
		Aspects.ASPECTS.put(!AspectTests.test ? arcLoc(name) : new ResourceLocation("arcana_test",name),aspect);
		if (!AspectTests.test)
			Arcana.logger.info("Arcana: Added new aspect '"+name+"'");
		return aspect;
	}

	public static Aspect fromId(int readInt) {
		return Aspects.ASPECTS.values().stream()
				.filter(entry -> entry.getId() == readInt)
				.findAny().orElse(Aspects.EMPTY);
	}
}