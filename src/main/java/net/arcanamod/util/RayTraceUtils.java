package net.arcanamod.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class RayTraceUtils {

	public static BlockPos getTargetBlockPos(PlayerEntity player, World world, int maxDistance){
		BlockRayTraceResult rayTraceResult = getTargetBlockResult(player,world, maxDistance);
		return rayTraceResult.getPos();
	}
	
	public static BlockState getTargetBlock(PlayerEntity player, World world, int maxdistance){
		BlockPos blockpos = getTargetBlockPos(player, world, maxdistance);
		return world.getBlockState(blockpos);
	}

	public static BlockRayTraceResult getTargetBlockResult(PlayerEntity player,World world, int maxdistance){
		Vector3d vec = player.getPositionVec();
		Vector3d vec3 = new Vector3d(vec.x,vec.y+player.getEyeHeight(),vec.z);
		Vector3d vec3a = player.getLook(1.0F);
		Vector3d vec3b = vec3.add(vec3a.getX() * maxdistance, vec3a.getY()*  maxdistance, vec3a.getZ()*  maxdistance);

		BlockRayTraceResult rayTraceResult = world.rayTraceBlocks(new RayTraceContext(vec3, vec3b,RayTraceContext.BlockMode.OUTLINE,  RayTraceContext.FluidMode.ANY, player));


		if(rayTraceResult!=null)
		{
			double xm=rayTraceResult.getHitVec().getX();
			double ym=rayTraceResult.getHitVec().getY();
			double zm=rayTraceResult.getHitVec().getZ();


			//player.sendMessage(new StringTextComponent(rayTraceResult.getFace().toString()));
			if(rayTraceResult.getFace() == Direction.SOUTH) {
				zm--;
			}
			if(rayTraceResult.getFace() == Direction.EAST) {
				xm--;
			}
			if(rayTraceResult.getFace() == Direction.UP) {
				ym--;
			}

			return new BlockRayTraceResult(rayTraceResult.getHitVec(), rayTraceResult.getFace(), new BlockPos(xm,ym,zm), false);
		}
		return null;
	}

	public static <T extends Entity> List<T> rayTraceEntities(World w, Vector3d pos, Vector3d ray, Optional<Predicate<T>> entityFilter, Class<T> entityClazz)
	{
		Vector3d end = pos.add(new Vector3d(1, 1, 1));
		AxisAlignedBB aabb = new AxisAlignedBB(pos.x, pos.y, pos.z, end.x, end.y, end.z).expand(ray.x, ray.y, ray.z);
		Vector3d checkVec = pos.add(ray);
		List<T> ret = Lists.newArrayList();
		for (T t : w.getEntitiesWithinAABB(entityClazz, aabb, entityFilter.orElse(Predicates.alwaysTrue())))
		{
			AxisAlignedBB entityBB = t.getBoundingBox();
			if (entityBB == null)
			{
				continue;
			}

			if (entityBB.intersects(Math.min(pos.x, checkVec.x), Math.min(pos.y, checkVec.y), Math.min(pos.z, checkVec.z), Math.max(pos.x, checkVec.x), Math.max(pos.y, checkVec.y), Math.max(pos.z, checkVec.z)))
			{
				ret.add(t);
			}
		}

		return ret;
	}
}
