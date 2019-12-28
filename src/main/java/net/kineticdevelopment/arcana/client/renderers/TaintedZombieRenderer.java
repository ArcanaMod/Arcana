package net.kineticdevelopment.arcana.client.renderers;

import javax.annotation.Nonnull;

import net.kineticdevelopment.arcana.client.models.TaintedZombieModel;
import net.kineticdevelopment.arcana.common.entities.TaintedZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class TaintedZombieRenderer extends RenderLiving<TaintedZombie> 
{
    private ResourceLocation mobTexture = new ResourceLocation("arcana:textures/entities/taintedzombie.png");

    public static final Factory FACTORY = new Factory();

    public TaintedZombieRenderer(RenderManager rendermanagerIn) 
    {
        super(rendermanagerIn, new TaintedZombieModel(), 1.0F);
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull TaintedZombie entity) 
    {
        return mobTexture;
    }

    public static class Factory implements IRenderFactory<TaintedZombie> 
    {
        @Override
        public Render<? super TaintedZombie> createRenderFor(RenderManager manager) 
        {
            return new TaintedZombieRenderer(manager);
        }

    }
}