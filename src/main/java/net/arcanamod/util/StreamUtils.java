package net.arcanamod.util;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class StreamUtils{
	
	public static <X extends INBT, Z> Stream<Z> streamAndApply(ListNBT list, Class<X> filterType, Function<X, Z> applicator){
		return list.stream().filter(filterType::isInstance).map(filterType::cast).map(applicator);
	}
}