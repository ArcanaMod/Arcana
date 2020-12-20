package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.world.World;

public class TaintedLlamaEntity extends LlamaEntity {
	public TaintedLlamaEntity(EntityType<? extends Entity> p_i50237_1_, World p_i50237_2_) {
		super((EntityType<? extends LlamaEntity>) p_i50237_1_, p_i50237_2_);
	}
}
