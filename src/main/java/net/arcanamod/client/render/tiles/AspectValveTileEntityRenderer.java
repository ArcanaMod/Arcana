package net.arcanamod.client.render.tiles;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.tiles.AspectValveTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.ItemLayerModel;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.block.SixWayBlock.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectValveTileEntityRenderer extends TileEntityRenderer<AspectValveTileEntity>{
	
	public static final ResourceLocation GEAR_TEX = new ResourceLocation(Arcana.MODID, "block/essentia_tube_valve");
	private static TextureAtlasSprite gearSprite;
	private static ImmutableList<BakedQuad> gearModel;
	
	public AspectValveTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher){
		super(rendererDispatcher);
	}
	
	@SuppressWarnings("deprecation")
	public void render(AspectValveTileEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay){
		if(gearModel == null){
			if(gearSprite == null)
				gearSprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(GEAR_TEX);
			gearModel = ItemLayerModel.getQuadsForSprite(0, gearSprite, TransformationMatrix.identity());
		}
		matrixStack.push();
		matrixStack.translate(.5, .5, .5);
		// rotate to pick an empty side
		BlockState state = te.getWorld().getBlockState(te.getPos());
		if(state.get(UP))
			if(!state.get(NORTH))
				matrixStack.rotate(Vector3f.XN.rotationDegrees(90));
			else if(!state.get(EAST))
				matrixStack.rotate(Vector3f.ZN.rotationDegrees(90));
			else if(!state.get(SOUTH))
				matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
			else if(!state.get(WEST))
				matrixStack.rotate(Vector3f.ZP.rotationDegrees(90));
			else if(!state.get(DOWN))
				matrixStack.rotate(Vector3f.XN.rotationDegrees(180));
		// set base gear height
		matrixStack.translate(0, .75, 0);
		// modify height and rotation based on state
		if(te.enabled()){
			// display higher up
			// if lastChangedTick is less than 20 different from the current tick, transition
			float tickDiff = Math.min(10, (te.getWorld().getGameTime() + partialTicks) - te.getLastChangedTick());
			float heightDiff = (tickDiff / 10) * .07f;
			float rotationDiff = (tickDiff / 10) * 135;
			matrixStack.translate(0, heightDiff, 0);
			matrixStack.rotate(Vector3f.YN.rotationDegrees(rotationDiff + 45));
		}else{
			float tickDiff = Math.min(10, (te.getWorld().getGameTime() + partialTicks) - te.getLastChangedTick());
			float heightDiff = (1 - (tickDiff / 10)) * .07f;
			float rotationDiff = (1 - (tickDiff / 10)) * 135;
			matrixStack.translate(0, heightDiff, 0);
			matrixStack.rotate(Vector3f.YN.rotationDegrees(rotationDiff + 45));
		}
		// render
		IVertexBuilder builder = buffer.getBuffer(RenderType.getCutout());
		MatrixStack.Entry entry = matrixStack.getLast();
		matrixStack.translate(-.5, 0, -.5);
		matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
		renderGearModel(entry, builder, combinedLight, combinedOverlay);
		matrixStack.pop();
	}
	
	private void renderGearModel(MatrixStack.Entry entry, IVertexBuilder buff, int combinedLight, int combinedOverlay){
		for(BakedQuad quad : gearModel)
			buff.addQuad(entry, quad, 1, 1, 1, combinedLight, combinedOverlay);
	}
}
