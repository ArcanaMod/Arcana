package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.Requirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GuessworkRequirement extends Requirement{
	
	public static final ResourceLocation TYPE = new ResourceLocation(Main.MODID, "guesswork");
	
	protected int id;
	
	public GuessworkRequirement(int id){
		this.id = id;
	}
	
	public boolean satisfied(EntityPlayer player){
		return false;
	}
	
	public void take(EntityPlayer player){
		// no-op
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public NBTTagCompound data(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("id", id);
		return compound;
	}
}