package net.kineticdevelopment.arcana.util.handlers;

import net.minecraftforge.common.util.WorldCapabilityData;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.util.Constants.WorldEvents;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class WorldTaintLevelHandler
{
	@SubscribeEvent
	public static void onEvent(WorldEvent.Load event)
	{
		TaintLevelSaveHandler.createTaintLevelFile(event.getWorld());
	}
}
