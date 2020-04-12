package net.kineticdevelopment.arcana.client.research;

import net.kineticdevelopment.arcana.client.research.impls.FieldworkRequirementRenderer;
import net.kineticdevelopment.arcana.client.research.impls.GuessworkRequirementRenderer;
import net.kineticdevelopment.arcana.client.research.impls.ItemRequirementRenderer;
import net.kineticdevelopment.arcana.client.research.impls.XpRequirementRenderer;
import net.kineticdevelopment.arcana.core.research.Requirement;
import net.kineticdevelopment.arcana.core.research.impls.FieldworkRequirement;
import net.kineticdevelopment.arcana.core.research.impls.GuessworkRequirement;
import net.kineticdevelopment.arcana.core.research.impls.ItemRequirement;
import net.kineticdevelopment.arcana.core.research.impls.XpRequirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface RequirementRenderer<T extends Requirement>{
	
	Map<ResourceLocation, RequirementRenderer<?>> map = new LinkedHashMap<>();
	
	static void init(){
		map.put(ItemRequirement.TYPE, new ItemRequirementRenderer());
		map.put(XpRequirement.TYPE, new XpRequirementRenderer());
		map.put(GuessworkRequirement.TYPE, new GuessworkRequirementRenderer());
		map.put(FieldworkRequirement.TYPE, new FieldworkRequirementRenderer());
	}
	
	static <T extends Requirement> RequirementRenderer<T> get(String type){
		return (RequirementRenderer<T>)map.get(type);
	}
	
	static <T extends Requirement> RequirementRenderer<T> get(Requirement type){
		return (RequirementRenderer<T>)map.get(type.type());
	}
	
	void render(int x, int y, T requirement, int ticks, float partialTicks, EntityPlayer player);
	List<String> tooltip(T requirement, EntityPlayer player);
}