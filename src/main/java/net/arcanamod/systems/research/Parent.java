package net.arcanamod.systems.research;

import net.arcanamod.capabilities.Researcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Parent{
	
	ResourceLocation entry;
	int stage = -1;
	boolean showArrowhead = true, showLine = true, reverseLine = false;
	
	public static Logger LOGGER = LogManager.getLogger();
	
	public Parent(ResourceLocation entry){
		this.entry = entry;
	}
	
	public static Parent parse(String text){
		Parent parent = new Parent(null);
		String original = text;
		// Check for prefixes
		// ~ for no line, & for no arrowheads, / for reversed
		// @ for stage
		if(text.startsWith("~")){
			text = text.substring(1);
			parent.showLine = false;
		}
		if(text.startsWith("&")){
			text = text.substring(1);
			parent.showArrowhead = false;
		}
		if(text.startsWith("/")){
			text = text.substring(1);
			parent.reverseLine = true;
		}
		if(text.contains("@")){
			String[] sections = text.split("@");
			text = sections[0];
			try{
				parent.stage = Integer.parseUnsignedInt(sections[1]);
			}catch(NumberFormatException exception){
				LOGGER.error("Invalid entry stage \"" + sections[1] + "\" found in parent \"" + original + "\"!");
			}
		}
		try{
			parent.entry = new ResourceLocation(text);
		}catch(ResourceLocationException exception){
			LOGGER.error("Invalid entry \"" + text + "\" found in parent \"" + original + "\"!");
		}
		return parent;
	}
	
	public String asString(){
		return (!showLine ? "~" : "") + (!showArrowhead ? "&" : "")+ (reverseLine ? "/" : "") + entry + (stage != -1 ? "@" + stage : "");
	}
	
	public ResourceLocation getEntry(){
		return entry;
	}
	
	public int getStage(){
		return stage;
	}
	
	public boolean shouldShowArrowhead(){
		return showArrowhead;
	}
	
	public boolean shouldShowLine(){
		return showLine;
	}
	
	public boolean shouldReverseLine(){
		return reverseLine;
	}
	
	public boolean satisfiedBy(Researcher r){
		ResearchEntry entry = ResearchBooks.getEntry(getEntry());
		if(entry == null)
			return true;
		if(entry.meta().contains("locked"))
			return false;
		return stage == -1 ? r.entryStage(entry) >= entry.sections().size() : r.entryStage(entry) >= stage;
	}
}