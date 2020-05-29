package net.arcanamod.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.arcanamod.items.WandItem;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.Focus;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelTransformComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static net.arcanamod.Arcana.arcLoc;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class WandModelGeometry implements IModelGeometry<WandModelGeometry>{
	
	// hold onto data here
	ResourceLocation cap;
	ResourceLocation material;
	ResourceLocation variant;
	ResourceLocation focus;
	
	Logger LOGGER = LogManager.getLogger();
	
	public WandModelGeometry(ResourceLocation cap, ResourceLocation material, ResourceLocation variant, ResourceLocation focus){
		this.cap = cap;
		this.material = material;
		this.variant = variant;
		this.focus = focus;
	}
	
	public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation){
		IModelTransform transformsFromModel = owner.getCombinedTransform();
		ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transformMap = PerspectiveMapWrapper.getTransforms(new ModelTransformComposition(transformsFromModel, modelTransform));
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		
		// get core texture
		Material coreTex = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, material);
		// get cap texture
		Material capTex = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, cap);
		
		// get variant model
		ResourceLocation coreLoc = arcLoc("item/wands/variants/wand");
		IUnbakedModel coreModel = bakery.getUnbakedModel(coreLoc);
		// get variant cap model
		ResourceLocation capLoc = arcLoc("item/wands/caps/wand");
		IUnbakedModel capModel = bakery.getUnbakedModel(capLoc);
		
		// they *should* be, but jusst checking.
		if(coreModel instanceof BlockModel)
			((BlockModel)coreModel).textures.put("core", Either.left(coreTex));
		else
			LOGGER.error("Core model isn't a block model!");
		if(capModel instanceof BlockModel)
			((BlockModel)capModel).textures.put("cap", Either.left(capTex));
		else
			LOGGER.error("Cap model isn't a block model!");
		
		// get focus model and texture, apply, and add
		Random rand = new Random();
		if(focus != null){
			IBakedModel focusModel = bakery.getBakedModel(new ResourceLocation(focus.getNamespace(), "item/wands/foci/" + focus.getPath()), modelTransform, spriteGetter);
			if(focusModel != null){
				builder.addAll(focusModel.getQuads(null, null, rand));
			}
		}
		
		builder.addAll(coreModel.bakeModel(bakery, spriteGetter, transformsFromModel, coreLoc).getQuads(null, null, rand));
		builder.addAll(capModel.bakeModel(bakery, spriteGetter, transformsFromModel, capLoc).getQuads(null, null, rand));
		
		return new WandBakedModel(builder.build(), spriteGetter.apply(coreTex), Maps.immutableEnumMap(transformMap), new AttachmentOverrideHandler(bakery), true, true, this, owner, modelTransform);
	}
	
	public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors){
		return Collections.emptyList();
	}
	
	protected static final class AttachmentOverrideHandler extends ItemOverrideList{
		
		private final ModelBakery bakery;
		
		public AttachmentOverrideHandler(ModelBakery bakery){
			this.bakery = bakery;
		}
		
		public IBakedModel getModelWithOverrides(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack, @Nullable World world, @Nullable LivingEntity entity){
			// get cap
			Cap cap = WandItem.getCap(stack);
			// get material
			Core core = WandItem.getCore(stack);
			// get variant (staff/scepter/wand)
			// always "wand" currently
			// get focus
			Focus focus = WandItem.getFocus(stack);
			return new WandModelGeometry(cap.getTextureLocation(), core.getTextureLocation(), arcLoc("wand"), focus.getModelLocation()).bake(((WandBakedModel)originalModel).owner, bakery, ModelLoader.defaultTextureGetter(), ((WandBakedModel)originalModel).modelTransform, originalModel.getOverrides(), new ResourceLocation("arcana:does_this_do_anything_lol"));
		}
	}
}