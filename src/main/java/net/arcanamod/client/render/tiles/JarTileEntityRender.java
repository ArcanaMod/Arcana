package net.arcanamod.client.render.tiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.blocks.tiles.JarTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JarTileEntityRender extends TileEntityRenderer<JarTileEntity>{
	public static final ResourceLocation JAR_CONTENT_TOP = new ResourceLocation(Arcana.MODID, "models/parts/fluid_top");
	public static final ResourceLocation JAR_CONTENT_SIDE = new ResourceLocation(Arcana.MODID, "models/parts/fluid_side");
	public static final ResourceLocation JAR_CONTENT_BOTTOM = new ResourceLocation(Arcana.MODID, "models/parts/fluid_bottom");
	public static final ResourceLocation JAR_LABEL = new ResourceLocation(Arcana.MODID, "models/parts/jar_label");
	
	public JarTileEntityRender(TileEntityRendererDispatcher rendererDispatcherIn){
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
	
	@SuppressWarnings("deprecation")
	@Override
	public void render(JarTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay){

		TextureAtlasSprite spriteTop = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(JAR_CONTENT_TOP);
		TextureAtlasSprite spriteSide = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(JAR_CONTENT_SIDE);
		TextureAtlasSprite spriteBottom = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(JAR_CONTENT_BOTTOM);
		TextureAtlasSprite label = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(JAR_LABEL);
		TextureAtlasSprite aspect = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(tileEntity.getPaperAspectLocation());
		IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());
		IVertexBuilder label_builder = buffer.getBuffer(RenderType.getCutout());
		
		// 0-100
		float visAmount = ArcanaConfig.NO_JAR_ANIMATION.get() ? tileEntity.vis.getHolder(0).getCurrentVis() : (float) tileEntity.getClientVis(partialTicks);
		Color aspectColor = tileEntity.getAspectColor();
		Direction labelSide = tileEntity.getLabelSide();
		
		float visScaled = visAmount / 90;
		float visBase = -.3f;
		float visHeight = visScaled + visBase;

		float scale = 0.4f;

		// label
		if (labelSide != null){
			matrixStack.push();

			Quaternion q;
			float xt, zt;
			float xta, yta = -0.68f, zta;

			switch (labelSide) {
				case NORTH:
					q = new Quaternion(0,-90,0,true);
					xt = 0.45f;
					zt = -1.75f;
					xta = -0.02f;
					yta = -0.90f;
					zta = 0.1f;
					break;
				case SOUTH:
					q = new Quaternion(0,90,0,true);
					xt = -2.05f;
					zt = 0.845f;
					xta = -0.02f;
					yta = -0.90f;
					zta = 0.1f;
					break;
				case WEST:
					q = new Quaternion(0,0,0,true);
					xt = 0.45f;
					zt = 0.85f;
					xta = -0.02f;
					yta = -0.90f;
					zta = 0.1f;
					break;
				case EAST:
					q = new Quaternion(0,180,0,true);
					xt = -2.05f;
					zt = -1.75f;
					xta = -0.02f;
					yta = -0.90f;
					zta = 0.1f;
					break;
				default:
					q = new Quaternion(0,0,0,true);
					xt = 0;
					zt = 0;
					xta = 0;
					zta = 0;
					break;
			}

			matrixStack.scale(scale, scale, scale);
			matrixStack.rotate(q);
			matrixStack.translate(xt, .5f, zt);
			
			q = new Quaternion(tileEntity.label.renderRotation,0,0,true);
			matrixStack.rotate(q);
			matrixStack.translate(0,0,-(tileEntity.label.renderRotation/200f));

			add(label_builder, matrixStack, Color.WHITE, 0, 1, 0, label.getMinU(), label.getMaxV(), combinedLight);
			add(label_builder, matrixStack, Color.WHITE, 0, 0, 0, label.getMaxU(), label.getMaxV(), combinedLight);
			add(label_builder, matrixStack, Color.WHITE, 0, 0, 1, label.getMaxU(), label.getMinV(), combinedLight);
			add(label_builder, matrixStack, Color.WHITE, 0, 1, 1, label.getMinU(), label.getMinV(), combinedLight);

			q = new Quaternion(180,180,0,true);
			matrixStack.translate(0, 1, 0);
			matrixStack.rotate(q);
			add(label_builder, matrixStack, Color.WHITE, 0, 1, 0, label.getMinU(), label.getMaxV(), combinedLight);
			add(label_builder, matrixStack, Color.WHITE, 0, 0, 0, label.getMaxU(), label.getMaxV(), combinedLight);
			add(label_builder, matrixStack, Color.WHITE, 0, 0, 1, label.getMaxU(), label.getMinV(), combinedLight);
			add(label_builder, matrixStack, Color.WHITE, 0, 1, 1, label.getMinU(), label.getMinV(), combinedLight);
			matrixStack.rotate(q);
			matrixStack.translate(0, -1, 0);

			q = new Quaternion(-90,0,0,true);
			matrixStack.rotate(q);
			matrixStack.translate(xta,yta,zta);

			scale = 0.8f;
			matrixStack.scale(scale, scale, scale);
			matrixStack.translate(-.002, -.002, -.002);
			add(label_builder, matrixStack, Color.WHITE, 0, 1, 0, aspect.getMinU(), aspect.getMaxV(), combinedLight);
			add(label_builder, matrixStack, Color.WHITE, 0, 0, 0, aspect.getMaxU(), aspect.getMaxV(), combinedLight);
			add(label_builder, matrixStack, Color.WHITE, 0, 0, 1, aspect.getMaxU(), aspect.getMinV(), combinedLight);
			add(label_builder, matrixStack, Color.WHITE, 0, 1, 1, aspect.getMinU(), aspect.getMinV(), combinedLight);

			matrixStack.pop();
		}

		scale = 0.5f;
		if(visScaled > 0){
			matrixStack.push();
			matrixStack.scale(scale, scale, scale);
			matrixStack.translate(.5, .5, .5);

			Color c = new Color(aspectColor.getRGB()-0x80000000);
			
			// top
			add(builder, matrixStack, c, 0, visHeight, 1, spriteTop.getMinU(), spriteTop.getMaxV(), combinedLight);
			add(builder, matrixStack, c, 1, visHeight, 1, spriteTop.getMaxU(), spriteTop.getMaxV(), combinedLight);
			add(builder, matrixStack, c, 1, visHeight, 0, spriteTop.getMaxU(), spriteTop.getMinV(), combinedLight);
			add(builder, matrixStack, c, 0, visHeight, 0, spriteTop.getMinU(), spriteTop.getMinV(), combinedLight);
			
			// bottom
			add(builder, matrixStack, c, 1, visBase, 1, spriteBottom.getMaxU(), spriteBottom.getMaxV(), combinedLight);
			add(builder, matrixStack, c, 0, visBase, 1, spriteBottom.getMinU(), spriteBottom.getMaxV(), combinedLight);
			add(builder, matrixStack, c, 0, visBase, 0, spriteBottom.getMinU(), spriteBottom.getMinV(), combinedLight);
			add(builder, matrixStack, c, 1, visBase, 0, spriteBottom.getMaxU(), spriteBottom.getMinV(), combinedLight);
			
			// east (+X) face
			add(builder, matrixStack, c, 1, visHeight, 0, spriteSide.getMinU(), spriteSide.getMinV(), combinedLight);
			add(builder, matrixStack, c, 1, visHeight, 1, spriteSide.getMaxU(), spriteSide.getMinV(), combinedLight);
			add(builder, matrixStack, c, 1, visBase, 1, spriteSide.getMaxU(), spriteSide.getMaxV(), combinedLight);
			add(builder, matrixStack, c, 1, visBase, 0, spriteSide.getMinU(), spriteSide.getMaxV(), combinedLight);
			
			// west (-X) face
			add(builder, matrixStack, c, 0, visHeight, 0, spriteSide.getMinU(), spriteSide.getMinV(), combinedLight);
			add(builder, matrixStack, c, 0, visBase, 0, spriteSide.getMinU(), spriteSide.getMaxV(), combinedLight);
			add(builder, matrixStack, c, 0, visBase, 1, spriteSide.getMaxU(), spriteSide.getMaxV(), combinedLight);
			add(builder, matrixStack, c, 0, visHeight, 1, spriteSide.getMaxU(), spriteSide.getMinV(), combinedLight);
			
			// north (-Z) face
			add(builder, matrixStack, c, 1, visBase, 0, spriteSide.getMaxU(), spriteSide.getMaxV(), combinedLight);
			add(builder, matrixStack, c, 0, visBase, 0, spriteSide.getMinU(), spriteSide.getMaxV(), combinedLight);
			add(builder, matrixStack, c, 0, visHeight, 0, spriteSide.getMinU(), spriteSide.getMinV(), combinedLight);
			add(builder, matrixStack, c, 1, visHeight, 0, spriteSide.getMaxU(), spriteSide.getMinV(), combinedLight);
			
			// north (+Z) face
			add(builder, matrixStack, c, 0, visBase, 1, spriteSide.getMinU(), spriteSide.getMaxV(), combinedLight);
			add(builder, matrixStack, c, 1, visBase, 1, spriteSide.getMaxU(), spriteSide.getMaxV(), combinedLight);
			add(builder, matrixStack, c, 1, visHeight, 1, spriteSide.getMaxU(), spriteSide.getMinV(), combinedLight);
			add(builder, matrixStack, c, 0, visHeight, 1, spriteSide.getMinU(), spriteSide.getMinV(), combinedLight);

			matrixStack.pop();
		}
	}
}