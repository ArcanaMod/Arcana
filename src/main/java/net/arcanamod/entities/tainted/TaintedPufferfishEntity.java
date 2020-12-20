package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.world.World;

public class TaintedPufferfishEntity extends PufferfishEntity {
	public TaintedPufferfishEntity(EntityType<? extends Entity> p_i50248_1_, World p_i50248_2_) {
		super((EntityType<? extends PufferfishEntity>) p_i50248_1_, p_i50248_2_);
	}
}
