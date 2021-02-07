package net.arcanamod.systems.research.impls;

import net.arcanamod.Arcana;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.systems.research.Parent;
import net.arcanamod.systems.research.Requirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class ResearchCompletedRequirement extends Requirement{
	
	public static final ResourceLocation TYPE = new ResourceLocation(Arcana.MODID, "research_completed");
	
	protected Parent req;
	
	public ResearchCompletedRequirement(String req){
		this.req = Parent.parse(req);
	}
	
	public boolean satisfied(PlayerEntity player){
		return req.satisfiedBy(Researcher.getFrom(player));
	}
	
	public void take(PlayerEntity player){
		// no-op
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public CompoundNBT data(){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("requirement", req.asString());
		return compound;
	}
}