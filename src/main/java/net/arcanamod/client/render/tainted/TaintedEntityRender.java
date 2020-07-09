package net.arcanamod.client.render.tainted;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.client.model.KoalaEntityModel;
import net.arcanamod.client.model.tainted.TaintedCowEntityModel;
import net.arcanamod.entities.KoalaEntity;
import net.arcanamod.entities.tainted.TaintedEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TaintedEntityRender extends MobRenderer<TaintedEntity, TaintedCowEntityModel<TaintedEntity>> {
	protected static final ResourceLocation TEXTURE = new ResourceLocation(Arcana.MODID,
			"textures/entity/tainted_cow.png");

	public TaintedEntityRender(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new TaintedCowEntityModel<TaintedEntity>(), 0.5f);
	}

	@Override
	public ResourceLocation getEntityTexture(TaintedEntity entity) {
		return TEXTURE;
	}
}