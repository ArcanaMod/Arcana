package net.arcanamod.util;

import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.aspects.IAspectHolder;

// TODO: this stuff should be fine in AspectHandler but that's currently a little messy so its here rn
public final class VisUtils{
	
	/**
	 * Moves all aspects from <code>from</code> to <code>to</code>.
	 * Iterates through every holder in <code>from</code> and tries to empty them into every holder in <code>to</code>.
	 *
	 * @param from
	 * 		The aspect handler to draw aspects from.
	 * @param to
	 * 		The aspect handler to insert into.
	 */
	public static void moveAllAspects(IAspectHandler from, IAspectHandler to){
		for(IAspectHolder holder : from.getHolders())
			if(holder.getCurrentVis() > 0)
				for(IAspectHolder toHolder : to.getHolders())
					if(toHolder.getContainedAspect() == holder.getContainedAspect() || toHolder.getContainedAspect() == Aspects.EMPTY)
						if(toHolder.getCapacity() > toHolder.getCurrentVis()){
							int toInsert = Math.min(holder.getCurrentVis(), toHolder.getCapacity() - toHolder.getCurrentVis());
							holder.drain(new AspectStack(holder.getContainedAspect(), toInsert), false);
							toHolder.insert(new AspectStack(holder.getContainedAspect(), toInsert), false);
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
	public static void moveAspects(IAspectHolder from, IAspectHandler to){
		if(from.getCurrentVis() > 0)
			for(IAspectHolder toHolder : to.getHolders())
				if(toHolder.getContainedAspect() == from.getContainedAspect() || toHolder.getContainedAspect() == Aspects.EMPTY)
					if(toHolder.getCapacity() > toHolder.getCurrentVis()){
						int toInsert = Math.min(from.getCurrentVis(), toHolder.getCapacity() - toHolder.getCurrentVis());
						from.drain(new AspectStack(from.getContainedAspect(), toInsert), false);
						toHolder.insert(new AspectStack(from.getContainedAspect(), toInsert), false);
					}
	}
}