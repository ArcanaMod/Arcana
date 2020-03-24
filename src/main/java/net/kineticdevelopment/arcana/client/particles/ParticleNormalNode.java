package net.kineticdevelopment.arcana.client.particles;

import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.Sprites;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleNormalNode extends Particle {

    public ParticleNormalNode(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);
        setParticleTexture(Sprites.NORMAL_NODE);
        canCollide = false;
        particleGravity = 0.0F;
        setMaxAge(1);
        motionX = 0;
        motionY = 0;
        motionZ = 0;
        particleScale = 3f;
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleNormalNode(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public int getBrightnessForRender(float p_189214_1_) {
        return 3000;
    }

}

