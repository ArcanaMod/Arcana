package net.arcanamod.client.render;

import net.arcanamod.entities.BlastEmitterEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class BlastEmitterEntityRenderer extends EntityRenderer<BlastEmitterEntity> {
	public BlastEmitterEntityRenderer(EntityRendererManager manager){
		super(manager);
	}
	
	/**
	 * Returns the location of an entity's texture.
	 */
	@SuppressWarnings("deprecation")
	@Nonnull
	public ResourceLocation getEntityTexture(@Nonnull BlastEmitterEntity entity){
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}