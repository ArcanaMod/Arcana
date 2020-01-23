package net.kineticdevelopment.arcana.common.event;

import net.kineticdevelopment.arcana.utilities.taint.TaintLevelHandler;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Class for handling any events that occur upon world load
 * @author Atlas
 */
@EventBusSubscriber
public class WorldLoadEvent {
	@SubscribeEvent
	public static void onWorldLoad(Load event) {
		TaintLevelHandler.createTaintLevelFile(event.getWorld());
	}
}
