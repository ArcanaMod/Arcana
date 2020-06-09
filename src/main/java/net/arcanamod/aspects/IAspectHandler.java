package net.arcanamod.aspects;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface IAspectHandler extends INBTSerializable<CompoundNBT> {

	/**
	 * Returns the number of aspects storage units ("cells") available
	 * @return The number of cells available
	 */
	int getCellsAmount();

	void addCell(AspectCell cell);

	List<AspectCell> getCells();

	AspectCell getCell(int index);

	@Nonnull
	AspectStack getAspectStackInCell(int cell);

	int getCellCapacity(int cell);

	int fill(int cell, AspectStack resource);

	int drain(int cell, AspectStack resource);

	int drain(int cell, int maxDrain);

	CompoundNBT serializeNBT();

	void deserializeNBT(CompoundNBT data);

	static Optional<IAspectHandler> getOptional(@Nonnull ICapabilityProvider holder){
		return Optional.of(holder.getCapability(AspectHandlerCapability.ASPECT_HANDLER, null).orElse(null));
	}

	static IAspectHandler getFrom(@Nonnull ICapabilityProvider holder){
		return holder.getCapability(AspectHandlerCapability.ASPECT_HANDLER, null).orElse(null);
	}
}
