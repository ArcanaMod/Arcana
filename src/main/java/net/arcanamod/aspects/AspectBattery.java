package net.arcanamod.aspects;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AspectBattery implements ICapabilityProvider, IAspectHandler {

	private List<AspectCell> cells = new ArrayList<>();
	private int maxCells;

	public AspectBattery() {
		this(1);
	}

	public AspectBattery(int maxCells) {
		this.maxCells = maxCells;
	}

	public void addCell(AspectCell cell){
		if (maxCells != getCellsAmount())
			cells.add(cell);
	}

	public List<AspectCell> getCells() {
		return cells;
	}

	public AspectCell getCell(int index) {
		return cells.get(index);
	}

	/**
	 * Returns the number of aspects storage units ("cells") available
	 *
	 * @return The number of cells available
	 */
	@Override
	public int getCellsAmount() {
		return cells.size();
	}

	@Nonnull
	@Override
	public AspectStack getAspectStackInCell(int cell) {
		return cells.get(cell).getContainedAspectStack();
	}

	@Override
	public int getCellCapacity(int cell) {
		return cells.get(cell).getCapacity();
	}

	@Override
	public int fill(int cell, AspectStack resource) {
		return cells.get(cell).insert(resource,false);
	}

	@Nonnull
	@Override
	public int drain(int cell, AspectStack resource) {
		return cells.get(cell).drain(resource,false);
	}

	@Nonnull
	@Override
	public int drain(int cell, int maxDrain) {
		return cells.get(cell).drain(new AspectStack(cells.get(cell).getContainedAspectStack().getAspect(),maxDrain),false);
	}

	public CompoundNBT serializeNBT(){
		CompoundNBT compound = new CompoundNBT();
		CompoundNBT storedCells = new CompoundNBT();
		cells.forEach(aspectCell -> storedCells.put("cell_"+cells.indexOf(aspectCell),aspectCell.toNBT()));
		compound.put("cells",storedCells);
		return compound;
	}

	public void deserializeNBT(CompoundNBT data){
		AspectStack stack = AspectStack.EMPTY;
		CompoundNBT storedCells = data.getCompound("cells");
		for(String s : storedCells.keySet())
			cells.set(Integer.parseInt(s),AspectCell.fromNBT(storedCells.getCompound("cell_"+s)));
	}

	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing){
		return capability == AspectHandlerCapability.ASPECT_HANDLER;
	}

	@Nullable
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing){
		return capability == AspectHandlerCapability.ASPECT_HANDLER ? LazyOptional.of(() -> (T)this) : LazyOptional.empty();
	}

	@Override
	public String toString() {
		String cs = "";
		for (AspectCell c : cells)
			cs += c.toString();
		return "AspectBattery{" +
				"cells=" + cs +
				'}';
	}
}
