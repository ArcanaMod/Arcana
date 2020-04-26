package net.kineticdevelopment.arcana.core.research.impls;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kineticdevelopment.arcana.common.containers.AspectSlot;
import net.kineticdevelopment.arcana.common.containers.AspectStoreSlot;
import net.kineticdevelopment.arcana.common.containers.ResearchTableContainer;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.aspects.AspectHandler;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.utilities.GraphTraverser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.kineticdevelopment.arcana.core.aspects.Aspects.areAspectsConnected;

public class Chemistry extends Puzzle{
	
	// TODO: actual icon for chemistry
	private static final ResourceLocation ICON = new ResourceLocation(Main.MODID, "textures/item/research_note.png");
	private static final Logger LOGGER = LogManager.getLogger();
	private static int gWidth = 8, gHeight = 6;
	
	public static final String TYPE = "chemistry";
	
	protected List<Aspect> nodes = new ArrayList<>();
	
	@SuppressWarnings("UnstableApiUsage")
	// This is used to determine if the chemistry is complete
	// Populated in validate()
	protected MutableGraph<AspectNode> graph;
	protected AspectNode first;
	
	public Chemistry(){
	}
	
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
	
	private List<AspectSlot> genHexGrid(int gridWidth, int gridHeight, Supplier<AspectHandler> returnInv){
		List<AspectSlot> grid = new ArrayList<>();
		for(int y = 0; y < gridHeight; y++){
			for(int x = 0; x < gridWidth; x++){
				int xx = x * 22 + (y % 2 == 0 ? 11 : 0);
				int yy = y * 19;
				int scX = xx + 141 + (214 - (23 * gridWidth - 2)) / 2;
				int scY = yy + 35 + (134 - (19 * gridHeight + 1)) / 2;
				AspectSlot slot = new AspectStoreSlot(returnInv, scX, scY, 1);
				grid.add(slot);
				if(getAspectInSlot(x + y * gridWidth) != null)
					slot.visible = false;
			}
		}
		return grid;
	}
	
	public List<Puzzle.SlotInfo> getItemSlotLocations(EntityPlayer player){
		return Collections.emptyList();
	}
	
	public List<AspectSlot> getAspectSlots(Supplier<AspectHandler> returnInv){
		return genHexGrid(gWidth, gHeight, returnInv).stream().peek(slot -> slot.x += 2).peek(slot -> slot.y += 2).collect(Collectors.toList());
	}
	
	public boolean validate(List<AspectSlot> puzzleSlots, List<Slot> ignored, EntityPlayer player, ResearchTableContainer container){
		fillGraph(puzzleSlots);
		// now traverse the graph
		AtomicInteger count = new AtomicInteger();
		GraphTraverser.of(graph).traverse((f) -> {
			if(f.fixed)
				count.incrementAndGet();
		}, first);
		return count.get() == nodes.size();
	}
	
	@SuppressWarnings("UnstableApiUsage")
	private void fillGraph(List<AspectSlot> puzzleSlots){
		graph = GraphBuilder.undirected().build();
		first = null;
		// add all the aspects as nodes
		List<AspectNode> nodes = new ArrayList<>();
		int bound = gWidth * gHeight;
		for(int value = 0; value < bound; value++){
			AspectNode aspectNode = new AspectNode(getAt(value, puzzleSlots), getAspectInSlot(value) != null);
			nodes.add(aspectNode);
			if(first == null && aspectNode.fixed)
				first = aspectNode;
		}
		for(AspectNode node : nodes)
			if(node.aspect != null)
				graph.addNode(node);
		for(int y = 0; y < gHeight; y++)
			for(int x = 0; x < gWidth; x++){
				int index = x + y * gWidth;
				AspectNode node = nodes.get(index);
				// a slot is connected to:
				//    a slot next to it (+/-1 X)
				//    a slot right below or right above it (+/-1 Y)
				//    a slot left below or left above it (+/-1 Y, -1 X)
				if(x > 0 && areAspectsConnected(node.aspect, getAt(index - 1, puzzleSlots)))
					// connected left
					graph.putEdge(node, nodes.get(index - 1));
				if(x < gWidth - 1 && areAspectsConnected(node.aspect, getAt(index + 1, puzzleSlots)))
					// connected right
					graph.putEdge(node, nodes.get(index + 1));
				if(y < gHeight - 1){
					int belowInd = x + (y + 1) * gWidth;
					if(areAspectsConnected(node.aspect, getAt(belowInd, puzzleSlots)))
						// connected BL
						// or BR, idk
						graph.putEdge(node, nodes.get(belowInd));
					if(y % 2 == 0){
						if(belowInd + 1 < gWidth * gHeight && areAspectsConnected(node.aspect, getAt(belowInd + 1, puzzleSlots)))
							// connected BR
							graph.putEdge(node, nodes.get(belowInd + 1));
					}else if(x > 0 && areAspectsConnected(node.aspect, getAt(belowInd - 1, puzzleSlots)))
						// connected BL
						graph.putEdge(node, nodes.get(belowInd - 1));
				}
				if(y > 0){
					int aboveInd = x + (y - 1) * gWidth;
					if(areAspectsConnected(node.aspect, getAt(aboveInd, puzzleSlots)))
						// connected UL
						// or UR
						graph.putEdge(node, nodes.get(aboveInd));
					if(y % 2 == 0){
						if(x > 0 && areAspectsConnected(node.aspect, getAt(aboveInd + 1, puzzleSlots)))
							// connected UR
							graph.putEdge(node, nodes.get(aboveInd + 1));
					}else if(x > 0 && areAspectsConnected(node.aspect, getAt(aboveInd - 1, puzzleSlots)))
						// connected UL
						graph.putEdge(node, nodes.get(aboveInd - 1));
				}
			}
	}
	
	private Aspect getAt(int index, List<AspectSlot> puzzleSlots){
		Aspect slot = getAspectInSlot(index);
		if(slot != null)
			return slot;
		else
			return puzzleSlots.get(index).getAspect();
	}
	
	// TODO: this code (and everything below it) could absolutely be made better
	public Aspect getAspectInSlot(int slot){
		if(getAspectSlotIndexes().contains(slot))
			return nodes.get(getAspectSlotIndexes().indexOf(slot));
		else
			return null;
	}
	
	private List<Integer> getAspectSlotIndexes(){
		// there are 24 slots on the edges
		// with nodes.size() aspects to distribute equally among these slots
		int spacing = (int)Math.floor(24 / (float)nodes.size());
		if(spacing < 0 || spacing > 24)
			return Collections.emptyList();
		List<Integer> ret = new ArrayList<>();
		for(int i = 0; i < nodes.size(); i++)
			ret.add(getEdgeSlotIndex(spacing * i));
		return ret;
	}
	
	private int getEdgeSlotIndex(int rad){
		// the Xth edge slot
		if(rad <= 7)
			return rad;
		else if(rad <= 12)
			return (rad - 6) * 8 - 1;
		else if(rad <= 19)
			return 40 + (7 - (rad - 12));
		else if(rad <= 23)
			return 32 - ((rad - 20) * 8);
		return 0;
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
	
	private static class AspectNode{
		public Aspect aspect;
		public boolean fixed;
		
		public AspectNode(Aspect aspect, boolean fixed){
			this.aspect = aspect;
			this.fixed = fixed;
		}
		
		public String toString(){
			return "AspectNode{aspect=" + aspect + ", fixed=" + fixed + '}';
		}
	}
}