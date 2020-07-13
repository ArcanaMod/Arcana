package net.arcanamod.client.render.tainted;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.Arcana;
import net.arcanamod.entities.tainted.TaintedGhastEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.GhastModel;
import net.minecraft.util.ResourceLocation;

public class TaintedGhastRender extends MobRenderer<TaintedGhastEntity, GhastModel<TaintedGhastEntity>> {
	private static final ResourceLocation GHAST_TEXTURES = new ResourceLocation(Arcana.MODID,"textures/entity/ghast.png");
	private static final ResourceLocation GHAST_SHOOTING_TEXTURES = new ResourceLocation(Arcana.MODID,"textures/entity/ghast_shooting.png");

	public TaintedGhastRender(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new GhastModel<>(), 1.5F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(TaintedGhastEntity entity) {
		return entity.isAttacking() ? GHAST_SHOOTING_TEXTURES : GHAST_TEXTURES;
	}

	protected void preRenderCallback(TaintedGhastEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(4.5F, 4.5F, 4.5F);
	}
}
