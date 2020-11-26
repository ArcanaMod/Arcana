package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;

public class TaintedBeeEntity extends BeeEntity {
	public TaintedBeeEntity(EntityType<? extends Entity> type, World world) {
		super((EntityType<? extends BeeEntity>) type, world);
	}
}
