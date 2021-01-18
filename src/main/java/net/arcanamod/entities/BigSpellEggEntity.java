package net.arcanamod.entities;

import net.arcanamod.systems.spell.casts.Cast;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@SuppressWarnings("EntityConstructor")
public class BigSpellEggEntity extends SpellEggEntity {
	public BigSpellEggEntity(EntityType<? extends SpellEggEntity> type, World world) {
		super(type, world);
	}

	public BigSpellEggEntity(World world, PlayerEntity player, Cast cast) {
		super(world,player,cast);
	}
}
