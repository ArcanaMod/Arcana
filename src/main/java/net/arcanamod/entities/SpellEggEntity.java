package net.arcanamod.entities;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.util.FluidRaytraceHelper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class SpellEggEntity extends ProjectileItemEntity {
	private Cast cast;
	private PlayerEntity caster;

	private List<Class<?>> homeTargets = new ArrayList<>();

	private int ignoreTime;
	private Entity ignoreEntity;

	public SpellEggEntity(EntityType<? extends SpellEggEntity> type, World world) {
		super(type, world);
	}

	public SpellEggEntity(World world, PlayerEntity thrower, Cast cast) {
		super(ArcanaEntities.SPELL_EGG.get(), thrower, world);
		this.cast = cast;
		this.caster = thrower;
	}

	public SpellEggEntity(World world, double x, double y, double z) {
		super(ArcanaEntities.SPELL_EGG.get(), x, y, z, world);
	}

	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			double d0 = 0.08D;

			for(int i = 0; i < 8; ++i) {
				this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(this.getDefaultItem())), this.getPosX(), this.getPosY(), this.getPosZ(), ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D);
			}
		}

	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (cast != null) {
			if (result.getType() == RayTraceResult.Type.ENTITY) {
				((EntityRayTraceResult) result).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), 0.5F);
				if (!world.isRemote)
					cast.useOnEntity(caster, ((EntityRayTraceResult) result).getEntity());
				remove();
			}
			if (result.getType() == RayTraceResult.Type.BLOCK) {
				if (!world.isRemote)
					cast.useOnBlock(caster, world, ((BlockRayTraceResult) result).getPos());
			}
		}
	}

	@Override
	public void tick() {
//		if (this.throwableShake > 0) {
//			--this.throwableShake;
//		}

		// TODO: Look into Fix
//		if (this.inGround) {
//			this.inGround = false;
//			this.setMotion(this.getMotion().mul((double)(this.rand.nextFloat() * 0.2F), (double)(this.rand.nextFloat() * 0.2F), (double)(this.rand.nextFloat() * 0.2F)));
//		}

		AxisAlignedBB axisalignedbb = this.getBoundingBox().expand(this.getMotion()).grow(1.0D);

		for(Entity entity : this.world.getEntitiesInAABBexcluding(this, axisalignedbb, (p_213881_0_) -> {
			return !p_213881_0_.isSpectator() && p_213881_0_.canBeCollidedWith();
		})) {
			if (entity == this.ignoreEntity) {
				++this.ignoreTime;
				break;
			}

			if (this.getShooter() != null && this.ticksExisted < 2 && this.ignoreEntity == null) {
				this.ignoreEntity = entity;
				this.ignoreTime = 3;
				break;
			}
		}

		RayTraceResult raytraceresult = FluidRaytraceHelper.rayTrace(this, axisalignedbb, (p_213880_1_) -> {
			return !p_213880_1_.isSpectator() && p_213880_1_.canBeCollidedWith() && p_213880_1_ != this.ignoreEntity;
		}, RayTraceContext.BlockMode.OUTLINE, true);
		if (this.ignoreEntity != null && this.ignoreTime-- <= 0) {
			this.ignoreEntity = null;
		}

		if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
			if (raytraceresult.getType() == RayTraceResult.Type.BLOCK && this.world.getBlockState(((BlockRayTraceResult)raytraceresult).getPos()).getBlock() == Blocks.NETHER_PORTAL) {
				this.setPortal(((BlockRayTraceResult)raytraceresult).getPos());
			} else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)){
				this.onImpact(raytraceresult);
			}
		}

		Vector3d vec3d = this.getMotion();
		double d0 = this.getPosX() + vec3d.x;
		double d1 = this.getPosY() + vec3d.y;
		double d2 = this.getPosZ() + vec3d.z;
		float f = MathHelper.sqrt(horizontalMag(vec3d));
		this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));

		for(this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
		this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
		float f1;
		if (this.isInWater()) {
			for(int i = 0; i < 4; ++i) {
				float f2 = 0.25F;
				this.world.addParticle(ParticleTypes.BUBBLE, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
			}

			f1 = 0.8F;
		} else {
			f1 = 0.99F;
		}

		this.setMotion(vec3d.scale((double)f1));
		if (!this.hasNoGravity()) {
			Vector3d vec3d1 = this.getMotion();
			this.setMotion(vec3d1.x, vec3d1.y - (double)this.getGravityVelocity(), vec3d1.z);
		}

		this.setPosition(d0, d1, d2);

		if (!this.world.isRemote) {
			this.setFlag(6, this.isGlowing());
		}

		this.baseTick();

		/*int s = 15; // size of box
		if (homeTargets.size() > 0){
			AxisAlignedBB box = new AxisAlignedBB(getPosX()-s,getPosY()-s,getPosZ()-s,getPosX()+s,getPosY()+s,getPosZ()+s);
			for (Class<?> homeTarget : homeTargets){
				List<Entity> entitiesWithinBox = world.getEntitiesWithinAABB((Class<Entity>) homeTarget,box,Entity::isAlive);
				if (entitiesWithinBox.size() > 0){
					Entity firstEntity = entitiesWithinBox.get(0);
					//setMotion();
				}
			}
		}

		super.tick();*/
	}

	public void setCast(PlayerEntity caster, Cast cast){
		this.cast = cast;
		this.caster = caster;
	}

	@Override
	protected Item getDefaultItem() {
		return ArcanaItems.AMBER.get();
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public final void enableHoming(Class<?>... targets) {
		homeTargets = Lists.newArrayList(targets);
	}
}
