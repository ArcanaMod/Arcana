package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.world.World;

public class TaintedHorseEntity extends HorseEntity {
	public TaintedHorseEntity(EntityType<? extends Entity> type, World worldIn) {
		super((EntityType<? extends HorseEntity>) type, worldIn);
	}
}
