package net.arcanamod.world;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.DistExecutor;

import java.util.Set;
import java.util.function.Function;

public interface INodeView{
	
	Function<World, INodeView> SIDED_FACTORY = DistExecutor.runForDist(() -> () -> (world) -> world instanceof ClientWorld ? new ClientNodeView((ClientWorld)world) : null, () -> () -> (world) -> world instanceof ServerWorld ? new ServerNodeView((ServerWorld)world) : null);
	
	Set<Node> getAllNodes();
}
