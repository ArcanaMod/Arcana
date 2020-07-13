package net.arcanamod.event;

import net.arcanamod.fluids.ArcanaFluids;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityUpdateHandler {

	@SubscribeEvent
	public static void EntityUpdate(LivingEvent.LivingUpdateEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		boolean disabled = false;
		if (entity instanceof PlayerEntity)
		{
			if (((PlayerEntity)entity).isSpectator())
				disabled = true;
		}
		if (!disabled)
		{
			//Change this to what ever you want (swimming in taint :O, Taint pushes up (This code), etc.)
			BlockPos pos = entity.getPosition();
			Block b = entity.getEntityWorld().getBlockState(pos.offset(Direction.UP)).getBlock();
			Block b1 = entity.getEntityWorld().getBlockState(pos).getBlock();
			if (b.equals(ArcanaFluids.TAINT_FLUID_BLOCK.get()))
			{
				entity.setMotion(new Vec3d(entity.getMotion().x, 0.072600011620D+entity.getMotion().y, entity.getMotion().z));
			} else if (b1.equals(ArcanaFluids.TAINT_FLUID_BLOCK.get()))
			{
				entity.setMotion(new Vec3d(entity.getMotion().x, 0.072600011620D+entity.getMotion().y, entity.getMotion().z));
			}
		}
	}
}
