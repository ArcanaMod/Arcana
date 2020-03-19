package net.kineticdevelopment.arcana.core.research;

import net.minecraft.entity.player.EntityPlayer;

public abstract class Requirement{
	
	protected int count;
	
	public int getCount(){
		return count;
	}
	
	public abstract boolean satisfied(EntityPlayer player);
	public abstract void take(EntityPlayer player);
}