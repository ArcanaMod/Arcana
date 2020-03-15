package net.kineticdevelopment.arcana.core.research;

import net.minecraft.entity.player.EntityPlayer;

public abstract class Requirement{
	
	abstract boolean satisfied(EntityPlayer player);
	abstract void take(EntityPlayer player);
}