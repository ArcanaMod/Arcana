package net.arcanamod.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public final class LocalAxis{
	
	private LocalAxis(){}
	
	// Adapted from LookingPosArgument::toAbsolutePos (Yarn names)
	public static Vec3d toAbsolutePos(Vec3d localPos, Vec2f rotation, Vec3d worldPos){
		float yCos = MathHelper.cos((rotation.y + 90) * .017453292F);
		float ySin = MathHelper.sin((rotation.y + 90) * .017453292F);
		float xCos = MathHelper.cos(-rotation.x * .017453292F);
		float xSin = MathHelper.sin(-rotation.x * .017453292F);
		float a = MathHelper.cos((-rotation.x + 90) * .017453292F);
		float b = MathHelper.sin((-rotation.x + 90) * .017453292F);
		Vec3d vec3d2 = new Vec3d(yCos * xCos, xSin, ySin * xCos);
		Vec3d vec3d3 = new Vec3d(yCos * a, b, ySin * a);
		Vec3d vec3d4 = vec3d2.crossProduct(vec3d3).scale(-1);
		double xOff = vec3d2.x * localPos.z + vec3d3.x * localPos.y + vec3d4.x * localPos.x;
		double yOff = vec3d2.y * localPos.z + vec3d3.y * localPos.y + vec3d4.y * localPos.x;
		double zOff = vec3d2.z * localPos.z + vec3d3.z * localPos.y + vec3d4.z * localPos.x;
		return new Vec3d(worldPos.x + xOff, worldPos.y + yOff, worldPos.z + zOff);
	}
}
