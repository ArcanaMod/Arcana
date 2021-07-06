package net.arcanamod.client.model.baked;

import com.google.common.collect.ImmutableList;
import net.arcanamod.blocks.tiles.WardenedBlockTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class WardenedBlockBakedModel implements IBakedModel {
	public WardenedBlockBakedModel(IBakedModel unCamouflagedModel)
	{
		modelWhenNotCamouflaged = unCamouflagedModel;
	}

	public static ModelProperty<Optional<BlockState>> COPIED_BLOCK = new ModelProperty<>();
	public static ModelProperty<Boolean> HOLDING_SPELL = new ModelProperty<>();

	public static ModelDataMap getEmptyIModelData() {
		ModelDataMap.Builder builder = new ModelDataMap.Builder();
		builder.withInitial(COPIED_BLOCK, Optional.empty());
		builder.withInitial(HOLDING_SPELL, false);
		ModelDataMap modelDataMap = builder.build();
		return modelDataMap;
	}

	private void putVertex(BakedQuadBuilder builder, Vector3d normal,
						   double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b) {

		ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();
		for (int j = 0 ; j < elements.size() ; j++) {
			VertexFormatElement e = elements.get(j);
			switch (e.getUsage()) {
				case POSITION:
					builder.put(j, (float) x, (float) y, (float) z, 1.0f);
					break;
				case COLOR:
					builder.put(j, r, g, b, 1.0f);
					break;
				case UV:
					switch (e.getIndex()) {
						case 0:
							float iu = sprite.getInterpolatedU(u);
							float iv = sprite.getInterpolatedV(v);
							builder.put(j, iu, iv);
							break;
						case 2:
							builder.put(j, (short) 0, (short) 0);
							break;
						default:
							builder.put(j);
							break;
					}
					break;
				case NORMAL:
					builder.put(j, (float) normal.x, (float) normal.y, (float) normal.z);
					break;
				default:
					builder.put(j);
					break;
			}
		}
	}

	private BakedQuad createQuad(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d v4, TextureAtlasSprite sprite) {
		Vector3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
		int tw = sprite.getWidth();
		int th = sprite.getHeight();

		BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
		builder.setQuadOrientation(Direction.getFacingFromVector(normal.x, normal.y, normal.z));
		putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(builder, normal, v2.x, v2.y, v2.z, 0, th, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(builder, normal, v3.x, v3.y, v3.z, tw, th, sprite, 1.0f, 1.0f, 1.0f);
		putVertex(builder, normal, v4.x, v4.y, v4.z, tw, 0, sprite, 1.0f, 1.0f, 1.0f);
		return builder.build();
	}

	private static Vector3d v(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	/**
	 * Forge's extension in place of IBakedModel::getQuads
	 * It allows us to pass in some extra information which we can use to choose the appropriate quads to render
	 * @param state
	 * @param side
	 * @param rand
	 * @param extraData
	 * @return
	 */
	@Override
	@Nonnull
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData)
	{
		double l = -.001;
		double r = 1+.001;
		ArrayList<BakedQuad> ql = (ArrayList<BakedQuad>) (Object) (Object) ((ArrayList<BakedQuad>) getActualBakedModelFromIModelData(extraData).getQuads(state, side, rand)).clone(); // Casting object to object removes crashes when spell used on some blocks. but why?
		if (extraData.hasProperty(HOLDING_SPELL)&& extraData.getData(HOLDING_SPELL)) {
			ql.add(createQuad(v(l, r, l), v(l, r, r), v(r, r, r), v(r, r, l), modelWhenNotCamouflaged.getParticleTexture()));
			ql.add(createQuad(v(l, l, l), v(r, l, l), v(r, l, r), v(l, l, r), modelWhenNotCamouflaged.getParticleTexture()));
			ql.add(createQuad(v(r, r, r), v(r, l, r), v(r, l, l), v(r, r, l), modelWhenNotCamouflaged.getParticleTexture()));
			ql.add(createQuad(v(l, r, l), v(l, l, l), v(l, l, r), v(l, r, r), modelWhenNotCamouflaged.getParticleTexture()));
			ql.add(createQuad(v(r, r, l), v(r, l, l), v(l, l, l), v(l, r, l), modelWhenNotCamouflaged.getParticleTexture()));
			ql.add(createQuad(v(l, r, r), v(l, l, r), v(r, l, r), v(r, r, r), modelWhenNotCamouflaged.getParticleTexture()));
		}
		return ql;
	}

	@Override
	@Nonnull
	public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData)
	{
		Optional<BlockState> bestAdjacentBlock = ((WardenedBlockTileEntity)world.getTileEntity(pos)).getState();
		ModelDataMap modelDataMap = getEmptyIModelData();
		modelDataMap.setData(COPIED_BLOCK, bestAdjacentBlock);
		modelDataMap.setData(HOLDING_SPELL, ((WardenedBlockTileEntity)world.getTileEntity(pos)).isHoldingWand());
		return modelDataMap;
	}

	@Override
	public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data)
	{
		return getActualBakedModelFromIModelData(data).getParticleTexture();
	}

	private IBakedModel getActualBakedModelFromIModelData(@Nonnull IModelData data) {
		IBakedModel retval = modelWhenNotCamouflaged;  // default
		if (!data.hasProperty(COPIED_BLOCK)) {
			if (!loggedError) {
				LOGGER.error("IModelData did not have expected property COPIED_BLOCK");
				loggedError = true;
			}
			return retval;
		}
		Optional<BlockState> copiedBlock = data.getData(COPIED_BLOCK);
		if (!copiedBlock.isPresent()) return retval;

		Minecraft mc = Minecraft.getInstance();
		BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
		retval = blockRendererDispatcher.getModelForState(copiedBlock.get());
		return retval.getBakedModel();
	}

	private IBakedModel modelWhenNotCamouflaged;
	private Boolean hasWand;


	// ---- All these methods are required by the interface but we don't do anything special with them.

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
		throw new AssertionError("IBakedModel::getQuads should never be called, only IForgeBakedModel::getQuads");
	}

	// getTexture is used directly when player is inside the block. The game will crash if you don't use something
	//   meaningful here.
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return modelWhenNotCamouflaged.getParticleTexture();
	}


	// ideally, this should be changed for different blocks being camouflaged, but this is not supported by vanilla or forge
	@Override
	public boolean isAmbientOcclusion()
	{
		return modelWhenNotCamouflaged.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return modelWhenNotCamouflaged.isGui3d();
	}
	
	public boolean isSideLit(){
		return modelWhenNotCamouflaged.isSideLit();
	}
	
	@Override
	public boolean isBuiltInRenderer()
	{
		return modelWhenNotCamouflaged.isBuiltInRenderer();
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return modelWhenNotCamouflaged.getOverrides();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return modelWhenNotCamouflaged.getItemCameraTransforms();
	}

	private static final Logger LOGGER = LogManager.getLogger();
	private static boolean loggedError = false; // prevent spamming console
}