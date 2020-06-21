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

/**
 * Handles processing at end of world tick
 *
 * @author Mozaran
 */
@Mod.EventBusSubscriber
public class WorldTickHandler{
	
	//public static TIntObjectHashMap<ArrayDeque<ChunkPos>> chunksToGen = new TIntObjectHashMap<>();
	public static Collection<Consumer<World>> onTick = ConcurrentHashMultiset.create();
	
	/**
	 * @param event
	 * 		- WorldTickEvent Executes every world tick
	 */
	@SubscribeEvent
	public static void tickEnd(TickEvent.WorldTickEvent event){
		// probably fixes retrogenning -- a bit late lol
		
		if(event.phase == TickEvent.Phase.END){
			World world = event.world;
			
			//int dim = world.provider.getDimension();
			//ArrayDeque<ChunkPos> chunks = chunksToGen.get(dim);
			/*if(chunks != null && !chunks.isEmpty()){
				ChunkPos c = chunks.pollFirst();
				long worldSeed = world.getSeed();
				Random rand = new Random(worldSeed);
				long xSeed = rand.nextLong() >> 2 + 1L;
				long zSeed = rand.nextLong() >> 2 + 1L;
				rand.setSeed(xSeed * c.x + zSeed * c.z ^ worldSeed);
				OreGenerator.instance.generateWorld(rand, c.x, c.z, world, false);
				chunksToGen.put(dim, chunks);
			}else if(chunks != null){
				chunksToGen.remove(dim);
			}*/
			
			if(world instanceof ServerWorld){
				ServerWorld serverWorld = (ServerWorld)world;
				AuraView view = new ServerAuraView(serverWorld);
				view.getAllNodes().forEach(node -> node.type().tick(serverWorld, view, node));
			}
			
			if(!onTick.isEmpty()){
				List<Consumer<World>> temp = new ArrayList<>(onTick);
				temp.forEach(consumer -> consumer.accept(world));
				onTick.removeAll(temp);
			}
		}
	}
}