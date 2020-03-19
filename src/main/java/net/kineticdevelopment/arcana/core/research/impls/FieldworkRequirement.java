package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.Requirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class FieldworkRequirement extends Requirement{
	
	public static final ResourceLocation TYPE = new ResourceLocation(Main.MODID, "fieldwork");
	
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
		return new NBTTagCompound();
	}
}