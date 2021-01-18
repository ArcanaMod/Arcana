package net.arcanamod.systems.spell.casts;

import java.util.function.Consumer;

public class DelayedCast {
	public Consumer<Integer> spellEvent;
	public int ticks;
	public int ticksPassed;

	public DelayedCast(Consumer<Integer> spellEvent, int ticks){
		this.spellEvent = spellEvent;
		this.ticks = ticks;
	}
}