package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.world.World;

public class TaintedCatEntity extends CatEntity {
	public TaintedCatEntity(EntityType<? extends Entity> p_i50284_1_, World p_i50284_2_) {
		super((EntityType<? extends CatEntity>) p_i50284_1_, p_i50284_2_);
	}
}
