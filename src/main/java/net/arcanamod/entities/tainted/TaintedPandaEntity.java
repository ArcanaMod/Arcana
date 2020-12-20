package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.world.World;

public class TaintedPandaEntity extends PandaEntity {
	public TaintedPandaEntity(EntityType<? extends Entity> type, World world) {
		super((EntityType<? extends PandaEntity>) type, world);
	}
}
