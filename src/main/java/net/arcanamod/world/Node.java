package net.arcanamod.world;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import net.arcanamod.aspects.Aspect;
import net.minecraft.dispenser.IPosition;
import net.minecraft.world.IWorld;

// implements position for BlockPos constructor convenience
public interface Node extends IPosition{
	
	// might as well pick the fast version
	Reference2IntMap<Aspect> aspects();
	
	NodeType type();
	
	void tick(IWorld world);
}