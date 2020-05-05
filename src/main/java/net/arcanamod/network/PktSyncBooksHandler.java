package net.arcanamod.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.ResearchBook;
import net.arcanamod.research.ResearchBooks;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.format;

public class PktSyncBooksHandler implements IMessageHandler<PktSyncBooksHandler.PktSyncBooks, IMessage>{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public IMessage onMessage(PktSyncBooks message, MessageContext ctx){
		// from server -> client
		ResearchBooks.books = message.books;
		ResearchBooks.puzzles = message.puzzles;
		return null;
	}
	
	public static class PktSyncBooks implements IMessage{
		
		Map<ResourceLocation, ResearchBook> books = new LinkedHashMap<>();
		Map<ResourceLocation, Puzzle> puzzles = new LinkedHashMap<>();
		
		public PktSyncBooks(){
		}
		
		public PktSyncBooks(Map<ResourceLocation, ResearchBook> books, Map<ResourceLocation, Puzzle> puzzles){
			this.books = books;
			this.puzzles = puzzles;
		}
		
		public void fromBytes(ByteBuf buf){
			// NBTBase # read (DataInput)
			// used with ByteBufInputStream
			ByteBufInputStream stream = new ByteBufInputStream(buf);
			CompoundNBT nbt = null;
			try{
				nbt = CompressedStreamTools.read(stream, NBTSizeTracker.INFINITE);
			}catch(IOException e){
				LOGGER.error("An error occurred syncing research data with client: could not read packet.");
				e.printStackTrace();
			}
			
			if(nbt != null){
				ListNBT books = nbt.getTagList("books", 10);
				for(NBTBase bookElement : books){
					CompoundNBT book = (CompoundNBT)bookElement;
					// deserialize book
					ResearchBook book1 = ResearchBook.deserialize(book);
					this.books.put(book1.getKey(), book1);
				}
				ListNBT puzzles = nbt.getTagList("puzzles", 10);
				for(NBTBase puzzleElement : puzzles){
					CompoundNBT puzzle = (CompoundNBT)puzzleElement;
					// deserialize puzzle
					Puzzle puzzleObject = Puzzle.deserialize(puzzle);
					if(puzzleObject != null){
						this.puzzles.put(puzzleObject.getKey(), puzzleObject);
					}else
						LOGGER.error(format("An error occurred syncing research puzzles with client: could not deserialize Puzzle with type \"%s\"; invalid type.", puzzle.getString("type")));
				}
			}
		}
		
		public void toBytes(ByteBuf buf){
			// NBTBase # write (DataOutput)
			// used with ByteBufOutputStream
			ByteBufOutputStream stream = new ByteBufOutputStream(buf);
			CompoundNBT nbt = new CompoundNBT();
			ListNBT books = new ListNBT(), puzzles = new ListNBT();
			// add each book
			this.books.forEach((location, book) -> books.appendTag(book.serialize(location)));
			nbt.setTag("books", books);
			// add puzzles
			this.puzzles.forEach((location, puzzle) -> puzzles.appendTag(puzzle.getPassData()));
			nbt.setTag("puzzles", puzzles);
			try{
				CompressedStreamTools.write(nbt, stream);
			}catch(IOException e){
				LOGGER.error("An error occurred syncing research data with client: could not write packet.");
				e.printStackTrace();
			}
		}
	}
}