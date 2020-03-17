package net.kineticdevelopment.arcana.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

public abstract class StringPacket implements IMessage{
	
	String entryKey;
	
	public void fromBytes(ByteBuf buf){
		// note: here I use buf.readableBytes() because only a string is encoded. If I wanted to store other data,
		// I should handle this more appropriately.
		// 1.14's PacketBuffer will handle this for me.
		entryKey = buf.readCharSequence(buf.readableBytes(), StandardCharsets.UTF_8).toString();
	}
	
	public void toBytes(ByteBuf buf){
		buf.writeCharSequence(entryKey, StandardCharsets.UTF_8);
	}
}
