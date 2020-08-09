package net.arcanamod.util.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * \@GenBlockModel
 */
public @interface GBM {

	/**
	 * source() as ResourceLocation.toString()
	 */
	String source() default "";
}
