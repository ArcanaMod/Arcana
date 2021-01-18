package net.arcanamod.entities;

import net.arcanamod.items.ArcanaItems;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.systems.spell.casts.ICast;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpellEggEntity extends ProjectileItemEntity {
	private Cast cast;
	private PlayerEntity caster;

	public SpellEggEntity(EntityType<? extends SpellEggEntity> type, World world) {
		super(type, world);
	}

	public SpellEggEntity(World worldIn, LivingEntity throwerIn) {
		super(ArcanaEntities.SPELL_EGG.get(), throwerIn, worldIn);
	}

	public SpellEggEntity(World worldIn, double x, double y, double z) {
		super(ArcanaEntities.SPELL_EGG.get(), x, y, z, worldIn);
	}

	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		//if (id == 3) {
			double d0 = 0.08D;

			for(int i = 0; i < 8; ++i) {
				this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, this.getItem()), this.getPosX(), this.getPosY(), this.getPosZ(), ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D);
			}
		//}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.getType() == RayTraceResult.Type.ENTITY) {
			//this.cast.useOnEntity(caster,((EntityRayTraceResult)result).getEntity());
			((EntityRayTraceResult)result).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.1F);
		}

		if (!this.world.isRemote) {
			if (this.rand.nextInt(8) == 0) {
				int i = 1;
				if (this.rand.nextInt(32) == 0) {
					i = 4;
				}

				for(int j = 0; j < i; ++j) {
					ChickenEntity chickenentity = EntityType.CHICKEN.create(this.world);
					chickenentity.setGrowingAge(-24000);
					chickenentity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
					this.world.addEntity(chickenentity);
				}
			}

			this.world.setEntityState(this, (byte)3);
			this.remove();
		}
	}

	@Override
	protected Item getDefaultItem() {
		return ArcanaItems.AMBER.get();
	}

	public void setCast(PlayerEntity caster, Cast cast) {
		this.cast = cast;
		this.caster = caster;
	}
}
