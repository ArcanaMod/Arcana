package net.arcanamod.blocks.tiles;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.DelegatingAspectCell;
import net.arcanamod.aspects.VisShareable;
import net.arcanamod.aspects.handlers.AspectHandler;
import net.arcanamod.aspects.handlers.AspectHandlerCapability;
import net.arcanamod.aspects.handlers.AspectHolder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

public class AspectTubeTileEntity extends TileEntity implements ICapabilityProvider, AspectHandler{
	
	// might not work anymore but its due for replacement anyways
	
	List<AspectHolder> cells = new ArrayList<>();
	transient boolean initScanned = false;
	
	public AspectTubeTileEntity(){
		this(ArcanaTiles.ASPECT_TUBE_TE.get());
	}
	
	public AspectTubeTileEntity(TileEntityType<?> type){
		super(type);
	}
	
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side){
		if (cap == AspectHandlerCapability.ASPECT_HANDLER)
			return LazyOptional.of(() -> this).cast();
		return LazyOptional.empty();
	}
	
	// This will be fun
	// Look for all connected aspect handlers
	// Get all of their cells
	// Store a wrapped version of each cell that remembers the route there
	// On actual (non-simulated) insertion or draining, notify pipes e.g. window pipes
	// On neighbor change, if that neighbor is an aspect handler, redo the cell list and propagate signal to other pipes
	// unless the neighbor is a new pipe, because the constructor scanning does that
	
	@SuppressWarnings("ConstantConditions")
	public void scan(Set<BlockPos> notified){
		initScanned = true;
		// don't notify things `notified`
		notified.add(getPos());
		// but scan everything yourself
		Collection<ScanStack> scanned = Sets.newConcurrentHashSet();
		Collection<ScanStack> endAspectHandlers = Sets.newConcurrentHashSet();
		List<ScanStack> scanning = Lists.newArrayList();
		Collection<ScanStack> toScan = Sets.newHashSet();
		// add my immediate neighbors to `scanning`
		// then iterate through it (excluding members of scanned); add each block to `scanned` so we don't repeat
		// if its a pipe, tell it to scan and add its neighbors to `scanning`
		// if its a non-pipe aspect handler, add to `endAspectHandlers`
		addNeighborsToSet(new ScanStack(getPos()), toScan);
		while(!toScan.isEmpty()){
			scanning.addAll(toScan);
			toScan.clear();
			for(ScanStack blockPos : scanning){
				if(!scanned.contains(blockPos)){
					scanned.add(blockPos);
					TileEntity tile = getWorld().getTileEntity(blockPos.pos);
					if(tile != null){
						if(tile instanceof AspectTubeTileEntity){
							AspectTubeTileEntity entity = (AspectTubeTileEntity)tile;
							if(!notified.contains(blockPos.pos)){
								entity.scan(notified);
								notified.add(blockPos.pos);
							}
							if(entity.enabled())
								addNeighborsToSetExcluding(blockPos, toScan, scanned);
						}else if(tile.getCapability(AspectHandlerCapability.ASPECT_HANDLER).isPresent())
							if (!(tile instanceof VisShareable) || !((VisShareable) tile).isManual())
								endAspectHandlers.add(blockPos);
					}
				}
			}
			scanning.removeAll(scanned);
		}
		// then we just expose all of the endAspectHandler's cells
		cells.clear();
		for(ScanStack handler : endAspectHandlers){
			AspectHandler handle = AspectHandler.getFrom(getWorld().getTileEntity(handler.pos));
			if(handle != null)
				//cells.addAll(handle.getHolders());
				for(AspectHolder holder : handle.getHolders())
					cells.add(new NotifyingHolder(holder, handler.to));
		}
	}
	
	private void addNeighborsToSet(ScanStack pos, Collection<ScanStack> total){
		// up, down, north 2, south 1, east 1, west 2
		total.addAll(Arrays.asList(pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()));
	}
	
	private void addNeighborsToSetExcluding(ScanStack pos, Collection<ScanStack> total, Collection<ScanStack> excluded){
		// up, down, north 2, south 1, east 1, west 2
		Set<ScanStack> c = Sets.newHashSet(pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west());
		c.removeAll(excluded);
		total.addAll(c);
	}
	
	boolean enabled(){
		return true;
	}
	
	void notifyAspect(Aspect aspect){}
	
	// Aspect Handler
	
	private void checkScan(){
		if(!initScanned)
			scan(Sets.newHashSet(getPos()));
	}
	
	public int countHolders(){
		checkScan();
		return cells.size();
	}
	
	public List<AspectHolder> getHolders(){
		checkScan();
		return new ArrayList<>(cells);
	}
	
	public float insert(Aspect in, float amount){
		checkScan();
		return AspectHandler.super.insert(in, amount);
	}
	
	public float drain(Aspect aspect, float amount){
		checkScan();
		return AspectHandler.super.drain(aspect, amount);
	}
	
	public AspectStack drainAny(float amount){
		checkScan();
		return AspectHandler.super.drainAny(amount);
	}
	
	public void deserializeNBT(CompoundNBT nbt){
		// no-op
		// we scan on construction
	}
	
	public CompoundNBT serializeNBT(){
		// no-op
		// we store nothing
		return new CompoundNBT();
	}
	
	private static class ScanStack{
		BlockPos pos;
		Collection<BlockPos> to;
		
		public ScanStack(BlockPos pos, Collection<BlockPos> to){
			this.pos = pos;
			this.to = to;
		}
		
		public ScanStack(BlockPos pos){
			this.pos = pos;
			to = new ArrayList<>();
			to.add(pos);
		}
		
		public boolean equals(Object o){
			if(this == o)
				return true;
			if(o == null || getClass() != o.getClass())
				return false;
			ScanStack stack = (ScanStack)o;
			return pos.equals(stack.pos);
		}
		
		public int hashCode(){
			return Objects.hash(pos);
		}
		
		ScanStack up(){
			ArrayList<BlockPos> newStack = new ArrayList<>(to);
			newStack.add(pos.up());
			return new ScanStack(pos.up(), newStack);
		}
		ScanStack down(){
			ArrayList<BlockPos> newStack = new ArrayList<>(to);
			newStack.add(pos.down());
			return new ScanStack(pos.down(), newStack);
		}
		ScanStack north(){
			ArrayList<BlockPos> newStack = new ArrayList<>(to);
			newStack.add(pos.north());
			return new ScanStack(pos.north(), newStack);
		}
		ScanStack south(){
			ArrayList<BlockPos> newStack = new ArrayList<>(to);
			newStack.add(pos.south());
			return new ScanStack(pos.south(), newStack);
		}
		ScanStack east(){
			ArrayList<BlockPos> newStack = new ArrayList<>(to);
			newStack.add(pos.east());
			return new ScanStack(pos.east(), newStack);
		}
		ScanStack west(){
			ArrayList<BlockPos> newStack = new ArrayList<>(to);
			newStack.add(pos.west());
			return new ScanStack(pos.west(), newStack);
		}
	}
	
	private class NotifyingHolder implements AspectHolder, DelegatingAspectCell{
		
		private AspectHolder cell;
		private Collection<BlockPos> notifying;
		
		public NotifyingHolder(AspectHolder cell, Collection<BlockPos> notifying){
			this.cell = cell;
			this.notifying = notifying;
		}
		
		private void tryNotify(AspectStack stack, boolean simulate){
			if(!stack.isEmpty() && !simulate)
				notifying.forEach(pos -> {
					TileEntity entity = getWorld().getTileEntity(pos);
					if(entity instanceof AspectTubeTileEntity)
						((AspectTubeTileEntity)entity).notifyAspect(stack.getAspect());
				});
		}
		
		public AspectHolder underlying(){
			return cell;
		}
		
		public float insert(float amount, boolean simulate){
			tryNotify(new AspectStack(cell.getStack().getAspect(), amount), simulate);
			return cell.insert(amount, simulate);
		}
		
		public float drain(float amount, boolean simulate){
			tryNotify(new AspectStack(cell.getStack().getAspect(), amount), simulate);
			return cell.drain(amount, simulate);
		}
		
		public AspectStack getStack(){
			return cell.getStack();
		}
		
		public float getCapacity(){
			return cell.getCapacity();
		}
		
		public List<Aspect> getWhitelist(){
			return cell.getWhitelist();
		}
		
		public boolean voids(){
			return cell.voids();
		}
		
		public boolean canInsert(){
			return cell.canInsert();
		}
		
		public Consumer<Float> overfillingCallback(){
			return cell.overfillingCallback();
		}
		
		public void setStack(AspectStack stack){
			cell.setStack(stack);
		}
		
		public void setCapacity(float capacity){
			cell.setCapacity(capacity);
		}
		
		public void setWhitelist(List<Aspect> whitelist){
			cell.setWhitelist(whitelist);
		}
		
		public void setVoids(boolean voids){
			cell.setVoids(voids);
		}
		
		public void setCanInsert(boolean canInsert){
			cell.setCanInsert(canInsert);
		}
		
		public void setOverfillingCallback(Consumer<Float> callback){
			cell.setOverfillingCallback(callback);
		}
		
		public CompoundNBT serializeNBT(){
			return cell.serializeNBT();
		}
		
		public void deserializeNBT(CompoundNBT data){
			cell.deserializeNBT(data);
		}
	}

	@Override
	public String toString() {
		return "AspectTubeTileEntity{" +
				"cells=" + cells +
				", pos=" + pos +
				'}';
	}
}