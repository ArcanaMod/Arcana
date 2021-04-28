package net.arcanamod.commands;

import net.arcanamod.world.AuraView;
import net.arcanamod.world.Node;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class NodeSelector{
	
	/** false = nearest, true = in */
	private final boolean type;
	private final AxisAlignedBB aabb;
	private final int max;
	
	public NodeSelector(boolean type, AxisAlignedBB aabb, int max){
		this.type = type;
		this.aabb = aabb;
		this.max = max;
	}
	
	public int getMax(){
		return max;
	}
	
	public Collection<Node> select(CommandSource source){
		if(type){
			// in aabb
			AuraView view = new ServerAuraView(source.getWorld());
			return view.getNodesWithinAABB(aabb);
		}else{
			// nearest to caller
			Vector3d caller = source.getPos();
			// get all nodes in a 300x300 chunk area
			AuraView view = new ServerAuraView(source.getWorld());
			Collection<Node> ranged = new ArrayList<>(view.getNodesWithinAABB(new AxisAlignedBB(caller.x - 150, 0, caller.z - 150, caller.x + 150, 256, caller.z + 150)));
			// sort by distance
			return ranged.stream()
					.sorted(Comparator.comparingDouble(node -> new Vector3d(node.getX(), node.getY(), node.getZ()).distanceTo(caller)))
					.limit(max)
					.collect(Collectors.toList());
		}
	}
}