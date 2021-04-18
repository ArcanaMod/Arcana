package net.arcanamod.client.render.tiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class ResearchTableTileEntityRender extends TileEntityRenderer<ResearchTableTileEntity> {
	public ResearchTableTileEntityRender(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(ResearchTableTileEntity researchTableTileEntity, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, int i1) {
	}
}
