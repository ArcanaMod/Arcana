package net.arcanamod.entities.tainted;

import net.arcanamod.entities.DairSpiritEntity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class TaintedEntity extends AnimalEntity {
	public EntityType parentEntity;

	public TaintedEntity(EntityType<? extends AnimalEntity> type, World worldIn, EntityType entity) {
		super(type,worldIn);
		parentEntity = entity;
	}

	public EntityType getParentEntity() {
		return parentEntity;
	}

	@Nullable
	@Override
	public AgeableEntity createChild(AgeableEntity ageable) {
		return this;
	}
}
