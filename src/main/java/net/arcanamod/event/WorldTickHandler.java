package net.arcanamod.event;

import com.google.common.collect.ConcurrentHashMultiset;
import net.arcanamod.world.AuraView;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class WorldTickHandler{
	
	public static Collection<Consumer<World>> onTick = ConcurrentHashMultiset.create();
	
	@SubscribeEvent
	public static void tickEnd(TickEvent.WorldTickEvent event){
		if(event.phase == TickEvent.Phase.END){
			World world = event.world;
			
			if(world instanceof ServerWorld){
				ServerWorld serverWorld = (ServerWorld)world;
				AuraView view = new ServerAuraView(serverWorld);
				view.getAllNodes().forEach(node -> node.type().tick(serverWorld, view, node));
				if(event.world.getGameTime() % 6 == 0)
					view.tickTaintLevel();
			}
			
			if(!onTick.isEmpty()){
				List<Consumer<World>> temp = new ArrayList<>(onTick);
				temp.forEach(consumer -> consumer.accept(world));
				onTick.removeAll(temp);
			}
		}
	}
}