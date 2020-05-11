package net.arcanamod.client.research;

import net.arcanamod.client.research.impls.PuzzleRequirementRenderer;
import net.arcanamod.client.research.impls.XpRequirementRenderer;
import net.arcanamod.research.impls.PuzzleRequirement;
import net.arcanamod.client.research.impls.ItemRequirementRenderer;
import net.arcanamod.research.Requirement;
import net.arcanamod.research.impls.ItemRequirement;
import net.arcanamod.research.impls.XpRequirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RequirementRenderer<T extends Requirement>{
	
	Map<ResourceLocation, RequirementRenderer<?>> map = new HashMap<>();
	
	static void init(){
		map.put(ItemRequirement.TYPE, new ItemRequirementRenderer());
		map.put(XpRequirement.TYPE, new XpRequirementRenderer());
		map.put(PuzzleRequirement.TYPE, new PuzzleRequirementRenderer());
	}
	
	static <T extends Requirement> RequirementRenderer<T> get(String type){
		return (RequirementRenderer<T>)map.get(new ResourceLocation(type));
	}
	
	static <T extends Requirement> RequirementRenderer<T> get(Requirement type){
		return (RequirementRenderer<T>)map.get(type.type());
	}
	
	void render(int x, int y, T requirement, int ticks, float partialTicks, PlayerEntity player);
	
	List<ITextComponent> tooltip(T requirement, PlayerEntity player);
}