package net.arcanamod.blocks.pipes;

import net.arcanamod.aspects.AspectStack;
import net.minecraft.util.Direction;

public class AspectSpeck{
	
	AspectStack payload;    // The aspects contained in the speck.
	float speed;            // The speed the speck moves, in blocks per second.
	Direction direction;    // The direction the speck is moving.
	float pos;              // The position of the speck along the tube.
}