package net.arcanamod.util.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GIM {
	
	Type value();

	/**
	 * source() as ResourceLocation.toString()
	 */
	String source() default "";

	enum Type{
		ITEM,
		BLOCK_REF
	}
}
