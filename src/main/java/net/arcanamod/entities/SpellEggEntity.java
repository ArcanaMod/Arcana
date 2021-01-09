package net.arcanamod.entities;

import com.google.common.collect.Lists;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.systems.spell.casts.Cast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;

public class SpellEggEntity extends ProjectileItemEntity {
	private Cast cast;
	private PlayerEntity caster;

	private List<Class<?>> homeTargets = new ArrayList<>();

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
				((EntityRayTraceResult) result).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.5F);
				if (!world.isRemote)
					cast.useOnEntity(caster, ((EntityRayTraceResult) result).getEntity());
			}
			if (result.getType() == RayTraceResult.Type.BLOCK) {
				if (!world.isRemote)
					cast.useOnBlock(caster, world, ((BlockRayTraceResult) result).getPos());
			}
		}
	}

	@Override
	public void tick() {
		int s = 5; // size of box
		if (homeTargets.size() > 0){
			AxisAlignedBB box = new AxisAlignedBB(getPosX()-s,getPosY()-s,getPosZ()-s,getPosX()+s,getPosY()+s,getPosZ()+s);
			for (Class<?> homeTarget : homeTargets){
				List<Entity> entitiesWithinBox = world.getEntitiesWithinAABB((Class<Entity>) homeTarget,box,Entity::isAlive);
				if (entitiesWithinBox.size() > 0){
					Entity firstEntity = entitiesWithinBox.get(0);
					setMotion(firstEntity.getPosX()-getPosX(),firstEntity.getPosY()-getPosY(),firstEntity.getPosZ()-getPosZ());
				}
			}
		}

		if (isInWater()){
			if (cast != null){
				if (!world.isRemote)
					cast.useOnBlock(caster, world, getPosition());
				remove();
			}
		}

		super.tick();
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
