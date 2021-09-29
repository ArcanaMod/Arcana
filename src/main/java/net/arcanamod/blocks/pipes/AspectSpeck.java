package net.arcanamod.blocks.pipes;

import net.arcanamod.aspects.AspectStack;
import net.minecraft.util.Direction;

public class AspectSpeck{
	
	public AspectStack payload;    // The aspects contained in the speck.
	public float speed;            // The speed the speck moves, in blocks per second.
	public Direction direction;    // The direction the speck is moving.
	public float pos;              // The position of the speck along the tube.
	
	public AspectSpeck(AspectStack payload, float speed, Direction direction, float pos){
		this.payload = payload;
		this.speed = speed;
		this.direction = direction;
		this.pos = pos;
	}
}