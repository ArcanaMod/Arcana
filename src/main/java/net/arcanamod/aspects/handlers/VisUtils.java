package net.arcanamod.aspects.handlers;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.DelegatingAspectCell;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

public final class VisUtils{
	
	/** Sorts empty aspect holders after full ones. */
	public static final Comparator<AspectHolder> EMPTY_SORTER = (a, b) -> a.getStack().getAmount() == 0 ? (b.getStack().getAmount() == 0 ? 0 : 1) : (b.getStack().getAmount() == 0 ? -1 : 0);
	/** Sorts voiding aspect holders after non-voiding aspect holders. */
	public static final Comparator<AspectHolder> VOID_SORTER = (a, b) -> a.voids() ? (b.voids() ? 0 : 1) : (b.voids() ? -1 : 0);
	/** Sorts non-empty non-voiding aspect holders first, then empty aspect holders, then voiding aspect holders. */
	public static final Comparator<AspectHolder> INPUT_PRIORITY_SORTER = VOID_SORTER.thenComparing(EMPTY_SORTER);
	
	/** Sorts empty aspect holders after full ones. */
	public static final Comparator<AspectHolder> EMPTY_SORTER2 = (a, b) -> a.getStack().getAmount() == 0 ? (b.getStack().getAmount() == 0 ? 0 : 1) : (b.getStack().getAmount() == 0 ? -1 : 0);
	/** Sorts voiding aspect holders after non-voiding aspect holders. */
	public static final Comparator<AspectHolder> VOID_SORTER2 = (a, b) -> a.voids() ? (b.voids() ? 0 : 1) : (b.voids() ? -1 : 0);
	/** Sorts non-empty non-voiding aspect holders first, then empty aspect holders, then voiding aspect holders. */
	public static final Comparator<AspectHolder> INPUT_PRIORITY_SORTER2 = VOID_SORTER2.thenComparing(EMPTY_SORTER2);
	
	/**
	 * Moves some or all aspects from <code>from</code> to <code>to</code>.
	 * Iterates through every holder in <code>from</code> and tries to empty them into every holder in <code>to</code>.
	 * If max is set to -1, up to all aspects will be moved. Otherwise, only up to max aspects will be moved.
	 *
	 * @param from
	 * 		The aspect handler to draw aspects from.
	 * @param to
	 * 		The aspect handler to insert into.
	 * @param max
	 *      The maximum amount of aspects to move, or -1 for no limit.
	 */
	public static void moveAllAspects(@Nullable AspectHandler from, @Nullable AspectHandler to, float max){
		if(from == null || to == null)
			return;
		int transferred = 0;
		List<AspectHolder> fromHolders = from.getHolders();
		for(AspectHolder holder : fromHolders){
			if(transferred >= max && max != -1)
				break;
			if(holder.getStack().getAmount() > 0){
				List<AspectHolder> holders = to.getHolders();
				// move void cells, then empty cells to the end
				holders.sort(INPUT_PRIORITY_SORTER);
				for(AspectHolder toHolder : holders){
					// disallow self insertions
					// you shouldn't be transferring from a NotifyingHolder to a NotifyingHolder
					if(from.getHolders().contains(toHolder instanceof DelegatingAspectCell ? ((DelegatingAspectCell)toHolder).underlying() : toHolder) && to.getHolders().contains(holder instanceof DelegatingAspectCell ? ((DelegatingAspectCell)holder).underlying() : toHolder))
						continue;
					if(transferred >= max && max != -1)
						break;
					if(toHolder.getStack().getAspect() == holder.getStack().getAspect() || toHolder.getStack().getAspect() == Aspects.EMPTY)
						if(toHolder.canInsert() && (toHolder.getCapacity() > toHolder.getStack().getAmount() || toHolder.voids())){
							float toInsert = (int)(holder.getStack().getAmount());
							if(!toHolder.voids())
								toInsert = Math.min(toInsert, toHolder.getCapacity() - toHolder.getStack().getAmount());
							if(max != -1)
								toInsert = Math.min(toInsert, max - transferred);
							AspectStack stack = new AspectStack(holder.getStack().getAspect(), toInsert);
							transferred += holder.drain(toInsert, false);
							toHolder.insert(stack, false);
						}
				}
			}
		}
	}
	
	/**
	 * Moves all aspects from <code>from</code> to <code>to</code>.
	 * Iterates through every holder in <code>from</code> and tries to empty them into every holder in <code>to</code>.
	 *
	 * @param from
	 * 		The aspect holder to draw aspects from.
	 * @param to
	 * 		The aspect handler to insert into.
	 */
	public static void moveAspects(AspectHolder from, AspectHandler to, float max){
		int transferred = 0;
		if(from.getStack().getAmount() > 0){
			List<AspectHolder> holders = to.getHolders();
			// move void cells, then empty cells to the end
			holders.sort(INPUT_PRIORITY_SORTER);
			for(AspectHolder toHolder : holders){
				if(transferred >= max && max != -1)
					break;
				if(toHolder.getStack().getAspect() == from.getStack().getAspect() || toHolder.getStack().getAspect() == Aspects.EMPTY)
					if(toHolder.canInsert() && (toHolder.getCapacity() > toHolder.getStack().getAmount() || toHolder.voids())){
						float toInsert = (float)Math.floor(from.getStack().getAmount());
						if(!toHolder.voids())
							toInsert = Math.min(toInsert, toHolder.getCapacity() - toHolder.getStack().getAmount());
						if(max != -1)
							toInsert = Math.min(toInsert, max - transferred);
						AspectStack stack = new AspectStack(from.getStack().getAspect(), toInsert);
						transferred += from.drain(toInsert, false);
						toHolder.insert(stack, false);
					}
			}
		}
	}
	
	public static void moveAspects(AspectStack in, AspectHandler to, float max){
		moveAspects(in.getAspect(), in.getAmount(), to, max);
	}
	
	public static void moveAspects(Aspect in, float amount, AspectHandler to, float max){
		int transferred = 0;
		if(amount > 0){
			List<AspectHolder> holders = to.getHolders();
			// move void cells, then empty cells to the end
			holders.sort(INPUT_PRIORITY_SORTER);
			for(AspectHolder toHolder : holders){
				if(transferred >= max && max != -1)
					break;
				if(toHolder.getStack().getAspect() == in || toHolder.getStack().getAspect() == Aspects.EMPTY)
					if(toHolder.canInsert() && (toHolder.getCapacity() > toHolder.getStack().getAmount() || toHolder.voids())){
						float toInsert = (float)Math.floor(amount);
						if(!toHolder.voids())
							toInsert = Math.min(toInsert, toHolder.getCapacity() - toHolder.getStack().getAmount());
						if(max != -1)
							toInsert = Math.min(toInsert, max - transferred);
						AspectStack stack = new AspectStack(toHolder.getStack().getAspect(), toInsert);
						if(toInsert >= amount){
							transferred += amount;
							amount = 0;
						}else{
							transferred += toInsert;
							amount -= toInsert;
						}
						toHolder.insert(stack, false);
					}
			}
		}
	}
}