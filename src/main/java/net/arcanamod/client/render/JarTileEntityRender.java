package net.arcanamod.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.arcanamod.Arcana;
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

public class JarTileEntityRender extends TileEntityRenderer<JarTileEntity>
{
    public static final ResourceLocation JAR_CONTENT = new ResourceLocation(Arcana.MODID, "block/jar_placeholder");

    public JarTileEntityRender(TileEntityRendererDispatcher p_i226006_1_)
    {
        super(p_i226006_1_);
    }

    private void add(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v) {
        renderer.pos(stack.getLast().getMatrix(), x, y, z)
                .color(1.0f, 1.0f, 1.0f, 1.0f)
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

        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(JAR_CONTENT);
        IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());

        long time = System.currentTimeMillis();

        Quaternion rotation = Vector3f.XP.rotationDegrees(90);
        float scale = 0.4f;

        matrixStack.push();
        matrixStack.translate(.5, .5, .5);
        matrixStack.rotate(rotation);
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-.5, -.5, -.5);

        add(builder, matrixStack, 0, 1, .4f, sprite.getMinU(), sprite.getMaxV());
        add(builder, matrixStack, 1, 1, .4f, sprite.getMaxU(), sprite.getMaxV());
        add(builder, matrixStack, 1, 0, .4f, sprite.getMaxU(), sprite.getMinV());
        add(builder, matrixStack, 0, 0, .4f, sprite.getMinU(), sprite.getMinV());

        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(.5, .5, .5);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
        matrixStack.scale(scale, 0.4f, scale);
        matrixStack.translate(-.5, -.1, -.5);

        add(builder, matrixStack, 0, 1, 0f, sprite.getMinU(), sprite.getMaxV());
        add(builder, matrixStack, 1, 1, 0f, sprite.getMaxU(), sprite.getMaxV());
        add(builder, matrixStack, 1, 0, 0f, sprite.getMaxU(), sprite.getMinV());
        add(builder, matrixStack, 0, 0, 0f, sprite.getMinU(), sprite.getMinV());

        matrixStack.pop();
    }
}