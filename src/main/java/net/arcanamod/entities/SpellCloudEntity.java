package net.arcanamod.entities;

import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.Spells;
import net.arcanamod.systems.spell.casts.Cast;
import net.minecraft.block.material.PushReaction;
import net.minecraft.command.arguments.ParticleArgument;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
	private ISpell spell;
	private final Map<net.minecraft.entity.Entity, Integer> reapplicationDelayMap;
	private int duration;
	private int waitTime;
	private int reapplicationDelay;
	private boolean colorSet;
	private int durationOnUse;
	private float radiusOnUse;
	private float radiusPerTick;
	private LivingEntity owner;
	private UUID ownerUniqueId;

	public SpellCloudEntity(EntityType<? extends SpellCloudEntity> p_i50389_1_, World p_i50389_2_) {
		super(p_i50389_1_, p_i50389_2_);
		this.spell = Spells.EMPTY_SPELL;
		this.reapplicationDelayMap = Maps.newHashMap();
		this.duration = 600;
		this.waitTime = 20;
		this.reapplicationDelay = 20;
		this.noClip = true;
		this.setRadius(3.0F);
	}

	public SpellCloudEntity(World p_i46810_1_, double p_i46810_2_, double p_i46810_4_, double p_i46810_6_) {
		this(ArcanaEntities.SPELL_CLOUD.get(), p_i46810_1_);
		this.setPosition(p_i46810_2_, p_i46810_4_, p_i46810_6_);
	}

	public SpellCloudEntity(World p_i46810_1_, Vec3d vec) {
		this(ArcanaEntities.SPELL_CLOUD.get(), p_i46810_1_);
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
		double lvt_1_1_ = this.getPosX();
		double lvt_3_1_ = this.getPosY();
		double lvt_5_1_ = this.getPosZ();
		super.recalculateSize();
		this.setPosition(lvt_1_1_, lvt_3_1_, lvt_5_1_);
	}

	public float getRadius() {
		return (Float) this.getDataManager().get(RADIUS);
	}

	public void setSpell(ISpell spell) {
		this.spell = spell;
		if (!this.colorSet) {
			this.updateFixedColor();
		}

	}

	private void updateFixedColor() {
		if (this.spell == Spells.EMPTY_SPELL) {
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
		boolean lvt_1_1_ = this.shouldIgnoreRadius();
		float lvt_2_1_ = this.getRadius();
		if (this.world.isRemote) {
			IParticleData lvt_3_1_ = this.getParticleData();
			float lvt_6_1_;
			float lvt_7_1_;
			float lvt_8_1_;
			int lvt_10_1_;
			int lvt_11_1_;
			int lvt_12_1_;
			if (lvt_1_1_) {
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
				float lvt_4_2_ = 3.1415927F * lvt_2_1_ * lvt_2_1_;

				for (int lvt_5_2_ = 0; (float) lvt_5_2_ < lvt_4_2_; ++lvt_5_2_) {
					lvt_6_1_ = this.rand.nextFloat() * 6.2831855F;
					lvt_7_1_ = MathHelper.sqrt(this.rand.nextFloat()) * lvt_2_1_;
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
			if (lvt_1_1_ != lvt_3_2_) {
				this.setIgnoreRadius(lvt_3_2_);
			}

			if (lvt_3_2_) {
				return;
			}

			if (this.radiusPerTick != 0.0F) {
				lvt_2_1_ += this.radiusPerTick;
				if (lvt_2_1_ < 0.5F) {
					this.remove();
					return;
				}

				this.setRadius(lvt_2_1_);
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
						} while (lvt_12_3_ > (double) (lvt_2_1_ * lvt_2_1_));

						this.reapplicationDelayMap.put(lvt_7_3_, this.ticksExisted + this.reapplicationDelay);

						((Cast) spell).useOnEntity(null, lvt_7_3_);

						if (this.radiusOnUse != 0.0F) {
							lvt_2_1_ += this.radiusOnUse;
							if (lvt_2_1_ < 0.5F) {
								this.remove();
								return;
							}

							this.setRadius(lvt_2_1_);
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

	public void setRadiusOnUse(float p_184495_1_) {
		this.radiusOnUse = p_184495_1_;
	}

	public void setRadiusPerTick(float p_184487_1_) {
		this.radiusPerTick = p_184487_1_;
	}

	public void setWaitTime(int p_184485_1_) {
		this.waitTime = p_184485_1_;
	}

	public void setOwner(@Nullable LivingEntity p_184481_1_) {
		this.owner = p_184481_1_;
		this.ownerUniqueId = p_184481_1_ == null ? null : p_184481_1_.getUniqueID();
	}

	@Nullable
	public LivingEntity getOwner() {
		if (this.owner == null && this.ownerUniqueId != null && this.world instanceof ServerWorld) {
			net.minecraft.entity.Entity lvt_1_1_ = ((ServerWorld) this.world).getEntityByUuid(this.ownerUniqueId);
			if (lvt_1_1_ instanceof LivingEntity) {
				this.owner = (LivingEntity) lvt_1_1_;
			}
		}

		return this.owner;
	}

	protected void readAdditional(CompoundNBT p_70037_1_) {
		this.ticksExisted = p_70037_1_.getInt("Age");
		this.duration = p_70037_1_.getInt("Duration");
		this.waitTime = p_70037_1_.getInt("WaitTime");
		this.reapplicationDelay = p_70037_1_.getInt("ReapplicationDelay");
		this.durationOnUse = p_70037_1_.getInt("DurationOnUse");
		this.radiusOnUse = p_70037_1_.getFloat("RadiusOnUse");
		this.radiusPerTick = p_70037_1_.getFloat("RadiusPerTick");
		this.setRadius(p_70037_1_.getFloat("Radius"));
		this.ownerUniqueId = p_70037_1_.getUniqueId("OwnerUUID");
		if (p_70037_1_.contains("Particle", 8)) {
			try {
				this.setParticleData(ParticleArgument.parseParticle(new StringReader(p_70037_1_.getString("Particle"))));
			} catch (CommandSyntaxException var5) {
				PRIVATE_LOGGER.warn("Couldn't load custom particle {}", p_70037_1_.getString("Particle"), var5);
			}
		}

		if (p_70037_1_.contains("Color", 99)) {
			this.setColor(p_70037_1_.getInt("Color"));
		}

		if (p_70037_1_.contains("Spell", 8)) {
			this.setSpell(Spells.spellMap.get(new ResourceLocation(p_70037_1_.getString("Spell"))));
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

		if (this.spell != Spells.EMPTY_SPELL && this.spell != null) {
			compoundNBT.putString("Spell", ((Cast) spell).getId().toString()); // TODO: REPLACE (SPELL) wit (ISPELL)
		}
	}

	public void notifyDataManagerChange(DataParameter<?> p_184206_1_) {
		if (RADIUS.equals(p_184206_1_)) {
			this.recalculateSize();
		}

		super.notifyDataManagerChange(p_184206_1_);
	}

	public PushReaction getPushReaction() {
		return PushReaction.IGNORE;
	}

	public IPacket<?> createSpawnPacket() {
		return new SSpawnObjectPacket(this);
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