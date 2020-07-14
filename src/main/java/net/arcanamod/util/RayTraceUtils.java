package net.arcanamod.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class RayTraceUtils {

	public static BlockPos getTargetBlockPos(PlayerEntity player, World world, int maxdistance){
		BlockRayTraceResult rayTraceResult = getTargetBlockResult(player,world, maxdistance);
		return new BlockPos(rayTraceResult.getHitVec().getX(),rayTraceResult.getHitVec().getY(),rayTraceResult.getHitVec().getZ());
	}

	public static BlockRayTraceResult getTargetBlockResult(PlayerEntity player,World world, int maxdistance){
		Vec3d vec = player.getPositionVector();
		Vec3d vec3 = new Vec3d(vec.x,vec.y+player.getEyeHeight(),vec.z);
		Vec3d vec3a = player.getLook(1.0F);
		Vec3d vec3b = vec3.add(vec3a.getX() * maxdistance, vec3a.getY()*  maxdistance, vec3a.getZ()*  maxdistance);

		BlockRayTraceResult rayTraceResult = world.rayTraceBlocks(new RayTraceContext(vec3, vec3b,RayTraceContext.BlockMode.OUTLINE,  RayTraceContext.FluidMode.ANY, player));
		
		double xm = rayTraceResult.getHitVec().getX();
		double ym = rayTraceResult.getHitVec().getY();
		double zm = rayTraceResult.getHitVec().getZ();
		
		if(rayTraceResult.getFace() == Direction.SOUTH)
			zm--;
		if(rayTraceResult.getFace() == Direction.EAST)
			xm--;
		if(rayTraceResult.getFace() == Direction.UP)
			ym--;
		
		return new BlockRayTraceResult(rayTraceResult.getHitVec(), rayTraceResult.getFace(), new BlockPos(xm,ym,zm), false);
	}
}
