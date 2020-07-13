package net.arcanamod.client.render.tainted;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.client.model.KoalaEntityModel;
import net.arcanamod.entities.KoalaEntity;
import net.arcanamod.entities.tainted.TaintedEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TaintedEntityRender<T extends MobEntity, M extends EntityModel<T>> extends MobRenderer<T, EntityModel<T>> {

	public TaintedEntityRender(EntityRendererManager renderManagerIn, M model) {
		super(renderManagerIn, (EntityModel<T>) model, 0.5f);
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return new ResourceLocation(Arcana.MODID,
				"textures/entity/"+entity.getType().getRegistryName().getPath()+".png");
	}
}