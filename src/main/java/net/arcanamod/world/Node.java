package net.arcanamod.world;

import net.arcanamod.aspects.AspectBattery;
import net.arcanamod.aspects.IAspectHandler;
import net.minecraft.dispenser.IPosition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.UUID;

// implements position for BlockPos constructor convenience
public class Node implements IPosition{
	
	/** The aspects contained in the node. */
	IAspectHandler aspects;
	/** The type of node this is. */
	NodeType type;
	/** The position of this node. */
	double x, y, z;
	/** The unique ID of this node. */
	UUID nodeUniqueId = MathHelper.getRandomUUID();
	/** The time, in ticks, until the node gains some aspects. */
	int timeUntilRecharge;
	/** Any extra data, used by the node type (e.g. hungry nodes). */
	CompoundNBT data = new CompoundNBT();
	
	public Node(IAspectHandler aspects, NodeType type, double x, double y, double z, int timeUntilRecharge){
		this.aspects = aspects;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.timeUntilRecharge = timeUntilRecharge;
	}
	
	public Node(IAspectHandler aspects, NodeType type, double x, double y, double z, int timeUntilRecharge, CompoundNBT data){
		this.aspects = aspects;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.timeUntilRecharge = timeUntilRecharge;
		this.data = data;
	}
	
	public Node(IAspectHandler aspects, NodeType type, double x, double y, double z, int timeUntilRecharge, UUID nodeUniqueId, CompoundNBT data){
		this.aspects = aspects;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.timeUntilRecharge = timeUntilRecharge;
		this.nodeUniqueId = nodeUniqueId;
		this.data = data;
	}
	
	public NodeType type(){
		return type;
	}
	
	public CompoundNBT serializeNBT(){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("type", NodeType.TYPES.inverse().get(type()).toString());
		nbt.put("aspects", aspects.serializeNBT());
		nbt.putDouble("x", getX());
		nbt.putDouble("y", getY());
		nbt.putDouble("z", getZ());
		nbt.putUniqueId("nodeUniqueId", nodeUniqueId);
		nbt.putInt("timeUntilRecharge", timeUntilRecharge);
		nbt.put("data", data);
		return nbt;
	}
	
	public static Node fromNBT(CompoundNBT nbt){
		IAspectHandler aspects = new AspectBattery();
		aspects.deserializeNBT(nbt.getCompound("aspects"));
		NodeType type = NodeType.TYPES.get(new ResourceLocation(nbt.getString("type")));
		double x = nbt.getDouble("x"), y = nbt.getDouble("y"), z = nbt.getDouble("z");
		int timeUntilRecharge = nbt.getInt("timeUntilRecharge");
		CompoundNBT data = nbt.getCompound("data");
		return nbt.hasUniqueId("nodeUniqueId") ? new Node(aspects, type, x, y, z, timeUntilRecharge, nbt.getUniqueId("nodeUniqueId"), data) : new Node(aspects, type, x, y, z, timeUntilRecharge, data);
	}
	
	public Vector3d getPosition(){
		return new Vector3d(x, y, z);
	}
	
	public IAspectHandler getAspects(){
		return aspects;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getZ(){
		return z;
	}
	
	public int getTimeUntilRecharge(){
		return timeUntilRecharge;
	}
	
	public void setType(NodeType type){
		this.type = type;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public void setZ(double z){
		this.z = z;
	}
	
	public void setTimeUntilRecharge(int timeUntilRecharge){
		this.timeUntilRecharge = timeUntilRecharge;
	}
	
	public UUID nodeUniqueId(){
		return nodeUniqueId;
	}
	
	public CompoundNBT getData(){
		return data;
	}
	
	public String toString(){
		return "Node{" +
				"aspects=" + aspects +
				", type=" + type +
				", x=" + x +
				", y=" + y +
				", z=" + z +
				", nodeUniqueId=" + nodeUniqueId +
				", timeUntilRecharge=" + timeUntilRecharge +
				", data=" + data +
				'}';
	}
}