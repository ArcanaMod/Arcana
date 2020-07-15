package net.arcanamod.client.model.tainted;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.arcanamod.entities.tainted.TaintedEntity;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class TaintedFoxModel<T extends TaintedEntity> extends EntityModel<T> {
	public final ModelRenderer field_217115_a;
	private final ModelRenderer field_217116_b;
	private final ModelRenderer field_217117_f;
	private final ModelRenderer field_217118_g;
	private final ModelRenderer field_217119_h;
	private final ModelRenderer field_217120_i;
	private final ModelRenderer field_217121_j;
	private final ModelRenderer field_217122_k;
	private final ModelRenderer field_217123_l;
	private final ModelRenderer field_217124_m;
	private float field_217125_n;

	public TaintedFoxModel() {
		this.textureWidth = 48;
		this.textureHeight = 32;
		this.field_217115_a = new ModelRenderer(this, 1, 5);
		this.field_217115_a.addBox(-3.0F, -2.0F, -5.0F, 8.0F, 6.0F, 6.0F);
		this.field_217115_a.setRotationPoint(-1.0F, 16.5F, -3.0F);
		this.field_217116_b = new ModelRenderer(this, 8, 1);
		this.field_217116_b.addBox(-3.0F, -4.0F, -4.0F, 2.0F, 2.0F, 1.0F);
		this.field_217117_f = new ModelRenderer(this, 15, 1);
		this.field_217117_f.addBox(3.0F, -4.0F, -4.0F, 2.0F, 2.0F, 1.0F);
		this.field_217118_g = new ModelRenderer(this, 6, 18);
		this.field_217118_g.addBox(-1.0F, 2.01F, -8.0F, 4.0F, 2.0F, 3.0F);
		this.field_217115_a.addChild(this.field_217116_b);
		this.field_217115_a.addChild(this.field_217117_f);
		this.field_217115_a.addChild(this.field_217118_g);
		this.field_217119_h = new ModelRenderer(this, 24, 15);
		this.field_217119_h.addBox(-3.0F, 3.999F, -3.5F, 6.0F, 11.0F, 6.0F);
		this.field_217119_h.setRotationPoint(0.0F, 16.0F, -6.0F);
		float f = 0.001F;
		this.field_217120_i = new ModelRenderer(this, 13, 24);
		this.field_217120_i.addBox(2.0F, 0.5F, -1.0F, 2.0F, 6.0F, 2.0F, 0.001F);
		this.field_217120_i.setRotationPoint(-5.0F, 17.5F, 7.0F);
		this.field_217121_j = new ModelRenderer(this, 4, 24);
		this.field_217121_j.addBox(2.0F, 0.5F, -1.0F, 2.0F, 6.0F, 2.0F, 0.001F);
		this.field_217121_j.setRotationPoint(-1.0F, 17.5F, 7.0F);
		this.field_217122_k = new ModelRenderer(this, 13, 24);
		this.field_217122_k.addBox(2.0F, 0.5F, -1.0F, 2.0F, 6.0F, 2.0F, 0.001F);
		this.field_217122_k.setRotationPoint(-5.0F, 17.5F, 0.0F);
		this.field_217123_l = new ModelRenderer(this, 4, 24);
		this.field_217123_l.addBox(2.0F, 0.5F, -1.0F, 2.0F, 6.0F, 2.0F, 0.001F);
		this.field_217123_l.setRotationPoint(-1.0F, 17.5F, 0.0F);
		this.field_217124_m = new ModelRenderer(this, 30, 0);
		this.field_217124_m.addBox(2.0F, 0.0F, -1.0F, 4.0F, 9.0F, 5.0F);
		this.field_217124_m.setRotationPoint(-4.0F, 15.0F, -1.0F);
		this.field_217119_h.addChild(this.field_217124_m);
	}

	public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		this.field_217119_h.rotateAngleX = ((float)Math.PI / 2F);
		this.field_217124_m.rotateAngleX = -0.05235988F;
		this.field_217120_i.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.field_217121_j.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.field_217122_k.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.field_217123_l.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.field_217115_a.setRotationPoint(-1.0F, 16.5F, -3.0F);
		this.field_217115_a.rotateAngleY = 0.0F;
		this.field_217120_i.showModel = true;
		this.field_217121_j.showModel = true;
		this.field_217122_k.showModel = true;
		this.field_217123_l.showModel = true;
		this.field_217119_h.setRotationPoint(0.0F, 16.0F, -6.0F);
		this.field_217119_h.rotateAngleZ = 0.0F;
		this.field_217120_i.setRotationPoint(-5.0F, 17.5F, 7.0F);
		this.field_217121_j.setRotationPoint(-1.0F, 17.5F, 7.0F);
	}

	protected Iterable<ModelRenderer> getHeadParts() {
		return ImmutableList.of(this.field_217115_a);
	}

	protected Iterable<ModelRenderer> getBodyParts() {
		return ImmutableList.of(this.field_217119_h, this.field_217120_i, this.field_217121_j, this.field_217122_k, this.field_217123_l);
	}

	/**
	 * Sets this entity's model rotation angles
	 */
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			this.field_217115_a.rotateAngleX = headPitch * ((float)Math.PI / 180F);
			this.field_217115_a.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.getHeadParts().forEach((p_228228_8_) -> {
			p_228228_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
		this.getBodyParts().forEach((p_228227_8_) -> {
			p_228227_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
	}
}
