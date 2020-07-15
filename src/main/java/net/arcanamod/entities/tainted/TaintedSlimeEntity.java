package net.arcanamod.entities.tainted;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

public class TaintedSlimeEntity extends SlimeEntity {
	public TaintedSlimeEntity(EntityType<? extends TaintedSlimeEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void setSlimeSize(int size, boolean resetHealth) {
		super.setSlimeSize(size,resetHealth);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(size * size)+4);
	}

	protected IParticleData getSquishParticle() {
		return ParticleTypes.MYCELIUM;
	}
}
