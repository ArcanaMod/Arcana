package net.arcanamod.systems.spell.modules;

import net.arcanamod.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static net.arcanamod.util.Pair.of;

public abstract class SpellModule {
	private List<SpellModule> bounded = new ArrayList<>();

	public int getOutputAmount(){
		return 5;
	}

	public int getInputAmount(){
		return 5;
	}

	public abstract boolean canConnect(SpellModule connectingModule);

	// addModule
	public void bindModule(SpellModule module){
		bounded.add(module);
	}

	// removeModule
	public void unbindModule(SpellModule module){
		bounded.remove(module);
	}

	public List<SpellModule> getBoundedModules(){
		return bounded;
	}
}
