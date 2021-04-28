package net.arcanamod.client.render.tainted;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.client.model.tainted.TaintedSquidModel;
import net.arcanamod.entities.tainted.TaintedSquidEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.Arcana.arcLoc;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaintedSquidRenderer extends MobRenderer<TaintedSquidEntity, TaintedSquidModel<TaintedSquidEntity>>{
	
	private static final ResourceLocation SQUID_TEXTURES = arcLoc("textures/entity/tainted_squid.png");
	
	public TaintedSquidRenderer(EntityRendererManager renderManager){
		super(renderManager, new TaintedSquidModel<>(), 0.7F);
	}
	
	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(TaintedSquidEntity entity){
		return SQUID_TEXTURES;
	}
	
	protected void applyRotations(TaintedSquidEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks){
		float f = MathHelper.lerp(partialTicks, entityLiving.prevSquidPitch, entityLiving.squidPitch);
		float f1 = MathHelper.lerp(partialTicks, entityLiving.prevSquidYaw, entityLiving.squidYaw);
		matrixStackIn.translate(0.0D, 0.5D, 0.0D);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f));
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));
		matrixStackIn.translate(0.0D, -1.2F, 0.0D);
	}
}