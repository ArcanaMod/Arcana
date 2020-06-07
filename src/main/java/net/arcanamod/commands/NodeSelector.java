package net.arcanamod.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.arcanamod.world.INodeView;
import net.arcanamod.world.Node;
import net.arcanamod.world.ServerNodeView;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.*;
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
	
	public boolean isBoxSelector(){
		return type;
	}
	
	public AxisAlignedBB getAabb(){
		return aabb;
	}
	
	public int getMax(){
		return max;
	}
	
	public Collection<Node> select(CommandSource source){
		if(type){
			// in aabb
			INodeView view = new ServerNodeView(source.getWorld());
			return view.getNodesWithinAABB(aabb);
		}else{
			// nearest to caller
			Vec3d caller = source.getPos();
			// get all nodes in a 300x300 chunk area
			INodeView view = new ServerNodeView(source.getWorld());
			Collection<Node> ranged = new ArrayList<>(view.getNodesWithinAABB(new AxisAlignedBB(caller.x - 150, 0, caller.z - 150, caller.x + 150, 256, caller.z + 150)));
			// sort by distance
			return ranged.stream()
					.sorted(Comparator.comparingDouble(node -> new Vec3d(node.getX(), node.getY(), node.getZ()).distanceTo(caller)))
					.limit(max)
					.collect(Collectors.toList());
		}
	}
}