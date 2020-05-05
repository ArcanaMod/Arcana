package net.arcanamod.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.arcanamod.Arcana;
import net.arcanamod.research.Researcher;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

// 1.14/15: should be removable I think, deserialisation seems to occur on clients
public class PktSyncClientResearchHandler implements IMessageHandler<PktSyncClientResearchHandler.PktSyncClientResearch, IMessage>{
	
	public IMessage onMessage(PktSyncClientResearch message, MessageContext ctx){
		// from server -> client
		// 1.14/15: almost certainly remove, seems like a bug that its required anyways
		// wait for player to be nonnull
		if(Arcana.proxy.getPlayerOnClient() != null)
			Researcher.getFrom(Arcana.proxy.getPlayerOnClient()).setEntryData(message.data);
		else{
			Runnable tryDo = new Runnable(){
				public void run(){
					if(Arcana.proxy.getPlayerOnClient() != null){
						Researcher researcher = Researcher.getFrom(Arcana.proxy.getPlayerOnClient());
						researcher.setEntryData(message.data);
						researcher.setPuzzleData(message.puzzleData);
					}else
						Arcana.proxy.scheduleOnClient(this); // cant use lambdas because of scoping
				}
			};
			// effectively looping until its nonnull
			Arcana.proxy.scheduleOnClient(tryDo);
		}
		return null;
	}
	
	public static class PktSyncClientResearch implements IMessage{
		
		Map<ResourceLocation, Integer> data = new LinkedHashMap<>();
		Set<ResourceLocation> puzzleData = new HashSet<>();
		
		public PktSyncClientResearch(){
		}
		
		public PktSyncClientResearch(Map<ResourceLocation, Integer> data, Set<ResourceLocation> puzzleData){
			this.data = data;
			this.puzzleData = puzzleData;
		}
		
		public void fromBytes(ByteBuf buf){
			// NBTBase # read (DataInput)
			// used with ByteBufInputStream
			ByteBufInputStream stream = new ByteBufInputStream(buf);
			CompoundNBT nbt = null;
			try{
				nbt = CompressedStreamTools.read(stream, NBTSizeTracker.INFINITE);
			}catch(IOException e){
				e.printStackTrace();
			}
			
			if(nbt != null){
				CompoundNBT researches = (CompoundNBT)nbt.getTag("researches");
				for(String key : researches.getKeySet())
					data.put(new ResourceLocation(key), researches.getInteger(key));
				
				ListNBT puzzles = nbt.getTagList("puzzles", Constants.NBT.TAG_STRING);
				for(NBTBase key : puzzles)
					puzzleData.add(new ResourceLocation(((StringNBT)key).getString()));
			}
		}
		
		public void toBytes(ByteBuf buf){
			// NBTBase # write (DataOutput)
			// used with ByteBufOutputStream
			ByteBufOutputStream stream = new ByteBufOutputStream(buf);
			CompoundNBT nbt = new CompoundNBT();
			CompoundNBT researches = new CompoundNBT();
			// add each book
			data.forEach((entry, stage) -> researches.setInteger(entry.toString(), stage));
			nbt.setTag("researches", researches);
			
			ListNBT puzzles = new ListNBT();
			puzzleData.forEach(puzzle -> puzzles.appendTag(new StringNBT(puzzle.toString())));
			nbt.setTag("puzzles", puzzles);
			try{
				CompressedStreamTools.write(nbt, stream);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}