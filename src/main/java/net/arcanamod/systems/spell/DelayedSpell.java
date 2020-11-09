package net.arcanamod.systems.spell;

import java.util.function.Consumer;

public class DelayedSpell {
	public Consumer<Integer> spellEvent;
	public int ticks;
	public int ticksPassed;

	public DelayedSpell(Consumer<Integer> spellEvent, int ticks){
		this.spellEvent = spellEvent;
		this.ticks = ticks;
	}
}