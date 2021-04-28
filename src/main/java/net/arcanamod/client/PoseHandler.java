package net.arcanamod.client;

import net.arcanamod.items.WandItem;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;

public final class PoseHandler{
	
	public static void applyPose(PlayerModel<?> model, Entity entity){
		if(entity instanceof PlayerEntity){
			PlayerEntity player = (PlayerEntity)entity;
			// If we're using a wand,
			if(player.getActiveItemStack().getItem() instanceof WandItem){
				// point where we're facing.
				// TODO: better draining pose
				ModelRenderer arm = player.getActiveHand() == Hand.MAIN_HAND ? model.bipedRightArm : model.bipedLeftArm;
				Vector3d facing = player.getLookVec();
				arm.rotateAngleX = -((float)facing.y) - 1.5f;
			}
		}
	}
}