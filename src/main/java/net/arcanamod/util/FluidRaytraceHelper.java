package net.arcanamod.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Predicate;

public class FluidRaytraceHelper {
	public static RayTraceResult rayTrace(Entity projectile, boolean checkEntityCollision, boolean includeShooter, @Nullable Entity shooter, RayTraceContext.BlockMode blockModeIn) {
		return rayTrace(projectile, checkEntityCollision, includeShooter, shooter, blockModeIn, true, (p_221270_2_) -> {
			return !p_221270_2_.isSpectator() && p_221270_2_.canBeCollidedWith() && (includeShooter || !p_221270_2_.isEntityEqual(shooter)) && !p_221270_2_.noClip;
		}, projectile.getBoundingBox().expand(projectile.getMotion()).grow(1.0D));
	}

	public static RayTraceResult rayTrace(Entity projectile, AxisAlignedBB boundingBox, Predicate<Entity> filter, RayTraceContext.BlockMode blockModeIn, boolean checkEntityCollision) {
		return rayTrace(projectile, checkEntityCollision, false, (Entity)null, blockModeIn, false, filter, boundingBox);
	}

	private static RayTraceResult rayTrace(Entity projectile, boolean checkEntityCollision, boolean includeShooter, @Nullable Entity shooter, RayTraceContext.BlockMode blockModeIn, boolean p_221268_5_, Predicate<Entity> filter, AxisAlignedBB boundingBox) {
		Vector3d vec3d = projectile.getMotion();
		World world = projectile.world;
		Vector3d vec3d1 = projectile.getPositionVec();
		if (p_221268_5_ && !world.hasNoCollisions(projectile, projectile.getBoundingBox(), ((Set<Entity>)(!includeShooter && shooter != null ? getEntityAndMount(shooter) : ImmutableSet.of()))::contains)) {
			return new BlockRayTraceResult(vec3d1, Direction.getFacingFromVector(vec3d.x, vec3d.y, vec3d.z), new BlockPos(projectile.getPosition()), false);
		} else {
			Vector3d vec3d2 = vec3d1.add(vec3d);
			RayTraceResult raytraceresult = world.rayTraceBlocks(new RayTraceContext(vec3d1, vec3d2, blockModeIn, RayTraceContext.FluidMode.SOURCE_ONLY, projectile));
			if (checkEntityCollision) {
				if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
					vec3d2 = raytraceresult.getHitVec();
				}

				RayTraceResult raytraceresult1 = ProjectileHelper.rayTraceEntities(world, projectile, vec3d1, vec3d2, boundingBox, filter);
				if (raytraceresult1 != null) {
					raytraceresult = raytraceresult1;
				}
			}

			return raytraceresult;
		}
	}

	private static Set<Entity> getEntityAndMount(Entity rider) {
		Entity entity = rider.getRidingEntity();
		return entity != null ? ImmutableSet.of(rider, entity) : ImmutableSet.of(rider);
	}
}
