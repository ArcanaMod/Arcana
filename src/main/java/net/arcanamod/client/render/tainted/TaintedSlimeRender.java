package net.arcanamod.client.render.tainted;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.Arcana;
import net.arcanamod.entities.tainted.TaintedSlimeEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeGelLayer;
import net.minecraft.client.renderer.entity.model.SlimeModel;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TaintedSlimeRender extends MobRenderer<TaintedSlimeEntity, SlimeModel<TaintedSlimeEntity>> {
	private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation(Arcana.MODID,"textures/entity/tainted_slime.png");

	public TaintedSlimeRender(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new SlimeModel<>(16), 0.25F);
		this.addLayer(new SlimeGelLayer<>(this));
	}

	public void render(TaintedSlimeEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		this.shadowSize = 0.25F * (float)entityIn.getSlimeSize();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	protected void preRenderCallback(TaintedSlimeEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		float f = 0.999F;
		matrixStackIn.scale(0.999F, 0.999F, 0.999F);
		matrixStackIn.translate(0.0D, (double)0.001F, 0.0D);
		float f1 = (float)entitylivingbaseIn.getSlimeSize();
		float f2 = MathHelper.lerp(partialTickTime, entitylivingbaseIn.prevSquishFactor, entitylivingbaseIn.squishFactor) / (f1 * 0.5F + 1.0F);
		float f3 = 1.0F / (f2 + 1.0F);
		matrixStackIn.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(TaintedSlimeEntity entity) {
		return SLIME_TEXTURES;
	}
}