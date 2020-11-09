package net.arcanamod.systems.spell.modules;

import net.arcanamod.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static net.arcanamod.util.Pair.of;

public abstract class SpellModule {
	private List<Pair<SpellModule,BindType>> bounded = new ArrayList<>();

	public int getOutputAmount(){
		return 5;
	}

	public int getInputAmount(){
		return 5;
	}

	// addModule
	void bindModule(SpellModule module, BindType type){
		bounded.add(of(module,type));
	}

	// removeModule
	void unbindModule(SpellModule module, BindType type){
		bounded.remove(of(module, type));
	}

	public List<Pair<SpellModule,BindType>> getBoundedModules(){
		return bounded;
	}

	enum BindType{
		INPUT,
		OUTPUT
	}
}
