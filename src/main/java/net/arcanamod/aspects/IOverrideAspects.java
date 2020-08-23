package net.arcanamod.aspects;

import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public interface IOverrideAspects {
	List<AspectStack> getAspectStacks(ItemStack stack);

	static List<AspectStack> setSpecialOverrideType(SpecialOverrideType type){
		return Collections.singletonList(type == SpecialOverrideType.NONE ? AspectStack.EMPTY : new AspectStack(Aspect.createDummy()));
	}

	enum SpecialOverrideType{
		DEFAULT,
		NONE
	}
}
