package net.arcanamod.mixin;

import net.arcanamod.client.PoseHandler;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class BipedModelMixin<T extends LivingEntity> extends BipedModel<T>{
	
	public BipedModelMixin(float modelSize){
		super(modelSize);
	}
	
	@SuppressWarnings("ConstantConditions")
	@Inject(method = "setRotationAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
	        at = @At("TAIL"))
	private void applyPose(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
		// yes I assure you
		// (PlayerModel<?>)((Object)this) is completely necessary
		// and there is no better way
		PoseHandler.applyPose((PlayerModel<?>)((Object)this), entity);
	}
}