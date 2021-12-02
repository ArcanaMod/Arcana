package net.arcanamod.blocks.pipes;

import net.arcanamod.aspects.AspectStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;

public class AspectSpeck{
	
	public AspectStack payload;    // The aspects contained in the speck.
	public float speed;            // The speed the speck moves, in blocks per second.
	public Direction direction;    // The direction the speck is moving.
	public float pos;              // The position of the speck along the tube.
	
	public boolean stuck = false;  // Whether the speck if forced to move in a direction it can't. Not saved.
	
	public AspectSpeck(AspectStack payload, float speed, Direction direction, float pos){
		this.payload = payload;
		this.speed = speed;
		this.direction = direction;
		this.pos = pos;
	}
	
	@Nonnull
	public CompoundNBT toNbt(){
		CompoundNBT tag = new CompoundNBT();
		tag.put("payload", payload.toNbt());
		tag.putFloat("speed", speed);
		tag.putInt("direction", direction.getIndex());
		tag.putFloat("pos", pos);
		return tag;
	}
	
	@Nonnull
	public static AspectSpeck fromNbt(@Nonnull CompoundNBT tag){
		return new AspectSpeck(AspectStack.fromNbt(tag.getCompound("payload")), tag.getFloat("speed"), Direction.byIndex(tag.getInt("direction")), tag.getFloat("pos"));
	}
}