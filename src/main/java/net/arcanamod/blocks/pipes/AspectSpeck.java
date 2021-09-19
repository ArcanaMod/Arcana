package net.arcanamod.blocks.pipes;

import net.arcanamod.aspects.AspectStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;

public class AspectSpeck{
	
	AspectStack payload;    // The aspects contained in the speck
	float speed;            // The speed the speck moves
	Direction direction;    // The direction the speck is moving
	Vector3d pos;           // The position of the speck, relative to the position of the pipe's centre.
}