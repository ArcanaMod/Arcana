package net.arcanamod.entities.tainted;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class TaintedEntity extends MonsterEntity implements IMob {
	public EntityType parentEntity;

	public TaintedEntity(EntityType<? extends Entity> type, World worldIn, EntityType entity) {
		super((EntityType<? extends MonsterEntity>) type,worldIn);
		parentEntity = entity;
	}

	public EntityType getParentEntity() {
		return parentEntity;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.applyEntityAI();
	}

	protected void applyEntityAI() {
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	// TODO: Adjust values for every entity
	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.registerAttributes()
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D);
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.UNDEFINED;
	}

	@OnlyIn(Dist.CLIENT)
	public float getHeadRotationPointY(float p_70894_1_) {
		return 0.0F;
	}

	@OnlyIn(Dist.CLIENT)
	public float getHeadRotationAngleX(float p_70890_1_) {
		return this.rotationPitch * ((float) Math.PI / 180F);
	}
}
