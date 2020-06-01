package net.arcanamod.client.render;

import net.arcanamod.Arcana;
import net.arcanamod.client.model.KoalaEntityModel;
import net.arcanamod.entities.KoalaEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class KoalaEntityRender extends MobRenderer<KoalaEntity, KoalaEntityModel<KoalaEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Arcana.MODID,
            "textures/entity/koala.png");

    public KoalaEntityRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new KoalaEntityModel<KoalaEntity>(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(KoalaEntity entity) {
        return TEXTURE;
    }
}
