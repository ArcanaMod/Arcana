package net.arcanamod.entities.tainted;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.world.World;

public class TaintedGhastEntity extends GhastEntity {
	public TaintedGhastEntity(EntityType<? extends TaintedGhastEntity> type, World worldIn) {
		super(type, worldIn);
	}
}
