package net.arcanamod.entities;

import net.arcanamod.systems.spell.casts.Cast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlastEmitterEntity extends Entity {
	private static final DataParameter<Float> RADIUS;
	private static final DataParameter<Float> CURRENT_RADIUS;
	private static final DataParameter<Integer> COOLDOWN;
	
	private List<LivingEntity> wasDamaged = new ArrayList<>();
	private int cooldown = 0;
	
	public BlastEmitterEntity(World worldIn, float radius) {
		super(ArcanaEntities.BLAST_EMITTER.get(), worldIn);
		this.setRadius(radius);
	}
	
	public BlastEmitterEntity(EntityType<BlastEmitterEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	@Override
	protected void registerData() {
		this.getDataManager().register(RADIUS, 0.8F);
		this.getDataManager().register(CURRENT_RADIUS, 0.0F);
		this.getDataManager().register(COOLDOWN, 0);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (cooldown >= getCooldown()) {
			
			setCurrentRadius(getCurrentRadius() + 0.4f);
			
			Random rand = new Random();
			IParticleData particle = ParticleTypes.BUBBLE;
			float currRadius = getCurrentRadius();
			float surface = (3.1415927F * getRadius() * getRadius()) / ((currRadius + 0.1f) / 2);
			float randomizedPi;
			float spread;
			float offsetX;
			int color;
			int r;
			int g;
			
			if (world.isRemote) {
				for (int i = 0; (float) i < surface; ++i) {
					randomizedPi = rand.nextFloat() * 6.2831855F;
					spread = 0.8f * currRadius;
					offsetX = MathHelper.cos(randomizedPi) * spread;
					float offsetZ = MathHelper.sin(randomizedPi) * spread;
					if (particle.getType() == ParticleTypes.ENTITY_EFFECT) {
						color = Color.CYAN.getRGB();
						r = color >> 16 & 255;
						g = color >> 8 & 255;
						int b = color & 255;
						world.addOptionalParticle(particle, getPosX() + (double) offsetX, getPosY(), getPosZ() + (double) offsetZ, (double) ((float) r / 255.0F), (double) ((float) g / 255.0F), (double) ((float) b / 255.0F));
					} else {
						world.addOptionalParticle(particle, getPosX() + (double) offsetX, getPosY(), getPosZ() + (double) offsetZ, (0.5D - rand.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - rand.nextDouble()) * 0.15D);
					}
				}
			} else {
				if (currRadius < getRadius()) {
					List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(getPosX() - currRadius, getPosY() - currRadius, getPosZ() - currRadius, getPosX() + currRadius, getPosY() + currRadius, getPosZ() + currRadius), LivingEntity::isAlive);
					for (LivingEntity leInBox : entities) {
						if (!wasDamaged.contains(leInBox)) {
							leInBox.attackEntityFrom(DamageSource.MAGIC, 0.6f);
							wasDamaged.add(leInBox);
						}
					}
				}
				if (currRadius > (getRadius() * 2))
					this.remove();
			}
		}
		cooldown++;
		if (cooldown >= Short.MAX_VALUE)
			this.remove();
	}
	
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 *
	 * @param compound
	 */
	@Override
	protected void readAdditional(CompoundNBT compound) {
	
	}
	
	@Override
	protected void writeAdditional(CompoundNBT compound) {
	
	}
	
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	public float getRadius() {
		try{
			return (Float) this.getDataManager().get(RADIUS);
		} catch (NullPointerException exception){
			exception.printStackTrace();
			return 0;
		}
	}
	public void setRadius(float radius) {
		if (!this.world.isRemote) {
			this.getDataManager().set(RADIUS, radius);
		}
	}
	
	public float getCooldown() {
		try{
			return (Integer) this.getDataManager().get(COOLDOWN);
		} catch (NullPointerException exception){
			exception.printStackTrace();
			return 0;
		}
	}
	public void setCooldown(int ticks) {
		if (!this.world.isRemote) {
			this.getDataManager().set(COOLDOWN, ticks);
		}
	}
	
	public float getCurrentRadius() {
		try{
			return (Float) this.getDataManager().get(CURRENT_RADIUS);
		} catch (NullPointerException exception){
			exception.printStackTrace();
			return 0;
		}
	}
	public void setCurrentRadius(float radius) {
		if (!this.world.isRemote) {
			this.getDataManager().set(CURRENT_RADIUS, radius);
		}
	}
	
	static{
		RADIUS = EntityDataManager.createKey(BlastEmitterEntity.class, DataSerializers.FLOAT);
		CURRENT_RADIUS = EntityDataManager.createKey(BlastEmitterEntity.class, DataSerializers.FLOAT);
		COOLDOWN = EntityDataManager.createKey(BlastEmitterEntity.class, DataSerializers.VARINT);
	}
}
