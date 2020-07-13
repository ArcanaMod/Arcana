package net.arcanamod.client.render.tainted;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.Arcana;
import net.arcanamod.entities.tainted.TaintedCaveSpiderEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.util.ResourceLocation;

public class TaintedCaveSpiderRender extends SpiderRenderer<TaintedCaveSpiderEntity> {
	private static final ResourceLocation CAVE_SPIDER_TEXTURES = new ResourceLocation(Arcana.MODID,"textures/entity/cave_spider.png");

	public TaintedCaveSpiderRender(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize *= 0.55F;
	}

	protected void preRenderCallback(TaintedCaveSpiderEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.55F, 0.55F, 0.55F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(TaintedCaveSpiderEntity entity) {
		return CAVE_SPIDER_TEXTURES;
	}
}
