package net.arcanamod.systems.spell.impls;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.systems.spell.CastAspect;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.SpellExtraData;
import net.arcanamod.systems.spell.SpellRegistry;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.arcanamod.aspects.Aspects.*;

/**
 * ISpell class but it self registers.
 */
public abstract class DefaultSpell implements ISpell {
	public DefaultSpell(){
		SpellRegistry.addSpell(getId(),this);
	}

	protected static void useCasts(DefaultSpell spell, PlayerEntity player, CastAspect[] aspects) {
		int distance = 10;
		int max_distance = 16;

		for (CastAspect aspect : aspects){
			if (aspect.primaryAspect == AIR) {
				spell.onAirCast(player.world,RayTraceUtils.getTargetBlockPos(player, player.world, distance),4,6);
			} else if (aspect.primaryAspect == WATER){
				//BlockPos pos = RayTraceUtils.getTargetBlockPos(player, player.world, distance);
				//Entity[] targets;
				//spell.onWaterCast(targets);
			} else if (aspect.primaryAspect == FIRE){
				//Entity target;
				//spell.onFireCast(target,RayTraceUtils.getTargetBlockPos(player, player.world, distance));
			} else if (aspect.primaryAspect == EARTH){
				spell.onEarthCast(RayTraceUtils.getTargetBlockPos(player, player.world, max_distance));
			} else if (aspect.primaryAspect == ORDER){
				spell.onOrderCast(player);
			} else if (aspect.primaryAspect == CHAOS){
				//Entity target;
				//spell.onChaosCast(target,RayTraceUtils.getTargetBlockPos(player, player.world, distance));
			}
		}
	}

	public abstract ISpell build(Aspect[] modAspects, CastAspect[] castAspects, SpellExtraData data);

	public abstract ResourceLocation getId();

	@Override
	public abstract void use(PlayerEntity player, Action action);

	public abstract void onAirCast(World world, BlockPos pos, int area, int duration);
	public abstract void onWaterCast(Entity[] entityTargets);
	public abstract void onFireCast(Entity entityTarget, BlockPos blockTarget);
	public abstract void onEarthCast(BlockPos blockTarget);
	public abstract void onOrderCast(PlayerEntity playerTarget);
	public abstract void onChaosCast(Entity entityTarget, BlockPos blockTarget);
}
