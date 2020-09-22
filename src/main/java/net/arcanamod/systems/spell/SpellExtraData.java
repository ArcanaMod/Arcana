package net.arcanamod.systems.spell;

import java.util.HashMap;

// I know that CompoundNBT exist but I don't like a lot of spaghetti in code
// :irritater:
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
