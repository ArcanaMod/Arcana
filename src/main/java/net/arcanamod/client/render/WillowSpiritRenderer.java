package net.arcanamod.client.render;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.client.model.WillowEntityModel;
import net.arcanamod.entities.SpiritEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WillowSpiritRenderer extends MobRenderer<SpiritEntity, WillowEntityModel<SpiritEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Arcana.MODID,
            "textures/entity/willow_spirit.png");

    public WillowSpiritRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new WillowEntityModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(SpiritEntity entity) {
        return TEXTURE;
    }
}

