package net.arcanamod.util.annotations;

import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.fluids.ArcanaFluids;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.item.ItemStack;

public class AnnotationUtil {
	public static final Class<?>[] targetedClasses = {ArcanaBlocks.class, ArcanaItems.class, ArcanaFluids.class};
}