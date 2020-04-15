package net.kineticdevelopment.arcana.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.kineticdevelopment.arcana.client.research.ClientBooks;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.core.research.ResearchBook;
import net.kineticdevelopment.arcana.core.research.ServerBooks;
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
		// TODO: this should probably be made into something somewhat better
		ClientBooks.books = message.books;
		ClientBooks.puzzles = message.puzzles;
		// and for multiplayer, for PuzzleRequirement
		ServerBooks.books = message.books;
		ServerBooks.puzzles = message.puzzles;
		return null;
	}
	
	public static class PktSyncBooks implements IMessage{
		
		Map<ResourceLocation, ResearchBook> books = new LinkedHashMap<>();
		Map<ResourceLocation, Puzzle> puzzles = new LinkedHashMap<>();
		
		public PktSyncBooks(){}
		
		public PktSyncBooks(Map<ResourceLocation, ResearchBook> books, Map<ResourceLocation, Puzzle> puzzles){
			this.books = books;
		}
		
		public void fromBytes(ByteBuf buf){
			// NBTBase # read (DataInput)
			// used with ByteBufInputStream
			ByteBufInputStream stream = new ByteBufInputStream(buf);
			NBTTagCompound nbt = null;
			try{
				nbt = CompressedStreamTools.read(stream, NBTSizeTracker.INFINITE);
			}catch(IOException e){
				LOGGER.error("An error occurred syncing research data with client: could not read packet.");
				e.printStackTrace();
			}
			
			if(nbt != null){
				NBTTagList books = nbt.getTagList("books", 10);
				for(NBTBase bookElement : books){
					NBTTagCompound book = (NBTTagCompound)bookElement;
					// deserialize book
					ResearchBook book1 = ResearchBook.deserialize(book);
					this.books.put(book1.getKey(), book1);
				}
				NBTTagList puzzles = nbt.getTagList("puzzles", 10);
				for(NBTBase puzzleElement : puzzles){
					NBTTagCompound puzzle = (NBTTagCompound)puzzleElement;
					// deserialize book
					Puzzle puzzleObject = Puzzle.deserialize(puzzle);
					if(puzzleObject != null){
						this.puzzles.put(puzzleObject.getKey(), puzzleObject);
					}else LOGGER.error(format("An error occurred syncing research puzzles with client: could not deserialize Puzzle with type \"%s\"; invalid type.", puzzle.getString("type")));
				}
			}
		}
		
		public void toBytes(ByteBuf buf){
			// NBTBase # write (DataOutput)
			// used with ByteBufOutputStream
			ByteBufOutputStream stream = new ByteBufOutputStream(buf);
			NBTTagCompound nbt = new NBTTagCompound();
			NBTTagList books = new NBTTagList(), puzzles = new NBTTagList();
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