package net.arcanamod.event;

import net.arcanamod.world.ClientNodeView;
import net.arcanamod.world.INodeView;
import net.arcanamod.world.ServerNodeView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Handles processing at end of world tick
 *
 * @author Mozaran
 */
public class WorldTickHandler{
	
	public static WorldTickHandler instance = new WorldTickHandler();
	public static Collection<Runnable> untilPlayerJoin = new CopyOnWriteArrayList<>();
	//public static TIntObjectHashMap<ArrayDeque<ChunkPos>> chunksToGen = new TIntObjectHashMap<>();
	
	/**
	 * @param event
	 * 		- WorldTickEvent Executes every world tick
	 */
	@SubscribeEvent
	public void tickEnd(TickEvent.WorldTickEvent event){
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
				INodeView view = new ServerNodeView(serverWorld);
				view.getAllNodes().forEach(node -> node.type().tick(serverWorld, view, node));
			}
		}
	}
	
	// TODO: MOVE TO CLIENTSIDE
	
	@SubscribeEvent
	public void tickEndClient(TickEvent.ClientTickEvent event){
		if(event.phase == TickEvent.Phase.END){
			ClientWorld world = Minecraft.getInstance().world;
			
			if(world != null){
				INodeView view = new ClientNodeView(world);
				view.getAllNodes().forEach(node -> node.type().tick(world, view, node));
			}
			
			if(!untilPlayerJoin.isEmpty() && Minecraft.getInstance().player != null){
				List<Runnable> temp = new ArrayList<>(untilPlayerJoin);
				temp.forEach(Runnable::run);
				untilPlayerJoin.removeAll(temp);
			}
		}
	}
}