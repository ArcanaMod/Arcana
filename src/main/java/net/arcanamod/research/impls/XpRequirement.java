package net.arcanamod.research.impls;

import net.arcanamod.Arcana;
import net.arcanamod.research.Requirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class XpRequirement extends Requirement{
	
	public static final ResourceLocation TYPE = new ResourceLocation(Arcana.MODID, "xp");
	
	public boolean satisfied(PlayerEntity player){
		return player.experienceLevel >= getAmount();
	}
	
	public void take(PlayerEntity player){
		player.experienceLevel -= getAmount();
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public CompoundNBT data(){
		return new CompoundNBT();
	}
}