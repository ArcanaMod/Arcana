package net.kineticdevelopment.arcana.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.kineticdevelopment.arcana.client.research.ClientBooks;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

// 1.14/15: should be removable I think, deserialisation seems to occur on clients
public class PktSyncClientResearchHandler implements IMessageHandler<PktSyncClientResearchHandler.PktSyncClientResearch, IMessage>{
	
	public IMessage onMessage(PktSyncClientResearch message, MessageContext ctx){
		// from server -> client
		// 1.14/15: almost certainly removal, seems like a bug that its required anyways
		// wait for player to be nonnull
		if(Minecraft.getMinecraft().player != null)
			Researcher.getFrom(Minecraft.getMinecraft().player).setData(message.data);
		else{
			Runnable tryDo = new Runnable(){
				public void run(){
					if(Minecraft.getMinecraft().player != null)
						Researcher.getFrom(Minecraft.getMinecraft().player).setData(message.data);
					else
						Minecraft.getMinecraft().addScheduledTask(this); // cant use lambdas because of scoping
				}
			};
			// effectively looping until its nonnul
			Minecraft.getMinecraft().addScheduledTask(tryDo);
		}
		return null;
	}
	
	public static class PktSyncClientResearch implements IMessage{
		
		Map<ResourceLocation, Integer> data = new LinkedHashMap<>();
		
		public PktSyncClientResearch(){}
		
		public PktSyncClientResearch(Map<ResourceLocation, Integer> data){
			this.data = data;
		}
		
		public void fromBytes(ByteBuf buf){
			// NBTBase # read (DataInput)
			// used with ByteBufInputStream
			ByteBufInputStream stream = new ByteBufInputStream(buf);
			NBTTagCompound nbt = null;
			try{
				nbt = CompressedStreamTools.read(stream, NBTSizeTracker.INFINITE);
			}catch(IOException e){
				e.printStackTrace();
			}
			
			if(nbt != null){
				NBTTagCompound researches = (NBTTagCompound)nbt.getTag("researches");
				for(String key : researches.getKeySet())
					data.put(new ResourceLocation(key), researches.getInteger(key));
			}
		}
		
		public void toBytes(ByteBuf buf){
			// NBTBase # write (DataOutput)
			// used with ByteBufOutputStream
			ByteBufOutputStream stream = new ByteBufOutputStream(buf);
			NBTTagCompound nbt = new NBTTagCompound();
			NBTTagCompound researches = new NBTTagCompound();
			// add each book
			data.forEach((entry, stage) -> researches.setInteger(entry.toString(), stage));
			nbt.setTag("researches", researches);
			try{
				CompressedStreamTools.write(nbt, stream);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}