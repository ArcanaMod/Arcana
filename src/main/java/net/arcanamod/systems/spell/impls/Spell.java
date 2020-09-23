package net.arcanamod.systems.spell.impls;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.*;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.arcanamod.aspects.Aspects.*;

/**
 * ISpell class but it self registers.
 */
public abstract class Spell implements ISpell {
	public Spell(){
		SpellRegistry.addSpell(getId(),this);
	}

	protected static void useCasts(Spell spell, PlayerEntity player, List<CastAspect> aspects) {
		int distance = 10;
		int max_distance = 16;

		for (CastAspect aspect : aspects){
			if (aspect.primaryAspect == AIR) {
				spell.onAirCast(player, player.world, RayTraceUtils.getTargetBlockPos(player, player.world, distance), 4, 6);
			} else if (aspect.primaryAspect == WATER){
				BlockPos pos = RayTraceUtils.getTargetBlockPos(player, player.world, distance);
				List<Entity> target = player.world.getEntitiesWithinAABB(Entity.class,new AxisAlignedBB(pos).expand(4,4,4));
				if (target.size()>=1){
					spell.onWaterCast(player, target);
				}
			} else if (aspect.primaryAspect == FIRE){
				BlockPos pos = RayTraceUtils.getTargetBlockPos(player, player.world, distance);
				List<Entity> target = player.world.getEntitiesWithinAABB(Entity.class,new AxisAlignedBB(pos).expand(0,1,0));
				if (target.size()>=1){
					spell.onFireCast(player, target.get(0), pos);
				} else spell.onFireCast(player, null, pos);
			} else if (aspect.primaryAspect == EARTH){
				spell.onEarthCast(player, RayTraceUtils.getTargetBlockPos(player, player.world, max_distance));
			} else if (aspect.primaryAspect == ORDER){
				spell.onOrderCast(player);
			} else if (aspect.primaryAspect == CHAOS){
				BlockPos pos = RayTraceUtils.getTargetBlockPos(player, player.world, distance);
				LightningBoltEntity lbe = new LightningBoltEntity(player.world,pos.getX(),pos.getY(),pos.getZ(),true);
				player.world.addEntity(lbe);
				List<Entity> e = RayTraceUtils.rayTraceEntities(player.world,new Vec3d(pos.getX(),pos.getY(),pos.getZ()),player.getLookVec(),Optional.empty(), Entity.class);
				spell.onChaosCast(player, null, pos);
			}
		}
	}

	public static ISpell deserializeNBT(CompoundNBT compound){
		if (compound.contains("Spell")) {
			List<Aspect> modifiers = new ArrayList<>();
			for (INBT nbt : ((ListNBT) Objects.requireNonNull(compound.get("modifiers")))) {
				modifiers.add(Optional.ofNullable(AspectUtils.getAspectByResourceLocation(new ResourceLocation(((CompoundNBT)nbt).getString("Modifier")))).orElse(Aspects.EMPTY));
			}
			List<CastAspect> casts = new ArrayList<>();
			for (INBT nbt : ((ListNBT) Objects.requireNonNull(compound.get("casts")))) {
				casts.add(new CastAspect(
					Optional.ofNullable(AspectUtils.getAspectByResourceLocation(new ResourceLocation(((CompoundNBT)nbt).getString("Primary")))).orElse(Aspects.EMPTY),
					Optional.ofNullable(AspectUtils.getAspectByResourceLocation(new ResourceLocation(((CompoundNBT)nbt).getString("Combo")))).orElse(Aspects.EMPTY)
				));
			}
			return Spells.spellMap.get(new ResourceLocation(compound.getString("Spell"))).build(modifiers, casts, new CompoundNBT());
		} else return null;
	}

	public static CompoundNBT serializeNBT(ISpell spell){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("Spell",((Spell)spell).getId().toString()); // <-- Hardcoded here, fixes needed.
		ListNBT modifierList = new ListNBT();
		for (int i = 0; i < spell.getModAspects().size(); i++) {
			CompoundNBT c = new CompoundNBT();
			c.putString("Modifier", AspectUtils.getResourceLocationFromAspect(spell.getModAspects().get(i)).toString());
			modifierList.add(c);
		}

		ListNBT castList = new ListNBT();
		for (int i = 0; i < spell.getCastAspects().size(); i++) {
			CompoundNBT c = new CompoundNBT();
			c.putString("Primary",AspectUtils.getResourceLocationFromAspect(spell.getCastAspects().get(i).primaryAspect).toString());
			c.putString("Combo",AspectUtils.getResourceLocationFromAspect(spell.getCastAspects().get(i).comboAspect).toString());
			castList.add(c);
		}
		compound.put("modifiers",modifierList);
		compound.put("casts",castList);

		return compound;
	}

	public abstract ISpell build(List<Aspect> modAspects, List<CastAspect> castAspects, CompoundNBT compound);

	public abstract ResourceLocation getId();
	
	public Optional<ITextComponent> getName(CompoundNBT nbt){
		return Optional.of(new TranslationTextComponent("spell." + getId().getNamespace() + "." + getId().getPath()));
	}
	
	@Override
	public abstract void use(PlayerEntity player, Action action);

	public abstract void onAirCast(PlayerEntity caster, World world, BlockPos pos, int area, int duration);
	public abstract void onWaterCast(PlayerEntity caster, List<Entity> entityTargets);
	public abstract void onFireCast(PlayerEntity caster, @Nullable Entity entityTarget, BlockPos blockTarget);
	public abstract void onEarthCast(PlayerEntity caster, BlockPos blockTarget);
	public abstract void onOrderCast(PlayerEntity playerTarget);
	public abstract void onChaosCast(PlayerEntity caster, Entity entityTarget, BlockPos blockTarget);
}
