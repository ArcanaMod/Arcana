package net.arcanamod.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BonedBookJavaModel extends EntityModel<Entity> {
	/**
	 * Sets this entity's model rotation angles
	 *
	 * @param entityIn
	 * @param limbSwing
	 * @param limbSwingAmount
	 * @param ageInTicks
	 * @param netHeadYaw
	 * @param headPitch
	 */
	@Override
	public void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

	}
/*	private final ModelRenderer bone;

	public BonedBookJavaModel() {
		textureWidth = 16;
		textureHeight = 16;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 16.0F, 0.0F);
		bone.setTextureOffset(0, 6).addBox(0.0F, -4.0F, -0.99F, 5.0F, 8.0F, 1.0F, 0.0F, false);
		bone.setTextureOffset(0, 6).addBox(0.0F, -4.0F, -0.01F, 5.0F, 8.0F, 1.0F, 0.0F, false);
		bone.setTextureOffset(0, 6).addBox(0.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.005F, 0.0F, false);
		bone.setTextureOffset(0, 6).addBox(0.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.005F, 0.0F, false);
		bone.setTextureOffset(0, 6).addBox(0.0F, -4.0F, -0.99F, 5.0F, 8.0F, 1.0F, 0.0F, false);
		bone.setTextureOffset(0, 6).addBox(0.0F, -4.0F, -0.99F, 5.0F, 8.0F, 1.0F, 0.0F, false);
		bone.setTextureOffset(0, 6).addBox(0.0F, -4.0F, -0.99F, 5.0F, 8.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		bone.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}*/
}
/*
package net.arcanamod.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class ArcanumModel extends Model {
	private final ModelRenderer coverRight = (new ModelRenderer(48, 48, 28, 13)).addBox(1.0F, -4.0F, -6.0F, 1.0F, 12.0F, 9.0F);
	private final ModelRenderer coverLeft = (new ModelRenderer(48, 48, 28, 25)).addBox(-2.0F, -4.0F, -6.0F, 1.0F, 12.0F, 9.0F);
	private final ModelRenderer bookSpine = (new ModelRenderer(48, 48, 31, 9)).addBox(-1.0F, -4.0F, -6.0F, 2.0F, 12.0F, 1.0F);
	private final ModelRenderer pagesRight;
	private final ModelRenderer pagesLeft;
	private final List<ModelRenderer> field_228246_h_;

	public ArcanumModel() {
		super(RenderType::getEntitySolid);

		//setRotationAngle(coverLeft,(float)Math.PI / 2F,(float)Math.PI / 2F,(float)Math.PI / 2F);
		pagesLeft = (new ModelRenderer(48, 48, 12, 15)).addBox(-1.0F, -3.5F, -5.0F, 1.0F, 11.0F, 7.0F);
		pagesRight = (new ModelRenderer(48, 48, 10, 15)).addBox(0.0F, -3.5F, -5.0F, 1.0F, 11.0F, 7.0F);
		this.field_228246_h_ = ImmutableList.of(this.coverRight, this.coverLeft, this.bookSpine/*, this.pagesRight, this.pagesLeft, this.flippingPageRight, this.flippingPageLeft);
		this.coverRight.setRotationPoint(0.0F, 0.0F, -1.0F);
		this.coverLeft.setRotationPoint(0.0F, 0.0F, 1.0F);
		this.bookSpine.rotateAngleY = ((float)Math.PI / 2F);
		}

@ Override
public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		this.func_228249_b_(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		}

public void func_228249_b_(MatrixStack p_228249_1_, IVertexBuilder p_228249_2_, int p_228249_3_, int p_228249_4_, float p_228249_5_, float p_228249_6_, float p_228249_7_, float p_228249_8_) {
		this.field_228246_h_.forEach((p_228248_8_) -> {
		p_228248_8_.render(p_228249_1_, p_228249_2_, p_228249_3_, p_228249_4_, p_228249_5_, p_228249_6_, p_228249_7_, p_228249_8_);
		});
		}

public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
		}

public void func_228247_a_(float p_228247_1_, float p_228247_2_, float p_228247_3_, float p_228247_4_) {
		float f = (MathHelper.sin(p_228247_1_ * 0.02F) * 0.1F + 1.25F) * p_228247_4_;
		//this.coverRight.rotateAngleY = (float)Math.PI + f;
		//this.coverLeft.rotateAngleY = -f;
		//this.pagesRight.rotateAngleY = f;
		//this.pagesLeft.rotateAngleY = -f;
		//this.flippingPageRight.rotateAngleY = f - f * 2.0F * p_228247_2_;
		//this.flippingPageLeft.rotateAngleY = f - f * 2.0F * p_228247_3_;
		//this.pagesRight.rotationPointX = MathHelper.sin(f);
		//this.pagesLeft.rotationPointX = MathHelper.sin(f);
		//this.flippingPageRight.rotationPointX = MathHelper.sin(f);
		//this.flippingPageLeft.rotationPointX = MathHelper.sin(f);
		}
		}*/