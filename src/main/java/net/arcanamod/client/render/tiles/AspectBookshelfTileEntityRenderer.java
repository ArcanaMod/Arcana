package net.arcanamod.client.render.tiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.tiles.AspectBookshelfTileEntity;
import net.arcanamod.items.PhialItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;

public class AspectBookshelfTileEntityRenderer extends TileEntityRenderer<AspectBookshelfTileEntity> {
    public static final ResourceLocation PHIAL_TEXTURE = new ResourceLocation(Arcana.MODID, "block/aspect_bookshelf_vial");

    public AspectBookshelfTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    private void add(IVertexBuilder renderer, MatrixStack stack, Color color, float x, float y, float z, float u, float v, int lightmap){
        renderer.pos(stack.getLast().getMatrix(), x, y, z)
                .color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f)
                .tex(u, v)
                .lightmap(lightmap)
                .normal(1, 0, 0)
                .endVertex();
    }

    public static final ResourceLocation JAR_CONTENT_SIDE = new ResourceLocation(Arcana.MODID, "models/parts/fluid_side");
    public static final ResourceLocation JAR_CONTENT_TOP = new ResourceLocation(Arcana.MODID, "models/parts/fluid_top");
    @Override
    public void render(AspectBookshelfTileEntity tileEntity, float partialTicks, @Nonnull MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(PHIAL_TEXTURE);
        IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());


        TextureAtlasSprite spriteSide = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(JAR_CONTENT_TOP);

        Quaternion q = Quaternion.ONE;
        switch (tileEntity.rotation) {
            case NORTH:
                q = new Quaternion(0, 0, 0, true);
                break;
            case WEST:
                q = new Quaternion(0, 90, 0, true);
                break;
            case SOUTH:
                q = new Quaternion(0, 180, 0, true);
                break;
            case EAST:
                q = new Quaternion(0, 270, 0, true);
                break;
        }
        matrixStack.translate(.5, 0, .5);
        matrixStack.rotate(q);
        matrixStack.translate(-.5, 0, -.5);


        for (int i = 0; i <= 8; i++) {
            if (tileEntity.getStackInSlot(i).getItem() instanceof PhialItem) {
                matrixStack.push();
                int slotX = 2 - ((i) % 3);
                int slotY = 2 - ((i) / 3);

                Color colour = PhialItem.getAspect(tileEntity.getStackInSlot(i)) != Aspects.EMPTY ?
                        new Color(PhialItem.getAspect(tileEntity.getStackInSlot(i)).getColorRange().get(2)) : Color.WHITE;
                Color c = new Color(colour.getRGB()-0x80000000);

                //Base
                add(builder, matrixStack, c, 0.3125f + (0.3125f * slotX), 0.0625f + (0.3125f * slotY), 0.5625f, spriteSide.getMaxU(), spriteSide.getMaxV(), combinedLight);
                add(builder, matrixStack, c, 0.0625f + (0.3125f * slotX), 0.0625f + (0.3125f * slotY), 0.5625f, spriteSide.getMinU(), spriteSide.getMaxV(), combinedLight);
                add(builder, matrixStack, c, 0.0625f + (0.3125f * slotX), 0.3125f + (0.3125f * slotY), 0.5625f, spriteSide.getMinU(), spriteSide.getMinV(), combinedLight);
                add(builder, matrixStack, c, 0.3125f + (0.3125f * slotX), 0.3125f + (0.3125f * slotY), 0.5625f, spriteSide.getMaxU(), spriteSide.getMinV(), combinedLight);


                /*
                //Front Handle
                add(builder, matrixStack, c, 0.28125f + (0.3125f * slotX), 0.09375f + (0.3125f * slotY), 0.4375f, spriteSide.getMaxU(), spriteSide.getMaxV(), combinedLight);
                add(builder, matrixStack, c, 0.09375f + (0.3125f * slotX), 0.09375f + (0.3125f * slotY), 0.4375f, spriteSide.getMinU(), spriteSide.getMaxV(), combinedLight);
                add(builder, matrixStack, c, 0.09375f + (0.3125f * slotX), 0.28125f + (0.3125f * slotY), 0.4375f, spriteSide.getMinU(), spriteSide.getMinV(), combinedLight);
                add(builder, matrixStack, c, 0.28125f + (0.3125f * slotX), 0.28125f + (0.3125f * slotY), 0.4375f, spriteSide.getMaxU(), spriteSide.getMinV(), combinedLight);

                //Back Handle
                add(builder, matrixStack, c, 0.28125f + (0.3125f * slotX), 0.09375f + (0.3125f * slotY), 0.5000f, spriteSide.getMaxU(), spriteSide.getMaxV(), combinedLight);
                add(builder, matrixStack, c, 0.09375f + (0.3125f * slotX), 0.09375f + (0.3125f * slotY), 0.5000f, spriteSide.getMinU(), spriteSide.getMaxV(), combinedLight);
                add(builder, matrixStack, c, 0.09375f + (0.3125f * slotX), 0.28125f + (0.3125f * slotY), 0.5000f, spriteSide.getMinU(), spriteSide.getMinV(), combinedLight);
                add(builder, matrixStack, c, 0.28125f + (0.3125f * slotX), 0.28125f + (0.3125f * slotY), 0.5000f, spriteSide.getMaxU(), spriteSide.getMinV(), combinedLight);
                 */




                matrixStack.pop();
            }
        }
    }
}
