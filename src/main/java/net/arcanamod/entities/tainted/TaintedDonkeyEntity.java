package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.world.World;

public class TaintedDonkeyEntity extends DonkeyEntity {
	public TaintedDonkeyEntity(EntityType<? extends Entity> p_i50239_1_, World p_i50239_2_) {
		super((EntityType<? extends DonkeyEntity>) p_i50239_1_, p_i50239_2_);
	}
}
