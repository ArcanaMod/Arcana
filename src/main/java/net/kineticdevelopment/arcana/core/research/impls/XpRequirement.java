package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.Requirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class XpRequirement extends Requirement{
	
	public static final ResourceLocation TYPE = new ResourceLocation(Main.MODID, "xp");
	
	public boolean satisfied(EntityPlayer player){
		return player.experienceLevel >= getAmount();
	}
	
	public void take(EntityPlayer player){
		player.experienceLevel -= getAmount();
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public NBTTagCompound data(){
		return new NBTTagCompound();
	}
}