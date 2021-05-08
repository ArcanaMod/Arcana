package net.arcanamod.client.research;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.client.research.impls.*;
import net.arcanamod.systems.research.impls.*;
import net.arcanamod.systems.research.Requirement;
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
		map.put(ItemTagRequirement.TYPE, new ItemTagRequirementRenderer());
		map.put(XpRequirement.TYPE, new XpRequirementRenderer());
		map.put(PuzzleRequirement.TYPE, new PuzzleRequirementRenderer());
		map.put(ResearchCompletedRequirement.TYPE, new ResearchCompletedRequirementRenderer());
		map.put(PuzzlesCompletedRequirement.TYPE, new PuzzlesCompletedRequirementRenderer());
	}
	
	static <T extends Requirement> RequirementRenderer<T> get(String type){
		return (RequirementRenderer<T>)map.get(new ResourceLocation(type));
	}
	
	static <T extends Requirement> RequirementRenderer<T> get(Requirement type){
		return (RequirementRenderer<T>)map.get(type.type());
	}
	
	void render(MatrixStack matrices, int x, int y, T requirement, int ticks, float partialTicks, PlayerEntity player);
	
	List<ITextComponent> tooltip(T requirement, PlayerEntity player);
	
	default boolean shouldDrawTickOrCross(T requirement, int amount){
		return amount == 1;
	}
}