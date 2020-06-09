package net.arcanamod.client.render;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NodeParticleData implements IParticleData{
	
	public static final IParticleData.IDeserializer<NodeParticleData> DESERIALIZER = new IParticleData.IDeserializer<NodeParticleData>() {
		public NodeParticleData deserialize(ParticleType<NodeParticleData> particleType, StringReader reader) throws CommandSyntaxException{
			reader.expect(' ');
			UUID uuid = UUID.fromString(reader.readStringUntil(' '));
			ResourceLocation rloc = new ResourceLocation(reader.getRemaining());
			return new NodeParticleData(uuid, rloc, particleType);
		}
		
		public NodeParticleData read(ParticleType<NodeParticleData> particleType, PacketBuffer buffer) {
			return new NodeParticleData(buffer.readUniqueId(), buffer.readResourceLocation(), particleType);
		}
	};
	
	/*
	 The node is *not* accessed by UUID. The UUID of the node is passed along to allow for
	 different nodes to have different appearances (such as offsetting animations) without
	 changing as other characteristics change (such as the node moving).
	*/
	UUID node;
	ResourceLocation nodeTexture;
	ParticleType<NodeParticleData> type;
	
	public NodeParticleData(UUID node, ResourceLocation nodeTexture, ParticleType<NodeParticleData> type){
		this.node = node;
		this.type = type;
		this.nodeTexture = nodeTexture;
	}
	
	public ParticleType<?> getType(){
		return type;
	}
	
	public void write(PacketBuffer buffer){
		buffer.writeUniqueId(node);
		buffer.writeResourceLocation(nodeTexture);
	}
	
	public String getParameters(){
		return Objects.requireNonNull(ForgeRegistries.PARTICLE_TYPES.getKey(this.getType())).toString() + " " + node.toString() + " " + nodeTexture.toString();
	}
}