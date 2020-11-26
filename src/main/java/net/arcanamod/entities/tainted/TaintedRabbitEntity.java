package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.world.World;

public class TaintedRabbitEntity extends RabbitEntity {
	public TaintedRabbitEntity(EntityType<? extends Entity> p_i50247_1_, World p_i50247_2_) {
		super((EntityType<? extends RabbitEntity>) p_i50247_1_, p_i50247_2_);
	}
}
