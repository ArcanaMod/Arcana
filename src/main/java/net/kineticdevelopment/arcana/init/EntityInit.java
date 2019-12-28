package net.kineticdevelopment.arcana.init;

import net.kineticdevelopment.arcana.client.renderers.TaintedChickenRenderer;
import net.kineticdevelopment.arcana.client.renderers.TaintedSpiderRenderer;
import net.kineticdevelopment.arcana.client.renderers.TaintedZombieRenderer;
import net.kineticdevelopment.arcana.common.entities.TaintedChicken;
import net.kineticdevelopment.arcana.common.entities.TaintedSpider;
import net.kineticdevelopment.arcana.common.entities.TaintedZombie;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityInit 
{
	public static void init() 
    {
        int id = 1;
        EntityRegistry.registerModEntity(new ResourceLocation("arcana", "taintedspider"), TaintedSpider.class, "TaintedSpider", id++, Main.instance, 64, 3, true, 0, 3);
        EntityRegistry.registerModEntity(new ResourceLocation("arcana", "taintedchicken"), TaintedChicken.class, "TaintedChicken", id++, Main.instance, 64, 3, true, 0, 3);
        EntityRegistry.registerModEntity(new ResourceLocation("arcana", "taintedzombie"), TaintedZombie.class, "TaintedZombie", id++, Main.instance, 64, 3, true, 0, 3);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() 
    {
        RenderingRegistry.registerEntityRenderingHandler(TaintedSpider.class, TaintedSpiderRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TaintedChicken.class, TaintedChickenRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TaintedZombie.class, TaintedZombieRenderer.FACTORY);
    }
}
