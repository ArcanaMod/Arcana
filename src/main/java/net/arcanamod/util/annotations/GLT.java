package net.arcanamod.util.annotations;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * \@GenLootTable
 */
public @interface GLT {
	/**
	 * replacement() Item as ResourceLocation.toString()
	 */
	String replacement() default "";
}
