package net.arcanamod.entities;

import net.arcanamod.items.ArcanaItems;
import net.arcanamod.systems.spell.casts.Cast;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class SpellEggEntity extends ProjectileItemEntity {
	private Cast cast;
	private PlayerEntity caster;

	public SpellEggEntity(EntityType<? extends SpellEggEntity> type, World world) {
		super(type, world);
	}

	public SpellEggEntity(World worldIn, PlayerEntity throwerIn, Cast cast) {
		super(ArcanaEntities.SPELL_EGG.get(), throwerIn, worldIn);
		this.cast = cast;
		this.caster = throwerIn;
	}

	public SpellEggEntity(World worldIn, double x, double y, double z) {
		super(ArcanaEntities.SPELL_EGG.get(), x, y, z, worldIn);
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
		if (result.getType() == RayTraceResult.Type.ENTITY) {
			((EntityRayTraceResult)result).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.1F);
			if (!world.isRemote)
			cast.useOnEntity(caster, ((EntityRayTraceResult)result).getEntity());
		}
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
}
