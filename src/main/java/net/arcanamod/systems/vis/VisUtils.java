package net.arcanamod.systems.vis;

import net.arcanamod.aspects.*;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

// TODO: this stuff should be fine in AspectHandler but that's currently a little messy so its here rn
public final class VisUtils{
	
	/** Sorts empty aspect holders after full ones. */
	public static final Comparator<IAspectHolder> EMPTY_SORTER = (a, b) -> a.getCurrentVis() == 0 ? (b.getCurrentVis() == 0 ? 0 : 1) : (b.getCurrentVis() == 0 ? -1 : 0);
	/** Sorts voiding aspect holders after non-voiding aspect holders. */
	public static final Comparator<IAspectHolder> VOID_SORTER = (a, b) -> a.isIgnoringFullness() ? (b.isIgnoringFullness() ? 0 : 1) : (b.isIgnoringFullness() ? -1 : 0);
	/** Sorts non-empty non-voiding aspect holders first, then empty aspect holders, then voiding aspect holders. */
	public static final Comparator<IAspectHolder> INPUT_PRIORITY_SORTER = VOID_SORTER.thenComparing(EMPTY_SORTER);
	
	/**
	 * Moves som eor all aspects from <code>from</code> to <code>to</code>.
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
	public static void moveAllAspects(@Nullable IAspectHandler from, @Nullable IAspectHandler to, float max){
		if(from == null || to == null)
			return;
		int transferred = 0;
		List<IAspectHolder> fromHolders = from.getHolders();
		for(IAspectHolder holder : fromHolders){
			if(transferred >= max && max != -1)
				break;
			if(holder.getCurrentVis() > 0){
				List<IAspectHolder> holders = to.getHolders();
				// move void cells, then empty cells to the end
				holders.sort(INPUT_PRIORITY_SORTER);
				for(IAspectHolder toHolder : holders){
					// disallow self insertions
					// you shouldn't be transferring from a NotifyingHolder to a NotifyingHolder
					if(from.getHolders().contains(toHolder instanceof DelegatingAspectCell ? ((DelegatingAspectCell)toHolder).underlying() : toHolder) && to.getHolders().contains(holder instanceof DelegatingAspectCell ? ((DelegatingAspectCell)holder).underlying() : toHolder))
						continue;
					if(transferred >= max && max != -1)
						break;
					if(toHolder.getContainedAspect() == holder.getContainedAspect() || toHolder.getContainedAspect() == Aspects.EMPTY)
						if(toHolder.canInput() && (toHolder.getCapacity() > toHolder.getCurrentVis() || toHolder.isIgnoringFullness())){
							float toInsert = (int)(holder.getCurrentVis());
							if(!toHolder.isIgnoringFullness())
								toInsert = Math.min(toInsert, toHolder.getCapacity() - toHolder.getCurrentVis());
							if(max != -1)
								toInsert = Math.min(toInsert, max - transferred);
							AspectStack stack = new AspectStack(holder.getContainedAspect(), toInsert);
							transferred += holder.drain(stack, false);
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
	public static void moveAspects(IAspectHolder from, IAspectHandler to, float max){
		int transferred = 0;
		if(from.getCurrentVis() > 0){
			List<IAspectHolder> holders = to.getHolders();
			// move void cells, then empty cells to the end
			holders.sort(INPUT_PRIORITY_SORTER);
			for(IAspectHolder toHolder : holders){
				if(transferred >= max && max != -1)
					break;
				if(toHolder.getContainedAspect() == from.getContainedAspect() || toHolder.getContainedAspect() == Aspects.EMPTY)
					if(toHolder.canInput() && (toHolder.getCapacity() > toHolder.getCurrentVis() || toHolder.isIgnoringFullness())){
						float toInsert = (float)Math.floor(from.getCurrentVis());
						if(!toHolder.isIgnoringFullness())
							toInsert = Math.min(toInsert, toHolder.getCapacity() - toHolder.getCurrentVis());
						if(max != -1)
							toInsert = Math.min(toInsert, max - transferred);
						AspectStack stack = new AspectStack(from.getContainedAspect(), toInsert);
						transferred += from.drain(stack, false);
						toHolder.insert(stack, false);
					}
			}
		}
	}
	
	public static void moveAspects(AspectStack in, IAspectHandler to, float max){
		moveAspects(in.getAspect(), in.getAmount(), to, max);
	}
	
	public static void moveAspects(Aspect in, float amount, IAspectHandler to, float max){
		int transferred = 0;
		if(amount > 0){
			List<IAspectHolder> holders = to.getHolders();
			// move void cells, then empty cells to the end
			holders.sort(INPUT_PRIORITY_SORTER);
			for(IAspectHolder toHolder : holders){
				if(transferred >= max && max != -1)
					break;
				if(toHolder.getContainedAspect() == in || toHolder.getContainedAspect() == Aspects.EMPTY)
					if(toHolder.canInput() && (toHolder.getCapacity() > toHolder.getCurrentVis() || toHolder.isIgnoringFullness())){
						float toInsert = (float)Math.floor(amount);
						if(!toHolder.isIgnoringFullness())
							toInsert = Math.min(toInsert, toHolder.getCapacity() - toHolder.getCurrentVis());
						if(max != -1)
							toInsert = Math.min(toInsert, max - transferred);
						AspectStack stack = new AspectStack(toHolder.getContainedAspect(), toInsert);
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