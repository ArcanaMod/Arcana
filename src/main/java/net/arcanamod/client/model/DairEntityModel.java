package net.arcanamod.client.model;

import net.arcanamod.entities.SpiritEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public class DairEntityModel<T extends SpiritEntity> extends EntityModel<T> {
    public ModelRenderer Body;
    public ModelRenderer Noose;
    public ModelRenderer ShoulderPadLeft;
    public ModelRenderer ShoulderPadRight;
    public ModelRenderer ChainMail;
    public ModelRenderer Head;
    public ModelRenderer HornLeft;
    public ModelRenderer HornRight;

    public DairEntityModel() {
        textureWidth = 48;
        textureHeight = 48;

        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 16.0F, 0.0F);
        setRotationAngle(Body, 0.1745F, 0.0F, 0.0F);
        Body.setTextureOffset(0, 31).addBox(-3.0F, 0.0F, -2.5F, 6.0F, 4.0F, 5.0F, 0.0F, false);

        Noose = new ModelRenderer(this);
        Noose.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(Noose);
        setRotationAngle(Noose, -0.1745F, 0.0F, 0.0F);
        Noose.setTextureOffset(32, 43).addBox(-2.0F, -0.7F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

        Head = new ModelRenderer(this);
        Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        Noose.addChild(Head);
        Head.setTextureOffset(0, 0).addBox(-3.0F, -6.4F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

        HornLeft = new ModelRenderer(this);
        HornLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.addChild(HornLeft);
        HornLeft.setTextureOffset(14, 13).addBox(3.0F, -7.0F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);

        HornRight = new ModelRenderer(this);
        HornRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.addChild(HornRight);
        HornRight.setTextureOffset(14, 13).addBox(-4.0F, -7.0F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);

        ChainMail = new ModelRenderer(this);
        ChainMail.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(ChainMail);
        ChainMail.setTextureOffset(0, 40).addBox(-2.0F, 3.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        ShoulderPadLeft = new ModelRenderer(this);
        ShoulderPadLeft.setRotationPoint(3.0F, 0.0F, 0.0F);
        Body.addChild(ShoulderPadLeft);
        setRotationAngle(ShoulderPadLeft, 0.0F, 0.0F, -0.2618F);
        ShoulderPadLeft.setTextureOffset(0, 12).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);

        ShoulderPadRight = new ModelRenderer(this);
        ShoulderPadRight.setRotationPoint(-3.0F, 0.0F, 0.0F);
        Body.addChild(ShoulderPadRight);
        setRotationAngle(ShoulderPadRight, 0.0F, 0.0F, 0.2618F);
        ShoulderPadRight.setTextureOffset(0, 12).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.Head.rotateAngleX = headPitch / (180F / (float)Math.PI);
        this.Head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
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
}
