package net.kineticdevelopment.arcana.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

public abstract class StringPacket implements IMessage{
	
	protected String entryKey;
	
	public void fromBytes(ByteBuf buf){
		PacketBuffer pb = new PacketBuffer(buf);
		// no reason to make it so big, but no reason to make it any shorter.
		entryKey = pb.readString(32767 * 4);
	}
	
	public void toBytes(ByteBuf buf){
		PacketBuffer pb = new PacketBuffer(buf);
		pb.writeString(entryKey);
	}
}
