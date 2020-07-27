package net.arcanamod.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.arcanamod.entities.SpiritEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class WillowEntityModel<T extends SpiritEntity> extends EntityModel<T> {
    private final ModelRenderer Head;
    private final ModelRenderer LeftHorn;
    private final ModelRenderer SideHeadGear;
    private final ModelRenderer HeadGearFront;
    private final ModelRenderer HeadGearBand2;
    private final ModelRenderer HeadGearBand;
    private final ModelRenderer LeftHornFront;
    private final ModelRenderer RightHorn;
    private final ModelRenderer RightHornFront;
    private final ModelRenderer Body;
    private final ModelRenderer Spine;
    private final ModelRenderer FeatherRightSide;
    private final ModelRenderer LeftArmPlate;
    private final ModelRenderer ArmorPlateFront;
    private final ModelRenderer Chainmail;
    private final ModelRenderer FeatherLeft;
    private final ModelRenderer FeatherLeftSide;
    private final ModelRenderer RightArmPlate;
    private final ModelRenderer FeatherRight;

    public WillowEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        Head = new ModelRenderer(this);
        Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.setTextureOffset(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

        LeftHorn = new ModelRenderer(this);
        LeftHorn.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.addChild(LeftHorn);
        setRotationAngle(LeftHorn, 0.0F, 0.0F, 0.1745F);
        LeftHorn.setTextureOffset(28, 0).addBox(1.2F, -9.3F, 0.5F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        SideHeadGear = new ModelRenderer(this);
        SideHeadGear.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.addChild(SideHeadGear);
        SideHeadGear.setTextureOffset(0, 12).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 3.0F, 3.0F, 0.0F, false);

        HeadGearFront = new ModelRenderer(this);
        HeadGearFront.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.addChild(HeadGearFront);
        HeadGearFront.setTextureOffset(18, 0).addBox(-2.0F, -5.5F, -4.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);

        HeadGearBand2 = new ModelRenderer(this);
        HeadGearBand2.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.addChild(HeadGearBand2);
        HeadGearBand2.setTextureOffset(0, 29).addBox(-3.5F, -3.0F, -3.5F, 7.0F, 1.0F, 7.0F, 0.0F, false);

        HeadGearBand = new ModelRenderer(this);
        HeadGearBand.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.addChild(HeadGearBand);
        HeadGearBand.setTextureOffset(0, 29).addBox(-3.5F, -5.0F, -3.5F, 7.0F, 1.0F, 7.0F, 0.0F, false);

        LeftHornFront = new ModelRenderer(this);
        LeftHornFront.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.addChild(LeftHornFront);
        setRotationAngle(LeftHornFront, 0.0F, 0.0F, 0.1745F);
        LeftHornFront.setTextureOffset(28, 0).addBox(1.2F, -7.7F, -2.5F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        RightHorn = new ModelRenderer(this);
        RightHorn.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.addChild(RightHorn);
        setRotationAngle(RightHorn, 0.0F, 0.0F, -0.1745F);
        RightHorn.setTextureOffset(28, 0).addBox(-2.2F, -9.3F, 0.5F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        RightHornFront = new ModelRenderer(this);
        RightHornFront.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.addChild(RightHornFront);
        setRotationAngle(RightHornFront, 0.0F, 0.0F, -0.1745F);
        RightHornFront.setTextureOffset(28, 0).addBox(-2.2F, -7.7F, -2.5F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 1.0F, 0.0F);
        setRotationAngle(Body, 0.0873F, 0.0F, 0.0F);
        Body.setTextureOffset(34, 0).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 5.0F, 4.0F, 0.0F, false);

        Spine = new ModelRenderer(this);
        Spine.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(Spine);
        setRotationAngle(Spine, 0.0873F, 0.0F, 0.0F);
        Spine.setTextureOffset(0, 0).addBox(-0.5F, 4.1F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        FeatherRightSide = new ModelRenderer(this);
        FeatherRightSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(FeatherRightSide);
        setRotationAngle(FeatherRightSide, -0.7854F, -1.3963F, 0.0F);
        FeatherRightSide.setTextureOffset(0, 18).addBox(-1.9F, -4.7F, 1.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        LeftArmPlate = new ModelRenderer(this);
        LeftArmPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(LeftArmPlate);
        setRotationAngle(LeftArmPlate, 0.0F, 0.0F, -0.1396F);
        LeftArmPlate.setTextureOffset(22, 15).addBox(3.0F, -0.2F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);

        ArmorPlateFront = new ModelRenderer(this);
        ArmorPlateFront.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(ArmorPlateFront);
        ArmorPlateFront.setTextureOffset(18, 0).addBox(-2.0F, 0.6F, 1.9F, 4.0F, 4.0F, 1.0F, 0.0F, false);

        Chainmail = new ModelRenderer(this);
        Chainmail.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(Chainmail);
        Chainmail.setTextureOffset(21, 9).addBox(-2.5F, 5.0F, -1.5F, 5.0F, 3.0F, 3.0F, 0.0F, false);

        FeatherLeft = new ModelRenderer(this);
        FeatherLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(FeatherLeft);
        setRotationAngle(FeatherLeft, -0.9599F, 0.5236F, 0.0F);
        FeatherLeft.setTextureOffset(0, 18).addBox(0.4F, -5.5F, 0.9F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        FeatherLeftSide = new ModelRenderer(this);
        FeatherLeftSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(FeatherLeftSide);
        setRotationAngle(FeatherLeftSide, -0.7854F, 1.3963F, 0.0F);
        FeatherLeftSide.setTextureOffset(0, 18).addBox(-0.1F, -4.7F, 1.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        RightArmPlate = new ModelRenderer(this);
        RightArmPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(RightArmPlate);
        setRotationAngle(RightArmPlate, 0.0F, 0.0F, 0.1396F);
        RightArmPlate.setTextureOffset(22, 15).addBox(-4.0F, -0.2F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);

        FeatherRight = new ModelRenderer(this);
        FeatherRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(FeatherRight);
        setRotationAngle(FeatherRight, -0.9599F, -0.5236F, 0.0F);
        FeatherRight.setTextureOffset(0, 18).addBox(-2.0F, -5.5F, 0.9F, 2.0F, 4.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.Head.rotateAngleX = headPitch / (180F / (float)Math.PI);
        this.Head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Head.render(matrixStack, buffer, packedLight, packedOverlay);
        Body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
