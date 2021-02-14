package net.arcanamod.systems.spell.casts;

import net.arcanamod.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ToggleableCast {
	public static List<Pair<UUID, ToggleableCast.Impl>> toggleableCasts = new ArrayList<>();

	public static class Impl {
		public Consumer<Integer> spellEvent;
		public int ticks;
		public int ticksPassed;

		public Impl(Consumer<Integer> spellEvent, int ticks) {
			this.spellEvent = spellEvent;
			this.ticks = ticks;
		}
	}
}
