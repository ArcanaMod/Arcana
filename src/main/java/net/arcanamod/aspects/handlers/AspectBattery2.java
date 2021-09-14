package net.arcanamod.aspects.handlers;

import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;

public class AspectBattery2 implements AspectHandler2{
	
	private List<AspectHolder2> holders = new ArrayList<>();
	
	public List<AspectHolder2> getHolders(){
		return holders;
	}
	
	public CompoundNBT serializeNBT(){
		CompoundNBT compound = new CompoundNBT();
		CompoundNBT storedCells = new CompoundNBT();
		holders.forEach(holder -> storedCells.put("holder_" + holders.indexOf(holder), holder.serializeNBT()));
		compound.put("holders", storedCells);
		return compound;
	}
	
	public void deserializeNBT(CompoundNBT data){
		CompoundNBT storedCells = data.getCompound("cells");
		int i = 0;
		for(String s : storedCells.keySet()){
			if(i >= holders.size())
				holders.add(AspectCell2.fromNbt(storedCells.getCompound(s)));
			else
				holders.set(Integer.parseInt(s.substring(7)), AspectCell2.fromNbt(storedCells.getCompound(s)));
			i++;
		}
	}
}