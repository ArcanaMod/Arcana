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

public class Thaumaturgy extends Puzzle {
	private static final ResourceLocation ICON = new ResourceLocation(Arcana.MODID, "textures/item/research_note.png");
	public static final String TYPE = "thaumaturgy";

	public Thaumaturgy(){}

	@Override
	public void load(JsonObject data, ResourceLocation file) {

	}

	@Override
	public String type() {
		return TYPE;
	}

	@Override
	public CompoundNBT getData() {
		return new CompoundNBT();
	}

	@Override
	public String getDefaultDesc() {
		return "requirement.thaumaturgy";
	}

	@Override
	public ResourceLocation getDefaultIcon() {
		return ICON;
	}

	@Override
	public List<SlotInfo> getItemSlotLocations(PlayerEntity player) {
		return Collections.emptyList();
	}

	@Override
	public List<AspectSlot> getAspectSlots(Supplier<AspectHandler> returnInv) {
		return Collections.emptyList();
	}

	@Override
	public boolean validate(List<AspectSlot> aspectSlots, List<Slot> itemSlots, PlayerEntity player, ResearchTableContainer container) {
		return false;
	}

	public static Thaumaturgy fromNBT(CompoundNBT passData){
		return new Thaumaturgy();
	}
}
