package net.arcanamod.client.render.tiles;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.arcanamod.blocks.tiles.VacuumTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class VacuumTileEntityRender<T extends VacuumTileEntity> extends TileEntityRenderer<T> {
    public static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
    public static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random RANDOM = new Random(31100L);
    private static final List<RenderType> RENDER_TYPES = (List) IntStream.range(0, 16).mapToObj((p_228882_0_) -> {
        return RenderType.getEndPortal(p_228882_0_ + 1);
    }).collect(ImmutableList.toImmutableList());

    public VacuumTileEntityRender(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    public void render(T tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        RANDOM.setSeed(31100L);
        double viewFromDistance = tileEntity.getPos().distanceSq(this.renderDispatcher.renderInfo.getProjectedView(), true);
        int passes = this.getPasses(viewFromDistance);
        float topOffset = this.getOffset();
        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        this.renderCube(tileEntity, topOffset, 0.15F, matrix4f, buffer.getBuffer((RenderType)RENDER_TYPES.get(0)));

        for(int i = 1; i < passes; ++i) {
            this.renderCube(tileEntity, topOffset, 2.0F / (float)(18 - i), matrix4f, buffer.getBuffer((RenderType)RENDER_TYPES.get(i)));
        }
    }

    private void renderCube(T tileEntity, float topOffset, float p_228883_3_, Matrix4f matrix, IVertexBuilder vertexBuilder) {
        float r = (RANDOM.nextFloat() * 0.5F + 0.1F) * p_228883_3_;
        float g = (RANDOM.nextFloat() * 0.5F + 0.4F) * p_228883_3_;
        float b = (RANDOM.nextFloat() * 0.5F + 0.5F) * p_228883_3_;
        this.renderFace(tileEntity, matrix, vertexBuilder, uZ(1.0F), uZ(0.0F), uZ(1.0F), uZ(0.0F), uZ(0.0F), uZ(0.0F), uZ(0.0F), uZ(0.0F), r, g, b, Direction.SOUTH);
        this.renderFace(tileEntity, matrix, vertexBuilder, uZ(1.0F), uZ(0.0F), uZ(0.0F), uZ(1.0F), uZ(1.0F), uZ(1.0F), uZ(1.0F), uZ(1.0F), r, g, b, Direction.NORTH);
        this.renderFace(tileEntity, matrix, vertexBuilder, uZ(0.0F), uZ(0.0F), uZ(0.0F), uZ(1.0F), uZ(1.0F), uZ(0.0F), uZ(0.0F), uZ(1.0F), r, g, b, Direction.EAST);
        this.renderFace(tileEntity, matrix, vertexBuilder, uZ(1.0F), uZ(1.0F), uZ(1.0F), uZ(0.0F), uZ(1.0F), uZ(0.0F), uZ(0.0F), uZ(1.0F), r, g, b, Direction.WEST);
        this.renderFace(tileEntity, matrix, vertexBuilder, uZ(1.0F), uZ(0.0F), uZ(1.0F), uZ(1.0F), uZ(1.0F), uZ(1.0F), uZ(0.0F), uZ(0.0F), r, g, b, Direction.DOWN);
        this.renderFace(tileEntity, matrix, vertexBuilder, uZ(1.0F), uZ(0.0F), topOffset, topOffset, uZ(0.0F), uZ(0.0F), uZ(1.0F), uZ(1.0F), r, g, b, Direction.UP);
    }

    private void renderFace(T vacuumTileEntity, Matrix4f matrix, IVertexBuilder vertexBuilder, float x0, float x1, float y0, float y1, float z0, float z1, float z2, float z3, float r, float g, float b, Direction direction) {
        if (vacuumTileEntity.shouldRenderFace(direction)) {
            vertexBuilder.pos(matrix, x0, y0, z0).color(r, g, b, 1.0F).endVertex();
            vertexBuilder.pos(matrix, x1, y0, z1).color(r, g, b, 1.0F).endVertex();
            vertexBuilder.pos(matrix, x1, y1, z2).color(r, g, b, 1.0F).endVertex();
            vertexBuilder.pos(matrix, x0, y1, z3).color(r, g, b, 1.0F).endVertex();
        }

    }

    // Unique Z axis
    private float uZ(float z){
        if (z==0F) return 0.0001F; else return 0.99981F;
    }

    protected int getPasses(double p) {
        if (p > 36864.0D) {
            return 1;
        } else if (p > 25600.0D) {
            return 3;
        } else if (p > 16384.0D) {
            return 5;
        } else if (p > 9216.0D) {
            return 7;
        } else if (p > 4096.0D) {
            return 9;
        } else if (p > 1024.0D) {
            return 11;
        } else if (p > 576.0D) {
            return 13;
        } else {
            return p > 256.0D ? 14 : 15;
        }
    }

    protected float getOffset() {
        return uZ(0.0F);
    }
}