package net.arcanamod.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.tiles.PedestalTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PedestalTileEntityRenderer extends TileEntityRenderer<PedestalTileEntity>{
	
	public PedestalTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher){
		super(rendererDispatcher);
	}
	
	@SuppressWarnings("deprecation")
	public void render(PedestalTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay){
		matrixStack.push();
		
		ItemStack item = tileEntity.getItem();
		// translation above the pedestal + bobbing
		float bob = MathHelper.sin(((float)tileEntity.getWorld().getGameTime() + partialTicks) / 10.0F) * 0.1F + 0.1F;
		matrixStack.translate(.5f, 1.4f + bob / 2, .5f);
		// spin
		float spin = (((float)tileEntity.getWorld().getGameTime() + partialTicks) / 20.0F);
		matrixStack.rotate(Vector3f.YP.rotation(spin));
		Minecraft.getInstance().getItemRenderer().renderItem(item, ItemCameraTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, buffer);
		
		matrixStack.pop();
	}
}