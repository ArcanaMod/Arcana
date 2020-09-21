package net.arcanamod.systems.spell.impls;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.*;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static net.arcanamod.aspects.Aspects.*;

/**
 * ISpell class but it self registers.
 */
public abstract class Spell implements ISpell {
	public Spell(){
		SpellRegistry.addSpell(getId(),this);
	}

	protected static void useCasts(Spell spell, PlayerEntity player, CastAspect[] aspects) {
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
				List<Entity> target = player.world.getEntitiesWithinAABB(Entity.class,new AxisAlignedBB(pos).expand(0,1,0));
				if (target.size()>=1){
					spell.onChaosCast(player, target.get(0), pos);
				} else spell.onChaosCast(player, null, pos);
			}
		}
	}

	public static ISpell deserializeNBT(CompoundNBT compound){
		if (compound.contains("Spell")) {
			Aspect[] modifiers = new Aspect[compound.getInt("ModifierAspectsLength")];
			for (int i = 0; i < compound.getInt("ModifierAspectsLength"); i++) {
				modifiers[i] = Optional.ofNullable(AspectUtils.getAspectByResourceLocation(new ResourceLocation(compound.getString("Modifier" + i)))).orElse(Aspects.EMPTY);
			}
			CastAspect[] casts = new CastAspect[compound.getInt("CastAspectsLength")];
			for (int i = 0; i < compound.getInt("CastAspectsLength"); i++) {
				casts[i] = new CastAspect(
					Optional.ofNullable(AspectUtils.getAspectByResourceLocation(new ResourceLocation(compound.getString("Cast" + i + "Primary")))).orElse(Aspects.EMPTY),
					Optional.ofNullable(AspectUtils.getAspectByResourceLocation(new ResourceLocation(compound.getString("Cast" + i + "Combo")))).orElse(Aspects.EMPTY)
				);
			}
			return Spells.spellMap.get(new ResourceLocation(compound.getString("Spell"))).build(modifiers, casts, new SpellExtraData());
		} else return null;
	}

	public static CompoundNBT serializeNBT(ISpell spell){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("Spell",((Spell)spell).getId().toString()); // <-- Hardcoded here, fixes needed.
		compound.putInt("ModifierAspectsLength",spell.getModAspects().length);
		for (int i = 0; i < spell.getModAspects().length; i++) {
			compound.putString("Modifier"+i, AspectUtils.getResourceLocationFromAspect(spell.getModAspects()[i]).toString());
		}
		compound.putInt("CastAspectsLength",spell.getCastAspects().length);
		for (int i = 0; i < spell.getCastAspects().length; i++) {
			compound.putString("Cast"+i+"Primary", AspectUtils.getResourceLocationFromAspect(spell.getCastAspects()[i].primaryAspect).toString());
			compound.putString("Cast"+i+"Combo", AspectUtils.getResourceLocationFromAspect(spell.getCastAspects()[i].comboAspect).toString());
		}
		return compound;
	}

	public abstract ISpell build(Aspect[] modAspects, CastAspect[] castAspects, SpellExtraData data);

	public abstract ResourceLocation getId();

	@Override
	public abstract void use(PlayerEntity player, Action action);

	public abstract void onAirCast(PlayerEntity caster, World world, BlockPos pos, int area, int duration);
	public abstract void onWaterCast(PlayerEntity caster, List<Entity> entityTargets);
	public abstract void onFireCast(PlayerEntity caster, @Nullable Entity entityTarget, BlockPos blockTarget);
	public abstract void onEarthCast(PlayerEntity caster, BlockPos blockTarget);
	public abstract void onOrderCast(PlayerEntity playerTarget);
	public abstract void onChaosCast(PlayerEntity caster, Entity entityTarget, BlockPos blockTarget);
}
