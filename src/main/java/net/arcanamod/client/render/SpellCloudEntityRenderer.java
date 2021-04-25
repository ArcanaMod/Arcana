package net.arcanamod.client.render;

import net.arcanamod.entities.SpellCloudEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class SpellCloudEntityRenderer extends EntityRenderer<SpellCloudEntity>{
	public SpellCloudEntityRenderer(EntityRendererManager manager){
		super(manager);
	}
	
	/**
	 * Returns the location of an entity's texture.
	 */
	@SuppressWarnings("deprecation")
	@Nonnull
	public ResourceLocation getEntityTexture(@Nonnull SpellCloudEntity entity){
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}