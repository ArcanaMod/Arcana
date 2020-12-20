package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.world.World;

public class TaintedGhastEntity extends GhastEntity {
	public TaintedGhastEntity(EntityType<? extends Entity> type, World worldIn) {
		super((EntityType<? extends GhastEntity>) type, worldIn);
	}
}
