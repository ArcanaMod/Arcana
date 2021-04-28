package net.arcanamod.entities.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

public class TaintedSlimeEntity extends SlimeEntity {
	public TaintedSlimeEntity(EntityType<? extends Entity> type, World worldIn) {
		super((EntityType<? extends SlimeEntity>) type, worldIn);
	}

	@Override
	protected void setSlimeSize(int size, boolean resetHealth) {
		super.setSlimeSize(size,resetHealth);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)(size * size)+4);
	}

	protected IParticleData getSquishParticle() {
		return ParticleTypes.MYCELIUM;
	}
}
