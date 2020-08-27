package net.arcanamod.client.render.aspects;

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