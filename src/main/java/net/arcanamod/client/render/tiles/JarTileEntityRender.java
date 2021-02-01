package net.arcanamod.client.render.tiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.ArcanaConfig;
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
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JarTileEntityRender extends TileEntityRenderer<JarTileEntity>{
	public static final ResourceLocation JAR_CONTENT_SIDE = new ResourceLocation(Arcana.MODID, "models/parts/fluid_side");
	public static final ResourceLocation JAR_CONTENT_TOP = new ResourceLocation(Arcana.MODID, "models/parts/fluid_top");
	public static final ResourceLocation JAR_CONTENT_BOTTOM = new ResourceLocation(Arcana.MODID, "models/parts/fluid_bottom");
	public static final ResourceLocation JAR_LABEL = new ResourceLocation(Arcana.MODID, "models/parts/jar_label");
	
	public JarTileEntityRender(TileEntityRendererDispatcher p_i226006_1_){
		super(p_i226006_1_);
	}
	
	private void add(IVertexBuilder renderer, MatrixStack stack, Color color, float x, float y, float z, float u, float v){
		renderer.pos(stack.getLast().getMatrix(), x, y, z)
				.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f)
				.tex(u, v)
				.lightmap(0, 240)
				.normal(1, 0, 0)
				.endVertex();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void render(JarTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay){
		
		TextureAtlasSprite spriteSide = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(JAR_CONTENT_SIDE);
		TextureAtlasSprite spriteTop = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(JAR_CONTENT_TOP);
		TextureAtlasSprite label = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(JAR_LABEL);
		TextureAtlasSprite aspect = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(tileEntity.getPaperAspectLocation());
		IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());
		
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
		matrixStack.scale(scale, scale, scale);
		Quaternion q = new Quaternion(-25,0,0,true);
		matrixStack.rotate(q);

		matrixStack.translate(.445f, .1f, 1.2f);

		add(builder, matrixStack, Color.WHITE, 0, 1, 0, label.getMinU(), label.getMaxV());
		add(builder, matrixStack, Color.WHITE, 0, 0, 0, label.getMaxU(), label.getMaxV());
		add(builder, matrixStack, Color.WHITE, 0, 0, 1, label.getMaxU(), label.getMinV());
		add(builder, matrixStack, Color.WHITE, 0, 1, 1, label.getMinU(), label.getMinV());

		matrixStack.translate(-.02, -.02, -.02);
		add(builder, matrixStack, Color.WHITE, 0, 1, 0, aspect.getMinU(), aspect.getMaxV());
		add(builder, matrixStack, Color.WHITE, 0, 0, 0, aspect.getMaxU(), aspect.getMaxV());
		add(builder, matrixStack, Color.WHITE, 0, 0, 1, aspect.getMaxU(), aspect.getMinV());
		add(builder, matrixStack, Color.WHITE, 0, 1, 1, aspect.getMinU(), aspect.getMinV());
		matrixStack.pop();
		}

		scale = 0.5f;
		if(visScaled > 0){
			matrixStack.push();
			matrixStack.scale(scale, scale, scale);
			matrixStack.translate(.5, .5, .5);
			
			// top
			add(builder, matrixStack, aspectColor, 0, visHeight, 1, spriteTop.getMinU(), spriteTop.getMaxV());
			add(builder, matrixStack, aspectColor, 1, visHeight, 1, spriteTop.getMaxU(), spriteTop.getMaxV());
			add(builder, matrixStack, aspectColor, 1, visHeight, 0, spriteTop.getMaxU(), spriteTop.getMinV());
			add(builder, matrixStack, aspectColor, 0, visHeight, 0, spriteTop.getMinU(), spriteTop.getMinV());
			
			// bottom
			add(builder, matrixStack, aspectColor, 1, visBase, 1, spriteTop.getMaxU(), spriteTop.getMaxV());
			add(builder, matrixStack, aspectColor, 0, visBase, 1, spriteTop.getMinU(), spriteTop.getMaxV());
			add(builder, matrixStack, aspectColor, 0, visBase, 0, spriteTop.getMinU(), spriteTop.getMinV());
			add(builder, matrixStack, aspectColor, 1, visBase, 0, spriteTop.getMaxU(), spriteTop.getMinV());
			
			// east (+X) face
			add(builder, matrixStack, aspectColor, 1, visHeight, 0, spriteSide.getMinU(), spriteSide.getMinV());
			add(builder, matrixStack, aspectColor, 1, visHeight, 1, spriteSide.getMaxU(), spriteSide.getMinV());
			add(builder, matrixStack, aspectColor, 1, visBase, 1, spriteSide.getMaxU(), spriteSide.getMaxV());
			add(builder, matrixStack, aspectColor, 1, visBase, 0, spriteSide.getMinU(), spriteSide.getMaxV());
			
			// west (-X) face
			add(builder, matrixStack, aspectColor, 0, visHeight, 0, spriteSide.getMinU(), spriteSide.getMinV());
			add(builder, matrixStack, aspectColor, 0, visBase, 0, spriteSide.getMinU(), spriteSide.getMaxV());
			add(builder, matrixStack, aspectColor, 0, visBase, 1, spriteSide.getMaxU(), spriteSide.getMaxV());
			add(builder, matrixStack, aspectColor, 0, visHeight, 1, spriteSide.getMaxU(), spriteSide.getMinV());
			
			// north (-Z) face
			add(builder, matrixStack, aspectColor, 1, visBase, 0, spriteSide.getMaxU(), spriteSide.getMaxV());
			add(builder, matrixStack, aspectColor, 0, visBase, 0, spriteSide.getMinU(), spriteSide.getMaxV());
			add(builder, matrixStack, aspectColor, 0, visHeight, 0, spriteSide.getMinU(), spriteSide.getMinV());
			add(builder, matrixStack, aspectColor, 1, visHeight, 0, spriteSide.getMaxU(), spriteSide.getMinV());
			
			// north (+Z) face
			add(builder, matrixStack, aspectColor, 0, visBase, 1, spriteSide.getMinU(), spriteSide.getMaxV());
			add(builder, matrixStack, aspectColor, 1, visBase, 1, spriteSide.getMaxU(), spriteSide.getMaxV());
			add(builder, matrixStack, aspectColor, 1, visHeight, 1, spriteSide.getMaxU(), spriteSide.getMinV());
			add(builder, matrixStack, aspectColor, 0, visHeight, 1, spriteSide.getMinU(), spriteSide.getMinV());
			
			matrixStack.pop();
		}
	}
}