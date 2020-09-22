package net.arcanamod.systems.spell;

import java.util.HashMap;

// TODO: replace with CompoundNBT. This has no reason to exist, makes reading code more difficult, and really makes no sense.
public class SpellExtraData {
	private final HashMap<String, Integer> data = new HashMap<>();

	public void write(String name, int value){
		data.put(name,value);
	}

	public int read(String name){
		return data.getOrDefault(name,0);
	}

	public boolean contains(String name){
		return data.containsKey(name);
	}
}
