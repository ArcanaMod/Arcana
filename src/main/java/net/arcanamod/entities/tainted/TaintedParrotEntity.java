package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.world.World;

public class TaintedParrotEntity extends ParrotEntity {
	public TaintedParrotEntity(EntityType<? extends Entity> type, World worldIn) {
		super((EntityType<? extends ParrotEntity>) type, worldIn);
	}
}
