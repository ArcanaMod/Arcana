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

	private List<IAspectHolder> cells = new ArrayList<>();
	private int maxCells;
	private int defaultCellSize;

	public AspectBattery() {
		this(1,100);
	}

	public AspectBattery(int maxCells, int defaultCellSize) {
		this.maxCells = maxCells;
		this.defaultCellSize = defaultCellSize;
	}

	public void createCell(AspectCell cell){
		if (getHoldersAmount() != maxCells)
			cells.add(cell);
	}

	public void deleteCell(AspectCell cell){
		if (getHoldersAmount() > 0)
			cells.remove(cell);
	}

	@Deprecated
	public void setCellAtIndex(int index, AspectCell cell){
		replaceCell(index, cell);
	}

	public void replaceCell(int index, AspectCell cell){
		if (index >= cells.size())
			cells.add(index,cell);
		else
			cells.set(index,cell);
	}

	/**
	 * Returns the number of aspects storage units ("cells") available
	 *
	 * @return The number of cells available
	 */
	@Override
	public int getHoldersAmount() {
		return cells.size();
	}

	/**
	 * Gets List of IAspectHolders
	 *
	 * @return List of IAspectHolders
	 */
	@Override
	public List<IAspectHolder> getHolders() {
		return cells;
	}

	/**
	 * Gets IAspectHolder by index. If cell doesn't exist is automatically created.
	 *
	 * @param index index of holder.
	 * @return IAspectHolder.
	 */
	@Override
	public IAspectHolder getHolder(int index) {
		if (index >= cells.size())
			cells.add(index,new AspectCell(defaultCellSize));
		return cells.get(index);
	}

	/**
	 * Inserts AspectStack that contains Aspect and Amount. If cell doesn't exist is automatically created.
	 *
	 * @param holder   index of a holder.
	 * @param resource AspectStack to insert.
	 * @param simulate Is Simulating?
	 * @return Inserted amount
	 */
	@Override
	public int insert(int holder, AspectStack resource, boolean simulate) {
		if (holder >= cells.size())
			cells.add(holder,new AspectCell(defaultCellSize));
		return cells.get(holder).insert(resource,simulate);
	}

	/**
	 * Inserts amount of existing AspectStack inside. If cell doesn't exist is automatically created.
	 *
	 * @param holder    index of a holder.
	 * @param maxInsert amount to insert.
	 * @param simulate  Is Simulating?
	 * @return Inserted amount
	 */
	@Override
	public int insert(int holder, int maxInsert, boolean simulate) {
		if (holder >= cells.size())
			cells.add(holder,new AspectCell(defaultCellSize));
		return cells.get(holder).insert(new AspectStack(cells.get(holder).getContainedAspect(),maxInsert),simulate);
	}

	/**
	 * Drains AspectStack that contains Aspect and Amount.
	 *
	 * @param holder   index of a holder.
	 * @param resource AspectStack to drain.
	 * @param simulate Is Simulating?
	 * @return Drained amount
	 */
	@Override
	public int drain(int holder, AspectStack resource, boolean simulate) {
		if (holder >= cells.size())
			return 0;
		return cells.get(holder).drain(resource,simulate);
	}

	/**
	 * Drains amount of existing AspectStack inside.
	 *
	 * @param holder   index of a holder.
	 * @param maxDrain amount to drain.
	 * @param simulate Is Simulating?
	 * @return Drained amount
	 */
	@Override
	public int drain(int holder, int maxDrain, boolean simulate) {
		if (holder >= cells.size())
			return 0;
		return cells.get(holder).drain(new AspectStack(cells.get(holder).getContainedAspect(),maxDrain),simulate);
	}

	public CompoundNBT serializeNBT(){
		CompoundNBT compound = new CompoundNBT();
		CompoundNBT storedCells = new CompoundNBT();
		cells.forEach(aspectCell -> storedCells.put("cell_"+cells.indexOf(aspectCell),((AspectCell)aspectCell).toNBT()));
		compound.put("cells",storedCells);
		return compound;
	}

	public void deserializeNBT(CompoundNBT data){
		AspectStack stack = AspectStack.EMPTY;
		CompoundNBT storedCells = data.getCompound("cells");
		int i = 0;
		for(String s : storedCells.keySet()) {
			if (i >= cells.size())
				cells.add(Integer.parseInt(s.replace("cell_", "")), AspectCell.fromNBT(storedCells.getCompound(s)));
			else
				cells.set(Integer.parseInt(s.replace("cell_", "")), AspectCell.fromNBT(storedCells.getCompound(s)));
			i++;
		}
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
		for (IAspectHolder c : cells) {
			cs += ((AspectCell)c).toString();
		}
		return "AspectBattery{" +
				"cells=" + cs +
				'}';
	}
}
