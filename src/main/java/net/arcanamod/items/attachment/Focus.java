package net.arcanamod.items.attachment;

import net.arcanamod.items.ArcanaItems;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Focus{
	
	List<Focus> FOCI = new ArrayList<>();
	Impl NO_FOCUS = new Impl(ArcanaItems.arcLoc("no_focus"));
	
	static Optional<Focus> getFocusById(int id){
		if(id < 0 || id >= FOCI.size())
			return Optional.empty();
		else
			return Optional.of(FOCI.get(id));
	}
	
	ResourceLocation getModelLocation();
	
	class Impl implements Focus{
		
		ResourceLocation modelLocation;
		
		public Impl(ResourceLocation modelLocation){
			this.modelLocation = modelLocation;
			FOCI.add(this);
		}
		
		public ResourceLocation getModelLocation(){
			return modelLocation;
		}
	}
}