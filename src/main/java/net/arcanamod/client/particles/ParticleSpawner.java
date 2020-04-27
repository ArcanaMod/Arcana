package net.arcanamod.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;

public class ParticleSpawner{
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static Particle spawnParticle(EnumArcanaParticles type, double x, double y, double z){
		if(mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null){
			
			Particle var21 = null;
			
			if(type == EnumArcanaParticles.NORMAL_NODE){
				var21 = new ParticleNormalNode(mc.world, x, y, z);
			}
			
			mc.effectRenderer.addEffect(var21);
			return var21;
			
		}
		return null;
	}
}
