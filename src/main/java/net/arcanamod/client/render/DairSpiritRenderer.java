package net.arcanamod.client.render;

import net.arcanamod.Arcana;
import net.arcanamod.client.model.DairEntityModel;
import net.arcanamod.entities.DairSpiritEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class DairSpiritRenderer extends MobRenderer<DairSpiritEntity, DairEntityModel<DairSpiritEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Arcana.MODID,
            "textures/entity/dair_spirit.png");

    public DairSpiritRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DairEntityModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(DairSpiritEntity entity) {
        return TEXTURE;
    }
}

