package net.kineticdevelopment.arcana.client.renderers;

import javax.annotation.Nonnull;

import net.kineticdevelopment.arcana.client.models.TaintedChickenModel;
import net.kineticdevelopment.arcana.common.entities.TaintedChicken;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class TaintedChickenRenderer extends RenderLiving<TaintedChicken> 
{
    private ResourceLocation mobTexture = new ResourceLocation("arcana:textures/entities/taintedchicken.png");

    public static final Factory FACTORY = new Factory();

    public TaintedChickenRenderer(RenderManager rendermanagerIn) 
    {
        super(rendermanagerIn, new TaintedChickenModel(), 1.0F);
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull TaintedChicken entity) 
    {
        return mobTexture;
    }

    public static class Factory implements IRenderFactory<TaintedChicken> 
    {
        @Override
        public Render<? super TaintedChicken> createRenderFor(RenderManager manager) 
        {
            return new TaintedChickenRenderer(manager);
        }

    }
}