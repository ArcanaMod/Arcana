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

import java.time.LocalDateTime;

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

        float vis_amount = 0;
        if (tileEntity.getWorld().getBlockState(tileEntity.getPos().down()).getBlock() == ArcanaBlocks.SILVERWOOD_PLANKS.get())
            vis_amount = 3f;
        else if (tileEntity.getWorld().getBlockState(tileEntity.getPos().down()).getBlock() == ArcanaBlocks.DAIR_PLANKS.get())
            vis_amount = 2f;
        else if (tileEntity.getWorld().getBlockState(tileEntity.getPos().down()).getBlock() == ArcanaBlocks.WILLOW_PLANKS.get())
            vis_amount = 1f;
        else if (tileEntity.getWorld().getBlockState(tileEntity.getPos().down()).getBlock() == ArcanaBlocks.EUCALYPTUS_PLANKS.get())
            vis_amount = -1f;
        else if (tileEntity.getWorld().getBlockState(tileEntity.getPos().down()).getBlock() == ArcanaBlocks.HAWTHORN_PLANKS.get())
            vis_amount = LocalDateTime.now().getNano()/100000000f;
        else
            vis_amount = 4f;

        float vis_scale = vis_amount/10f;
        float vis_height = vis_scale;//(-vis_amount/10/vis_scale);
        float vis_top = (vis_amount/10f)+0.4f;

        Quaternion rotation = Vector3f.XP.rotationDegrees(90);
        float scale = 0.4f;

        matrixStack.push();
        matrixStack.translate(.5, .5, .5);
        matrixStack.rotate(rotation);
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-.5, -.5, -.5);

        add(builder, matrixStack, 0, 1, vis_top, sprite.getMinU(), sprite.getMaxV());
        add(builder, matrixStack, 1, 1, vis_top, sprite.getMaxU(), sprite.getMaxV());
        add(builder, matrixStack, 1, 0, vis_top, sprite.getMaxU(), sprite.getMinV());
        add(builder, matrixStack, 0, 0, vis_top, sprite.getMinU(), sprite.getMinV());

        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(.5, .5, .5);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-.5, -0.1, -.5);
        //matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));

        add(builder, matrixStack, 0, 1, 0f, sprite.getMinU(), sprite.getMaxV());
        add(builder, matrixStack, 1, 1, 0f, sprite.getMaxU(), sprite.getMaxV());
        add(builder, matrixStack, 1, vis_height, 0f, sprite.getMaxU(), sprite.getMinV());
        add(builder, matrixStack, 0, vis_height, 0f, sprite.getMinU(), sprite.getMinV());

        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(.5, .5, .5);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-.5, -0.1, -.5);

        add(builder, matrixStack, 0, 1, 0f, sprite.getMinU(), sprite.getMaxV());
        add(builder, matrixStack, 1, 1, 0f, sprite.getMaxU(), sprite.getMaxV());
        add(builder, matrixStack, 1, vis_height, 0f, sprite.getMaxU(), sprite.getMinV());
        add(builder, matrixStack, 0, vis_height, 0f, sprite.getMinU(), sprite.getMinV());

        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(.5, .5, .5);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
        matrixStack.rotate(Vector3f.YN.rotationDegrees(90));
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-.5, -0.1, -.5);

        add(builder, matrixStack, 0, 1, 0f, sprite.getMinU(), sprite.getMaxV());
        add(builder, matrixStack, 1, 1, 0f, sprite.getMaxU(), sprite.getMaxV());
        add(builder, matrixStack, 1, vis_height, 0f, sprite.getMaxU(), sprite.getMinV());
        add(builder, matrixStack, 0, vis_height, 0f, sprite.getMinU(), sprite.getMinV());

        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(.5, .5, .5);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-.5, -0.1, -.5);

        add(builder, matrixStack, 0, 1, 0f, sprite.getMinU(), sprite.getMaxV());
        add(builder, matrixStack, 1, 1, 0f, sprite.getMaxU(), sprite.getMaxV());
        add(builder, matrixStack, 1, vis_height, 0f, sprite.getMaxU(), sprite.getMinV());
        add(builder, matrixStack, 0, vis_height, 0f, sprite.getMinU(), sprite.getMinV());

        matrixStack.pop();
    }
}