package net.arcanamod.entities.tainted;

import net.arcanamod.effects.ArcanaEffects;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TaintedCaveSpiderEntity extends SpiderEntity {
	public TaintedCaveSpiderEntity(EntityType<? extends Entity> type, World worldIn) {
		super((EntityType<? extends SpiderEntity>) type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return SpiderEntity.func_234305_eI_().createMutableAttribute(Attributes.MAX_HEALTH, 18.0D);
	}

	public boolean attackEntityAsMob(Entity entityIn) {
		if (super.attackEntityAsMob(entityIn)) {
			if (entityIn instanceof LivingEntity) {
				int i = 0;
				if (this.world.getDifficulty() == Difficulty.NORMAL) {
					i = 7;
				} else if (this.world.getDifficulty() == Difficulty.HARD) {
					i = 15;
				}

				if (i > 0) {
					((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.POISON, i * 4, 0));
					((LivingEntity)entityIn).addPotionEffect(new EffectInstance(ArcanaEffects.TAINTED.get(), i * 20, 1));
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Nullable
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		return spawnDataIn;
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 0.45F;
	}
}
