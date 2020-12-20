package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.world.World;

public class TaintedPolarBearEntity extends PolarBearEntity {
	public TaintedPolarBearEntity(EntityType<? extends Entity> p_i50249_1_, World p_i50249_2_) {
		super((EntityType<? extends PolarBearEntity>) p_i50249_1_, p_i50249_2_);
	}
}
