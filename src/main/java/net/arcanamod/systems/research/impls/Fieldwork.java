package net.arcanamod.systems.research.impls;

import com.google.gson.JsonObject;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.handlers.AspectHandler;
import net.arcanamod.containers.ResearchTableContainer;
import net.arcanamod.containers.slots.AspectSlot;
import net.arcanamod.systems.research.Puzzle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class Fieldwork extends Puzzle{
	
	private static final ResourceLocation ICON = new ResourceLocation(Arcana.MODID, "textures/gui/research/fieldwork.png");
	public static final String TYPE = "fieldwork";
	
	public void load(JsonObject data, ResourceLocation file){
		// no-op
	}
	
	public String type(){
		return TYPE;
	}
	
	public CompoundNBT getData(){
		// no-op
		return new CompoundNBT();
	}
	
	public String getDefaultDesc(){
		return "requirement.fieldwork";
	}
	
	public ResourceLocation getDefaultIcon(){
		return ICON;
	}
	
	public List<Puzzle.SlotInfo> getItemSlotLocations(PlayerEntity player){
		return Collections.emptyList();
	}
	
	public List<AspectSlot> getAspectSlots(Supplier<AspectHandler> returnInv){
		return Collections.emptyList();
	}
	
	public boolean validate(List<AspectSlot> aspectSlots, List<Slot> itemSlots, PlayerEntity player, ResearchTableContainer container){
		return false;
	}
}