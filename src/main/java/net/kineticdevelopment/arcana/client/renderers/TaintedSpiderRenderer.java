package net.kineticdevelopment.arcana.client.renderers;

import javax.annotation.Nonnull;

import net.kineticdevelopment.arcana.client.models.TaintedSpiderModel;
import net.kineticdevelopment.arcana.common.entities.TaintedSpider;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class TaintedSpiderRenderer extends RenderLiving<TaintedSpider> 
{
    private ResourceLocation mobTexture = new ResourceLocation("arcana:textures/entities/taintedspider.png");

    public static final Factory FACTORY = new Factory();

    public TaintedSpiderRenderer(RenderManager rendermanagerIn) 
    {
        super(rendermanagerIn, new TaintedSpiderModel(), 1.0F);
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull TaintedSpider entity) 
    {
        return mobTexture;
    }

    public static class Factory implements IRenderFactory<TaintedSpider> 
    {
        @Override
        public Render<? super TaintedSpider> createRenderFor(RenderManager manager) 
        {
            return new TaintedSpiderRenderer(manager);
        }

    }
}