package net.arcanamod.aspects;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IOverrideAspects{
	
	List<AspectStack> getAspectStacks(ItemStack stack);
}