package net.arcanamod.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.JarTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.time.LocalDateTime;

public class JarTileEntityRender extends TileEntityRenderer<JarTileEntity>
{
    public static final ResourceLocation JAR_CONTENT_SIDE = new ResourceLocation(Arcana.MODID, "models/parts/fluid_side");
    public static final ResourceLocation JAR_CONTENT_TOP = new ResourceLocation(Arcana.MODID, "models/parts/fluid_top");

    public JarTileEntityRender(TileEntityRendererDispatcher p_i226006_1_)
    {
        super(p_i226006_1_);
    }

    private void add(IVertexBuilder renderer, MatrixStack stack, Color color, float x, float y, float z, float u, float v) {
        renderer.pos(stack.getLast().getMatrix(), x, y, z)
                .color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1.0f)
                .tex(u, v)
                .lightmap(0, 240)
                .normal(1, 0, 0)
                .endVertex();
    }

    private static float diffFunction(long time, long delta, float scale) {
        long dt = time % (delta * 2);
        if (dt > delta) {
            dt = 2*delta - dt;
        }
        return dt * scale;
    }

    @Override
    public void render(JarTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        TextureAtlasSprite sprite_side = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(JAR_CONTENT_SIDE);
        TextureAtlasSprite sprite_top = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(JAR_CONTENT_TOP);
        IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());

        float vis_amount = tileEntity.getAspectAmount();
        Color aspectColor = tileEntity.getAspectColor();

        if (vis_amount < 12f)
        {
            float vis_scale = vis_amount/10f; //Don't touch this (Arcane Calculations)
            float vis_height = vis_scale-0.2f; //Don't touch this (Arcane Calculations)
            float vis_top = (vis_amount/10f)+0.2f; //Don't touch this (Arcane Calculations)

            Quaternion rotation = Vector3f.XP.rotationDegrees(90);
            float scale = 0.5f;

            matrixStack.push();
            matrixStack.translate(.5, .5, .5);
            matrixStack.rotate(rotation);
            matrixStack.scale(scale, scale, scale);
            matrixStack.translate(-.5, -.5, -.5);

            add(builder, matrixStack, aspectColor, 0, 1, vis_top, sprite_top.getMinU(), sprite_top.getMaxV());
            add(builder, matrixStack, aspectColor, 1, 1, vis_top, sprite_top.getMaxU(), sprite_top.getMaxV());
            add(builder, matrixStack, aspectColor, 1, 0, vis_top, sprite_top.getMaxU(), sprite_top.getMinV());
            add(builder, matrixStack, aspectColor, 0, 0, vis_top, sprite_top.getMinU(), sprite_top.getMinV());

            matrixStack.pop();
            matrixStack.push();
            matrixStack.translate(.5, .5, .5);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
            matrixStack.scale(scale, scale, scale);
            matrixStack.translate(-.5, -0.1, -.5);
            //matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));

            add(builder, matrixStack, aspectColor, 0, 1, 0f, sprite_side.getMinU(), sprite_side.getMaxV());
            add(builder, matrixStack, aspectColor, 1, 1, 0f, sprite_side.getMaxU(), sprite_side.getMaxV());
            add(builder, matrixStack, aspectColor, 1, vis_height, 0f, sprite_side.getMaxU(), sprite_side.getMinV());
            add(builder, matrixStack, aspectColor, 0, vis_height, 0f, sprite_side.getMinU(), sprite_side.getMinV());

            matrixStack.pop();
            matrixStack.push();
            matrixStack.translate(.5, .5, .5);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
            matrixStack.scale(scale, scale, scale);
            matrixStack.translate(-.5, -0.1, -.5);

            add(builder, matrixStack, aspectColor, 0, 1, 0f, sprite_side.getMinU(), sprite_side.getMaxV());
            add(builder, matrixStack, aspectColor, 1, 1, 0f, sprite_side.getMaxU(), sprite_side.getMaxV());
            add(builder, matrixStack, aspectColor, 1, vis_height, 0f, sprite_side.getMaxU(), sprite_side.getMinV());
            add(builder, matrixStack, aspectColor, 0, vis_height, 0f, sprite_side.getMinU(), sprite_side.getMinV());

            matrixStack.pop();
            matrixStack.push();
            matrixStack.translate(.5, .5, .5);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
            matrixStack.rotate(Vector3f.YN.rotationDegrees(90));
            matrixStack.scale(scale, scale, scale);
            matrixStack.translate(-.5, -0.1, -.5);

            add(builder, matrixStack, aspectColor, 0, 1, 0f, sprite_side.getMinU(), sprite_side.getMaxV());
            add(builder, matrixStack, aspectColor, 1, 1, 0f, sprite_side.getMaxU(), sprite_side.getMaxV());
            add(builder, matrixStack, aspectColor, 1, vis_height, 0f, sprite_side.getMaxU(), sprite_side.getMinV());
            add(builder, matrixStack, aspectColor, 0, vis_height, 0f, sprite_side.getMinU(), sprite_side.getMinV());

            matrixStack.pop();
            matrixStack.push();
            matrixStack.translate(.5, .5, .5);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
            matrixStack.scale(scale, scale, scale);
            matrixStack.translate(-.5, -0.1, -.5);

            add(builder, matrixStack, aspectColor, 0, 1, 0f, sprite_side.getMinU(), sprite_side.getMaxV());
            add(builder, matrixStack, aspectColor, 1, 1, 0f, sprite_side.getMaxU(), sprite_side.getMaxV());
            add(builder, matrixStack, aspectColor, 1, vis_height, 0f, sprite_side.getMaxU(), sprite_side.getMinV());
            add(builder, matrixStack, aspectColor, 0, vis_height, 0f, sprite_side.getMinU(), sprite_side.getMinV());

            matrixStack.pop();
        }
    }
}