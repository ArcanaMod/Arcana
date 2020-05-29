package net.arcanamod.event;

import net.arcanamod.world.NodeView;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handles processing at end of world tick
 *
 * @author Mozaran
 */
public class WorldTickHandler{
	
	public static WorldTickHandler instance = new WorldTickHandler();
	
	
	//public static TIntObjectHashMap<ArrayDeque<ChunkPos>> chunksToGen = new TIntObjectHashMap<>();
	
	/**
	 * @param event
	 * 		- WorldTickEvent Executes every world tick
	 */
	@SubscribeEvent
	public void tickEnd(TickEvent.WorldTickEvent event){
		// probably fixes retrogenning -- a bit late lol
		if(event.side.isClient()){
			return;
		}
		
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
				NodeView view = new NodeView(serverWorld);
				view.getAllNodes().forEach(node -> node.type().tick(serverWorld, view));
			}
		}
	}
}
