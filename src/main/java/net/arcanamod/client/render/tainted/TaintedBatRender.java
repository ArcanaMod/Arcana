package net.arcanamod.client.render.tainted;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.Arcana;
import net.arcanamod.client.model.tainted.TaintedBatModel;
import net.arcanamod.entities.tainted.TaintedBatEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BatModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TaintedBatRender extends MobRenderer<TaintedBatEntity, TaintedBatModel> {
	private static final ResourceLocation BAT_TEXTURES = new ResourceLocation(Arcana.MODID,"textures/entity/tainted_bat.png");

	public TaintedBatRender(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new TaintedBatModel(), 0.25F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(TaintedBatEntity entity) {
		return BAT_TEXTURES;
	}

	protected void preRenderCallback(TaintedBatEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.35F, 0.35F, 0.35F);
	}

	protected void applyRotations(TaintedBatEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
		if (entityLiving.getIsBatHanging()) {
			matrixStackIn.translate(0.0D, (double)-0.1F, 0.0D);
		} else {
			matrixStackIn.translate(0.0D, (double)(MathHelper.cos(ageInTicks * 0.3F) * 0.1F), 0.0D);
		}

		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}
}