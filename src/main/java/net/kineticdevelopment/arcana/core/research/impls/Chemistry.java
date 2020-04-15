package net.kineticdevelopment.arcana.core.research.impls;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Chemistry extends Puzzle{
	
	// TODO: actual icon for chemistry
	private static final ResourceLocation ICON = new ResourceLocation(Main.MODID, "textures/item/research_note.png");
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String TYPE = "chemistry";
	
	protected List<Aspect> nodes = new ArrayList<>();
	
	public Chemistry(){}
	
	public Chemistry(List<Aspect> nodes){
		this.nodes = nodes;
	}
	
	public void load(JsonObject data, ResourceLocation file){
		JsonArray nodeArray = data.getAsJsonArray("nodes");
		for(JsonElement node : nodeArray){
			if(node.isJsonPrimitive()){
				String nodeSt = node.getAsString();
				try{
					nodes.add(Aspect.valueOf(nodeSt.toUpperCase()));
				}catch(IllegalArgumentException ignored){
					LOGGER.error("Invalid aspect \"" + nodeSt + "\" found in file" + file + "! (Aspects are not case sensitive; check for misspellings.)");
				}
			}else
				LOGGER.error("Non-String found in nodes array in puzzle in " + file + "!");
		}
	}
	
	public NBTTagCompound getData(){
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList nodeList = new NBTTagList();
		for(Aspect node : nodes)
			nodeList.appendTag(new NBTTagString(node.name()));
		compound.setTag("nodes", nodeList);
		return compound;
	}
	
	public static Chemistry fromNBT(NBTTagCompound passData){
		List<Aspect> nodes = new ArrayList<>();
		for(NBTBase node : passData.getTagList("nodes", Constants.NBT.TAG_STRING))
			nodes.add(Aspect.valueOf(((NBTTagString)node).getString()));
		return new Chemistry(nodes);
	}
	
	public String type(){
		return TYPE;
	}
	
	public String getDefaultDesc(){
		return "requirement.chemistry";
	}
	
	public ResourceLocation getDefaultIcon(){
		return ICON;
	}
}