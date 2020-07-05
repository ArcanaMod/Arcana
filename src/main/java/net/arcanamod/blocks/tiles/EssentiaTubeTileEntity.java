package net.arcanamod.blocks.tiles;

import com.google.common.collect.Sets;
import net.arcanamod.aspects.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import java.util.*;

public class EssentiaTubeTileEntity extends TileEntity implements ICapabilityProvider, IAspectHandler{
	
	List<IAspectHolder> cells = new ArrayList<>();
	
	public EssentiaTubeTileEntity(){
		super(ArcanaTiles.ESSENTIA_TUBE_TE.get());
		//scan(Sets.newHashSet(getPos()));
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
	
	public void scan(Set<BlockPos> notified){
		// don't notify things `notified`
		notified.add(getPos());
		// but scan everything yourself
		Set<BlockPos> scanned = Sets.newConcurrentHashSet();
		Set<BlockPos> endAspectHandlers = Sets.newConcurrentHashSet();
		Set<BlockPos> scanning = Sets.newHashSet();
		Set<BlockPos> toScan = Sets.newHashSet();
		// add my immediate neighbors to `scanning`
		// then iterate through it (excluding members of scanned); add each block to `scanned` so we don't repeat
		// if its a pipe, tell it to scan and add its neighbors to `scanning`
		// if its a non-pipe aspect handler, add to `endAspectHandlers`
		addNeighborsToSet(getPos(), toScan);
		while(!toScan.isEmpty()){
			scanning.addAll(toScan);
			toScan.clear();
			for(BlockPos blockPos : scanning){
				if(!scanned.contains(blockPos)){
					scanned.add(blockPos);
					TileEntity tile = getWorld().getTileEntity(blockPos);
					if(tile != null){
						if(tile instanceof EssentiaTubeTileEntity){
							EssentiaTubeTileEntity entity = (EssentiaTubeTileEntity)tile;
							if(!notified.contains(blockPos)){
								entity.scan(new HashSet<>(notified));
								notified.add(blockPos);
							}
							addNeighborsToSet(blockPos, toScan);
						}else if(tile.getCapability(AspectHandlerCapability.ASPECT_HANDLER).isPresent())
							endAspectHandlers.add(blockPos);
					}
				}
			}
			scanning.removeAll(scanned);
		}
		// then we just expose all of the endAspectHandler's cells
		// TODO: wrap them with info on the route taken - just "last positions iterated" - to make windows work
		cells.clear();
		for(BlockPos handler : endAspectHandlers){
			IAspectHandler handle = IAspectHandler.getFrom(getWorld().getTileEntity(handler));
			if(handle != null)
				cells.addAll(handle.getHolders());
		}
	}
	
	private void addNeighborsToSet(BlockPos pos, Set<BlockPos> total){
		// up, down, north 2, south 1, east 1, west 2
		total.addAll(Arrays.asList(pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()));
	}
	
	// Aspect Handler
	
	public int getHoldersAmount(){
		return cells.size();
	}
	
	public List<IAspectHolder> getHolders(){
		return new ArrayList<>(cells);
	}
	
	public IAspectHolder getHolder(int index){
		return cells.get(index);
	}
	
	public boolean exist(int index){
		return cells.size() > index;
	}
	
	public void createCell(IAspectHolder cell){
		throw new UnsupportedOperationException("Tried to add a new aspect cell to a pipe!");
	}
	
	public void deleteCell(IAspectHolder cell){
		throw new UnsupportedOperationException("Tried to remove an aspect cell from a pipe!");
	}
	
	public void deleteCell(int index){
		throw new UnsupportedOperationException("Tried to remove an aspect cell from a pipe!");
	}
	
	public void setCellSizes(){
		throw new UnsupportedOperationException("Tried to modify pipe aspect cell data!");
	}
	
	public int insert(int holder, AspectStack resource, boolean simulate){
		// if an exception occurs, it's your fault
		return cells.get(holder).insert(resource, simulate);
	}
	
	public int insert(int holder, int maxInsert, boolean simulate){
		// if an exception occurs, it's your fault
		return cells.get(holder).insert(new AspectStack(cells.get(holder).getContainedAspect(), maxInsert), simulate);
	}
	
	public int drain(int holder, AspectStack resource, boolean simulate){
		if(holder >= cells.size())
			return 0;
		return cells.get(holder).drain(resource, simulate);
	}
	
	public int drain(int holder, int maxDrain, boolean simulate){
		if(holder >= cells.size())
			return 0;
		return cells.get(holder).drain(new AspectStack(cells.get(holder).getContainedAspect(), maxDrain), simulate);
	}
	
	public void clear(){
		throw new UnsupportedOperationException("Tried to clear an aspect pipe!");
	}
	
	public IAspectHolder findAspectInHolders(Aspect aspect){
		for(IAspectHolder cell : cells){
			if(cell.getContainedAspect() == aspect)
				return cell;
		}
		return null;
	}
	
	public int[] findIndexesFromAspectInHolders(Aspect aspect){
		List<Integer> indexes = new ArrayList<>();
		for(IAspectHolder cell : cells){
			if(cell.getContainedAspect() == aspect)
				indexes.add(cells.indexOf(cell));
		}
		return ArrayUtils.toPrimitive(indexes.toArray(new Integer[0]));
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
}