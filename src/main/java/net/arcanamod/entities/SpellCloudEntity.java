package net.arcanamod.entities;

import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.systems.spell.casts.Casts;
import net.arcanamod.systems.spell.casts.ICast;
import net.minecraft.block.material.PushReaction;
import net.minecraft.command.arguments.ParticleArgument;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpellCloudEntity extends Entity {
	private static final Logger PRIVATE_LOGGER = LogManager.getLogger();
	private static final DataParameter<Float> RADIUS;
	private static final DataParameter<Integer> COLOR;
	private static final DataParameter<Boolean> IGNORE_RADIUS;
	private static final DataParameter<IParticleData> PARTICLE;
	private ICast spell;
	private final Map<net.minecraft.entity.Entity, Integer> reapplicationDelayMap = Maps.newHashMap();
	private int duration;
	private int waitTime;
	private int reapplicationDelay;
	private boolean colorSet;
	private int durationOnUse;
	private float radiusOnUse;
	private float radiusPerTick;
	private LivingEntity owner;
	private UUID ownerUniqueId;

	public static class CloudVariableGrid{
		public PlayerEntity player;
		World world;
		Vector3d area;
		int rMultP;

		public CloudVariableGrid(PlayerEntity player, World world, Vector3d positionVec, int i) {
		}
	}

	public SpellCloudEntity(EntityType<? extends SpellCloudEntity> entityType, World world) {
		super(entityType, world);
		this.noClip = true;
		this.setRadius(3.0F);
	}

	public SpellCloudEntity(World world, double x, double y, double z) {
		this(ArcanaEntities.SPELL_CLOUD.get(), world);
		this.setPosition(x, y, z);
	}

	public SpellCloudEntity(World world, Vector3d vec) {
		this(ArcanaEntities.SPELL_CLOUD.get(), world);
		this.setPosition(vec.x,vec.y,vec.z);
	}

	protected void registerData() {
		this.getDataManager().register(COLOR, 0);
		this.getDataManager().register(RADIUS, 0.5F);
		this.getDataManager().register(IGNORE_RADIUS, false);
		this.getDataManager().register(PARTICLE, ParticleTypes.ENTITY_EFFECT);
	}

	public void setRadius(float p_184483_1_) {
		if (!this.world.isRemote) {
			this.getDataManager().set(RADIUS, p_184483_1_);
		}

	}

	public void recalculateSize() {
		double x = this.getPosX();
		double y = this.getPosY();
		double z = this.getPosZ();
		super.recalculateSize();
		this.setPosition(x, y, z);
	}

	public float getRadius() {
		return (Float) this.getDataManager().get(RADIUS);
	}

	public void setSpell(ICast spell) {
		this.spell = spell;
		if (!this.colorSet) {
			this.updateFixedColor();
		}

	}

	private void updateFixedColor() {
		if (this.spell == null) {
			this.getDataManager().set(COLOR, 0);
		} else {
			this.getDataManager().set(COLOR, this.spell.getSpellAspect().getColorRange().get(3));
		}

	}

	public int getColor() {
		return (Integer) this.getDataManager().get(COLOR);
	}

	public void setColor(int color) {
		this.colorSet = true;
		this.getDataManager().set(COLOR, color);
	}

	public IParticleData getParticleData() {
		return (IParticleData) this.getDataManager().get(PARTICLE);
	}

	public void setParticleData(IParticleData p_195059_1_) {
		this.getDataManager().set(PARTICLE, p_195059_1_);
	}

	protected void setIgnoreRadius(boolean p_184488_1_) {
		this.getDataManager().set(IGNORE_RADIUS, p_184488_1_);
	}

	public boolean shouldIgnoreRadius() {
		return (Boolean) this.getDataManager().get(IGNORE_RADIUS);
	}

	public int getDuration() {
		return this.duration;
	}

	public void setDuration(int p_184486_1_) {
		this.duration = p_184486_1_;
	}

	public void tick() {
		super.tick();

		boolean ignores = this.shouldIgnoreRadius();
		float radius = this.getRadius();
		if (this.world.isRemote) {
			IParticleData lvt_3_1_ = this.getParticleData();
			float lvt_6_1_;
			float lvt_7_1_;
			float lvt_8_1_;
			int lvt_10_1_;
			int lvt_11_1_;
			int lvt_12_1_;
			if (ignores) {
				if (this.rand.nextBoolean()) {
					for (int lvt_4_1_ = 0; lvt_4_1_ < 2; ++lvt_4_1_) {
						float lvt_5_1_ = this.rand.nextFloat() * 6.2831855F;
						lvt_6_1_ = MathHelper.sqrt(this.rand.nextFloat()) * 0.2F;
						lvt_7_1_ = MathHelper.cos(lvt_5_1_) * lvt_6_1_;
						lvt_8_1_ = MathHelper.sin(lvt_5_1_) * lvt_6_1_;
						if (lvt_3_1_.getType() == ParticleTypes.ENTITY_EFFECT) {
							int lvt_9_1_ = this.rand.nextBoolean() ? 16777215 : this.getColor();
							lvt_10_1_ = lvt_9_1_ >> 16 & 255;
							lvt_11_1_ = lvt_9_1_ >> 8 & 255;
							lvt_12_1_ = lvt_9_1_ & 255;
							this.world.addOptionalParticle(lvt_3_1_, this.getPosX() + (double) lvt_7_1_, this.getPosY(), this.getPosZ() + (double) lvt_8_1_, (double) ((float) lvt_10_1_ / 255.0F), (double) ((float) lvt_11_1_ / 255.0F), (double) ((float) lvt_12_1_ / 255.0F));
						} else {
							this.world.addOptionalParticle(lvt_3_1_, this.getPosX() + (double) lvt_7_1_, this.getPosY(), this.getPosZ() + (double) lvt_8_1_, 0.0D, 0.0D, 0.0D);
						}
					}
				}
			} else {
				float lvt_4_2_ = 3.1415927F * radius * radius;

				for (int lvt_5_2_ = 0; (float) lvt_5_2_ < lvt_4_2_; ++lvt_5_2_) {
					lvt_6_1_ = this.rand.nextFloat() * 6.2831855F;
					lvt_7_1_ = MathHelper.sqrt(this.rand.nextFloat()) * radius;
					lvt_8_1_ = MathHelper.cos(lvt_6_1_) * lvt_7_1_;
					float lvt_9_2_ = MathHelper.sin(lvt_6_1_) * lvt_7_1_;
					if (lvt_3_1_.getType() == ParticleTypes.ENTITY_EFFECT) {
						lvt_10_1_ = this.getColor();
						lvt_11_1_ = lvt_10_1_ >> 16 & 255;
						lvt_12_1_ = lvt_10_1_ >> 8 & 255;
						int lvt_13_1_ = lvt_10_1_ & 255;
						this.world.addOptionalParticle(lvt_3_1_, this.getPosX() + (double) lvt_8_1_, this.getPosY(), this.getPosZ() + (double) lvt_9_2_, (double) ((float) lvt_11_1_ / 255.0F), (double) ((float) lvt_12_1_ / 255.0F), (double) ((float) lvt_13_1_ / 255.0F));
					} else {
						this.world.addOptionalParticle(lvt_3_1_, this.getPosX() + (double) lvt_8_1_, this.getPosY(), this.getPosZ() + (double) lvt_9_2_, (0.5D - this.rand.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.rand.nextDouble()) * 0.15D);
					}
				}
			}
		} else {
			if (this.ticksExisted >= this.waitTime + this.duration) {
				this.remove();
				return;
			}

			boolean lvt_3_2_ = this.ticksExisted < this.waitTime;
			if (ignores != lvt_3_2_) {
				this.setIgnoreRadius(lvt_3_2_);
			}

			if (lvt_3_2_) {
				return;
			}

			if (this.radiusPerTick != 0.0F) {
				radius += this.radiusPerTick;
				if (radius < 0.5F) {
					this.remove();
					return;
				}

				this.setRadius(radius);
			}

			if (this.ticksExisted % 5 == 0) {
				Iterator lvt_4_3_ = this.reapplicationDelayMap.entrySet().iterator();

				while (lvt_4_3_.hasNext()) {
					Map.Entry<net.minecraft.entity.Entity, Integer> lvt_5_3_ = (Map.Entry) lvt_4_3_.next();
					if (this.ticksExisted >= (Integer) lvt_5_3_.getValue()) {
						lvt_4_3_.remove();
					}
				}

				List<LivingEntity> lvt_5_4_ = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox());
				if (!lvt_5_4_.isEmpty()) {
					Iterator var25 = lvt_5_4_.iterator();

					while (true) {
						LivingEntity lvt_7_3_;
						double lvt_12_3_;
						do {
							do {
								do {
									if (!var25.hasNext()) {
										return;
									}

									lvt_7_3_ = (LivingEntity) var25.next();
								} while (this.reapplicationDelayMap.containsKey(lvt_7_3_));
							} while (!lvt_7_3_.canBeHitWithPotion());

							double lvt_8_3_ = lvt_7_3_.getPosX() - this.getPosX();
							double lvt_10_3_ = lvt_7_3_.getPosZ() - this.getPosZ();
							lvt_12_3_ = lvt_8_3_ * lvt_8_3_ + lvt_10_3_ * lvt_10_3_;
						} while (lvt_12_3_ > (double) (radius * radius));

						this.reapplicationDelayMap.put(lvt_7_3_, this.ticksExisted + this.reapplicationDelay);

						if (spell != null)
							((Cast) spell).useOnEntity((PlayerEntity) owner, lvt_7_3_);

						if (this.radiusOnUse != 0.0F) {
							radius += this.radiusOnUse;
							if (radius < 0.5F) {
								this.remove();
								return;
							}

							this.setRadius(radius);
						}

						if (this.durationOnUse != 0) {
							this.duration += this.durationOnUse;
							if (this.duration <= 0) {
								this.remove();
								return;
							}
						}
					}
				}
			}
		}

	}

	public void setRadiusOnUse(float radius) {
		this.radiusOnUse = radius;
	}

	public void setRadiusPerTick(float radius) {
		this.radiusPerTick = radius;
	}

	public void setWaitTime(int time) {
		this.waitTime = time;
	}

	public void setOwner(@Nullable LivingEntity owner) {
		this.owner = owner;
		this.ownerUniqueId = owner == null ? null : owner.getUniqueID();
	}

	@Nullable
	public LivingEntity getOwner() {
		if (this.owner == null && this.ownerUniqueId != null && this.world instanceof ServerWorld) {
			net.minecraft.entity.Entity entity = ((ServerWorld) this.world).getEntityByUuid(this.ownerUniqueId);
			if (entity instanceof LivingEntity) {
				this.owner = (LivingEntity) entity;
			}
		}

		return this.owner;
	}

	protected void readAdditional(CompoundNBT compoundNBT) {
		this.ticksExisted = compoundNBT.getInt("Age");
		this.duration = compoundNBT.getInt("Duration");
		this.waitTime = compoundNBT.getInt("WaitTime");
		this.reapplicationDelay = compoundNBT.getInt("ReapplicationDelay");
		this.durationOnUse = compoundNBT.getInt("DurationOnUse");
		this.radiusOnUse = compoundNBT.getFloat("RadiusOnUse");
		this.radiusPerTick = compoundNBT.getFloat("RadiusPerTick");
		this.setRadius(compoundNBT.getFloat("Radius"));
		this.ownerUniqueId = compoundNBT.getUniqueId("OwnerUUID");
		if (compoundNBT.contains("Particle", 8)) {
			try {
				this.setParticleData(ParticleArgument.parseParticle(new StringReader(compoundNBT.getString("Particle"))));
			} catch (CommandSyntaxException var5) {
				PRIVATE_LOGGER.warn("Couldn't load custom particle {}", compoundNBT.getString("Particle"), var5);
			}
		}

		if (compoundNBT.contains("Color", 99)) {
			this.setColor(compoundNBT.getInt("Color"));
		}

		if (compoundNBT.contains("Spell", 8)) {
			this.setSpell(Casts.castMap.get(new ResourceLocation(compoundNBT.getString("Spell"))));
		}

	}

	protected void writeAdditional(CompoundNBT compoundNBT) {
		compoundNBT.putInt("Age", this.ticksExisted);
		compoundNBT.putInt("Duration", this.duration);
		compoundNBT.putInt("WaitTime", this.waitTime);
		compoundNBT.putInt("ReapplicationDelay", this.reapplicationDelay);
		compoundNBT.putInt("DurationOnUse", this.durationOnUse);
		compoundNBT.putFloat("RadiusOnUse", this.radiusOnUse);
		compoundNBT.putFloat("RadiusPerTick", this.radiusPerTick);
		compoundNBT.putFloat("Radius", this.getRadius());
		compoundNBT.putString("Particle", this.getParticleData().getParameters());
		if (this.ownerUniqueId != null) {
			compoundNBT.putUniqueId("OwnerUUID", this.ownerUniqueId);
		}

		if (this.colorSet) {
			compoundNBT.putInt("Color", this.getColor());
		}

		if (this.spell != null) {
			compoundNBT.putString("spell", ((Cast) spell).getId().toString()); // TODO: REPLACE (SPELL) wit (ISPELL)
		}
	}

	public void notifyDataManagerChange(DataParameter<?> dataParameter) {
		if (RADIUS.equals(dataParameter)) {
			this.recalculateSize();
		}

		super.notifyDataManagerChange(dataParameter);
	}

	public PushReaction getPushReaction() {
		return PushReaction.IGNORE;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public EntitySize getSize(Pose p_213305_1_) {
		return EntitySize.flexible(this.getRadius() * 2.0F, 0.5F);
	}

	static {
		RADIUS = EntityDataManager.createKey(AreaEffectCloudEntity.class, DataSerializers.FLOAT);
		COLOR = EntityDataManager.createKey(AreaEffectCloudEntity.class, DataSerializers.VARINT);
		IGNORE_RADIUS = EntityDataManager.createKey(AreaEffectCloudEntity.class, DataSerializers.BOOLEAN);
		PARTICLE = EntityDataManager.createKey(AreaEffectCloudEntity.class, DataSerializers.PARTICLE_DATA);
	}
}