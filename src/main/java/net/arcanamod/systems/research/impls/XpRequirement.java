package net.arcanamod.systems.research.impls;

import net.arcanamod.systems.research.Requirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import static net.arcanamod.Arcana.arcLoc;

public class XpRequirement extends Requirement{
	
	public static final ResourceLocation TYPE = arcLoc("xp");
	
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