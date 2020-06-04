package net.arcanamod.client;

import net.arcanamod.network.Connection;
import net.arcanamod.network.PkRequestNodeSync;
import net.arcanamod.world.ClientNodeView;
import net.arcanamod.world.INodeView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class ClientNodeHandler{
	
	public static final List<ChunkPos> clientLoadedChunks = new ArrayList<>();
	public static final Collection<Runnable> untilPlayerJoin = new CopyOnWriteArrayList<>();
	
	// Keep track of loaded chunks on client using events.
	
	@SubscribeEvent
	public static void chunkLoadOnClient(ChunkEvent.Load event){
		// on client
		// send PkRequestClientSync
		untilPlayerJoin.add(() -> Connection.sendToServer(new PkRequestNodeSync(event.getChunk().getPos())));
		clientLoadedChunks.add(event.getChunk().getPos());
	}
	
	@SubscribeEvent
	public static void chunkUnloadOnClient(ChunkEvent.Unload event){
		clientLoadedChunks.remove(event.getChunk().getPos());
	}
	
	// And tick nodes.
	
	@SubscribeEvent
	public static void tickEndClient(TickEvent.ClientTickEvent event){
		if(event.phase == TickEvent.Phase.END){
			ClientWorld world = Minecraft.getInstance().world;
			
			if(world != null){
				INodeView view = new ClientNodeView(world);
				view.getAllNodes().forEach(node -> node.type().tick(world, view, node));
			}
			
			if(!ClientNodeHandler.untilPlayerJoin.isEmpty() && Minecraft.getInstance().player != null){
				List<Runnable> temp = new ArrayList<>(ClientNodeHandler.untilPlayerJoin);
				temp.forEach(Runnable::run);
				ClientNodeHandler.untilPlayerJoin.removeAll(temp);
			}
		}
	}
}
