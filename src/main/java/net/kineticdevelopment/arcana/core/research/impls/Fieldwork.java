package net.kineticdevelopment.arcana.core.research.impls;

import com.google.gson.JsonObject;
import net.kineticdevelopment.arcana.common.containers.AspectSlot;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.aspects.AspectHandler;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class Fieldwork extends Puzzle{
	
	private static final ResourceLocation ICON = new ResourceLocation(Main.MODID, "textures/gui/research/fieldwork.png");
	public static final String TYPE = "fieldwork";
	
	public void load(JsonObject data, ResourceLocation file){
		// no-op
	}
	
	public String type(){
		return TYPE;
	}
	
	public NBTTagCompound getData(){
		// no-op
		return new NBTTagCompound();
	}
	
	public String getDefaultDesc(){
		return "requirement.fieldwork";
	}
	
	public ResourceLocation getDefaultIcon(){
		return ICON;
	}
	
	public List<Pair<Integer, Integer>> getItemSlotLocations(EntityPlayer player){
		return Collections.emptyList();
	}
	
	public List<AspectSlot> getAspectSlots(Supplier<AspectHandler> returnInv){
		return Collections.emptyList();
	}
	
	public boolean validate(List<AspectSlot> aspectSlots, List<Slot> itemSlots){
		return false;
	}
}