package net.arcanamod.client.model;

import net.arcanamod.entities.KoalaEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public class KoalaEntityModel<T extends KoalaEntity> extends EntityModel<T> {
    private final ModelRenderer Body;
    private final ModelRenderer Tail;
    private final ModelRenderer Head;
    private final ModelRenderer Ears;
    private final ModelRenderer Right;
    private final ModelRenderer Left;
    private final ModelRenderer Nose;
    private final ModelRenderer Legs;
    private final ModelRenderer Back;
    private final ModelRenderer RightBack;
    private final ModelRenderer LeftBack;
    private final ModelRenderer Front;
    private final ModelRenderer RightFront;
    private final ModelRenderer LeftFront;

    public KoalaEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 20.5F, -0.5F);
        setRotationAngle(Body, 1.5708F, 0.0F, 0.0F);
        Body.setTextureOffset(0, 18).addBox(-2.5F, -5.5F, -0.5F, 5.0F, 9.0F, 4.0F, 0.0F, false);

        Tail = new ModelRenderer(this);
        Tail.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(Tail);
        Tail.setTextureOffset(22, 23).addBox(-1.0F, 2.5F, 2.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        Head = new ModelRenderer(this);
        Head.setRotationPoint(0.0F, -4.5F, 2.5F);
        Body.addChild(Head);
        Head.setTextureOffset(0, 0).addBox(-3.0F, -4.0F, -2.0F, 6.0F, 5.0F, 5.0F, 0.0F, false);

        Ears = new ModelRenderer(this);
        Ears.setRotationPoint(-3.0F, -1.5F, 2.0F);
        Head.addChild(Ears);
        setRotationAngle(Ears, 1.5708F, 0.0F, -3.1416F);


        Right = new ModelRenderer(this);
        Right.setRotationPoint(5.5F, 3.0F, -3.5F);
        Ears.addChild(Right);
        Right.setTextureOffset(6, 10).addBox(-6.5F, -3.5F, 3.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        Left = new ModelRenderer(this);
        Left.setRotationPoint(-1.0F, -0.5F, -2.0F);
        Ears.addChild(Left);
        Left.setTextureOffset(6, 10).addBox(-7.0F, 0.0F, 1.5F, 3.0F, 3.0F, 1.0F, 0.0F, true);

        Nose = new ModelRenderer(this);
        Nose.setRotationPoint(0.0F, -3.5F, -0.5F);
        Head.addChild(Nose);
        setRotationAngle(Nose, 1.5708F, 3.1416F, -3.1416F);
        Nose.setTextureOffset(0, 10).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);

        Legs = new ModelRenderer(this);
        Legs.setRotationPoint(-3.0F, 0.5F, -3.5F);
        Body.addChild(Legs);
        setRotationAngle(Legs, 1.5708F, 0.0F, 0.0F);


        Back = new ModelRenderer(this);
        Back.setRotationPoint(0.0F, 0.0F, 0.0F);
        Legs.addChild(Back);


        RightBack = new ModelRenderer(this);
        RightBack.setRotationPoint(0.0F, 4.0F, -1.0F);
        Back.addChild(RightBack);
        RightBack.setTextureOffset(18, 29).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        LeftBack = new ModelRenderer(this);
        LeftBack.setRotationPoint(6.0F, 4.0F, -1.0F);
        Back.addChild(LeftBack);
        LeftBack.setTextureOffset(18, 29).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        Front = new ModelRenderer(this);
        Front.setRotationPoint(0.0F, 0.0F, 0.0F);
        Legs.addChild(Front);


        RightFront = new ModelRenderer(this);
        RightFront.setRotationPoint(0.0F, 4.0F, 4.0F);
        Front.addChild(RightFront);
        RightFront.setTextureOffset(18, 29).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        LeftFront = new ModelRenderer(this);
        LeftFront.setRotationPoint(6.0F, 4.0F, 4.0F);
        Front.addChild(LeftFront);
        LeftFront.setTextureOffset(18, 29).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.Head.rotateAngleX = headPitch / (180F / (float)Math.PI);
        this.Head.rotateAngleZ = netHeadYaw / (180F / (float)Math.PI);
        this.RightBack.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.LeftBack.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.RightFront.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.LeftFront.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setLivingAnimations(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTick);
    }
}
