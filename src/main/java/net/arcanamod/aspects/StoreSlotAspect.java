package net.arcanamod.aspects;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple implementation of {@link IAspectHandler} that can store a single aspect up to a set amount.
 */
public class StoreSlotAspect implements IAspectHandler, IAspectHolder, ICapabilityProvider{
	
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing){
		return capability == AspectHandlerCapability.ASPECT_HANDLER;
	}
	
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing){
		return capability == AspectHandlerCapability.ASPECT_HANDLER ? LazyOptional.of(() -> (T)this) : LazyOptional.empty();
	}
	
	public Aspect stored;
	public int held;
	int capacity;
	
	public StoreSlotAspect(){
		this(100);
	}
	
	public StoreSlotAspect(int capacity){
		this.capacity = capacity;
	}

	//IAspectHolder

	/**
	 * Inserts an amount of vis of an aspect, and returns the remainder.
	 *
	 * @param stack    Vis to insert.
	 * @param simulate If true, the amount of vis is not actually changed.
	 * @return The amount of vis (that would be) leftover.
	 */
	@Override
	public int insert(AspectStack stack, boolean simulate) {
		Aspect _stored = stored;
		if(_stored == null)
			_stored = stack.getAspect();
		if(_stored != stack.getAspect())
			return stack.getAmount();
		int capacityRemaining = getCapacity(stack.getAspect()) - getCurrentVis();
		if(stack.getAmount() <= capacityRemaining){
			if(!simulate){
				held = getCurrentVis() + stack.getAmount();
				stored = _stored;
			}
			return 0;
		}else{
			if(!simulate){
				held = getCapacity(stack.getAspect());
				stored = _stored;
			}
			return stack.getAmount() - capacityRemaining;
		}
	}

	/**
	 * Gets the current amount of vis of a given aspect stored in this handler.
	 *
	 * @return The amount of that aspect stored.
	 */
	@Override
	public int getCurrentVis() {
		return held;
	}

	/**
	 * Drains an amount of vis of a given aspect from this handler, and returns
	 * the amount removed.
	 *
	 * @param stack    The aspect stack to drain.
	 * @param simulate If true, the amount of vis is not actually changed.
	 * @return The amount of vis removed from this handler.
	 */
	@Override
	public int drain(AspectStack stack, boolean simulate) {
		int vis = getCurrentVis();
		if(stack.getAmount() >= vis){
			if(!simulate){
				held = 0;
				stored = null;
			}
			return vis;
		}else{
			if(!simulate)
				held = vis - stack.getAmount();
			return stack.getAmount();
		}
	}

	public boolean canInsert(Aspect aspect){
		return aspect == stored && held < capacity;
	}
	
	public boolean canStore(Aspect aspect){
		return true;
	}
	
	public int getCapacity(Aspect aspect){
		return capacity;
	}
	
	public int getCapacity(){
		return capacity;
	}
	
	public Set<Aspect> getAllowedAspects(){
		return new LinkedHashSet<>(Aspect.aspects);
	}

	/**
	 * Returns an AspectStack that contains Aspect with Amount.
	 *
	 * @return AspectStack.
	 */
	@Override
	public AspectStack getContainedAspectStack() {
		return new AspectStack(stored,held);
	}

	/**
	 * Returns an Aspect that Holder contains.
	 *
	 * @return Aspect.
	 */
	@Override
	public Aspect getContainedAspect() {
		return stored;
	}

	@Override
	public void setCapacity(int defaultCellSize) {
		capacity = defaultCellSize;
	}

	public Set<Aspect> getContainedAspects(){
		return Collections.singleton(stored);
	}

	//IAspectHandler

	/**
	 * Returns the number of aspects storage units ("cells") available
	 *
	 * @return The number of cells available
	 */
	@Override
	public int getHoldersAmount() {
		return 1;
	}

	/**
	 * Gets List of IAspectHolders
	 *
	 * @return List of IAspectHolders
	 */
	@Override
	public List<IAspectHolder> getHolders() {
		return Collections.singletonList((IAspectHolder) this);
	}

	/**
	 * Gets IAspectHolder by index.
	 *
	 * @param index index of holder.
	 * @return IAspectHolder.
	 */
	@Override
	public IAspectHolder getHolder(int index) {
		return this;
	}

	@Override
	public void createCell(IAspectHolder cell) {

	}

	@Override
	public void deleteCell(IAspectHolder cell) {

	}

	@Override
	public void setCellSizes() {

	}

	/**
	 * Inserts AspectStack that contains Aspect and Amount.
	 *
	 * @param holder   index of a holder.
	 * @param resource AspectStack to insert.
	 * @param simulate If true, the amount of vis is not actually changed.
	 * @return Inserted amount
	 */
	@Override
	public int insert(int holder, AspectStack resource, boolean simulate) {
		return insert(resource, simulate);
	}

	/**
	 * Inserts amount of existing AspectStack inside.
	 *
	 * @param holder    index of a holder.
	 * @param maxInsert amount to insert.
	 * @param simulate  If true, the amount of vis is not actually changed.
	 * @return Inserted amount
	 */
	@Override
	public int insert(int holder, int maxInsert, boolean simulate) {
		return insert(holder, new AspectStack(stored,maxInsert), simulate);
	}

	/**
	 * Drains AspectStack that contains Aspect and Amount.
	 *
	 * @param holder   index of a holder.
	 * @param resource AspectStack to drain.
	 * @param simulate If true, the amount of vis is not actually changed.
	 * @return Drained amount
	 */
	@Override
	public int drain(int holder, AspectStack resource, boolean simulate) {
		return drain(resource,simulate);
	}

	/**
	 * Drains amount of existing AspectStack inside.
	 *
	 * @param holder   index of a holder.
	 * @param maxDrain amount to drain.
	 * @param simulate If true, the amount of vis is not actually changed.
	 * @return Drained amount
	 */
	@Override
	public int drain(int holder, int maxDrain, boolean simulate) {
		return drain(holder, new AspectStack(stored,maxDrain), simulate);
	}

	@Override
	public void clear() {

	}

	@Override
	public IAspectHolder findAspectInHolders(Aspect aspect) {
		return this;
	}

	@Override
	public int findIndexFromAspectInHolders(Aspect aspect) {
		return 0;
	}

	public CompoundNBT serializeNBT(){
		CompoundNBT compound = new CompoundNBT();
		CompoundNBT storedAspects = new CompoundNBT();
		storedAspects.putInt(stored != null ? stored.name().toLowerCase() : "null", held);
		compound.put("stored", storedAspects);
		compound.putInt("capacity", capacity);
		return compound;
	}
	
	public void deserializeNBT(CompoundNBT data){
		CompoundNBT storedAspects = data.getCompound("stored");
		for(String s : storedAspects.keySet()){
			if(s != null && !s.equals("null")){
				stored = Aspect.valueOf(s.toUpperCase());
				held = storedAspects.getInt(s);
			}
		}
		capacity = data.getInt("capacity");
	}
}