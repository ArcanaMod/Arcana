package net.arcanamod.network;

import net.arcanamod.systems.research.Puzzle;
import net.arcanamod.systems.research.ResearchBook;
import net.arcanamod.systems.research.ResearchBooks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * Syncs all existing research. Not to be confused with {@link PkSyncPlayerResearch}, which syncs the player's progress.
 */
public class PkSyncResearch{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	Map<ResourceLocation, ResearchBook> books;
	Map<ResourceLocation, Puzzle> puzzles;
	
	public PkSyncResearch(Map<ResourceLocation, ResearchBook> books, Map<ResourceLocation, Puzzle> puzzles){
		this.books = books;
		this.puzzles = puzzles;
	}
	
	public static void encode(PkSyncResearch msg, PacketBuffer buffer){
		CompoundNBT nbt = new CompoundNBT();
		ListNBT books = new ListNBT(), puzzles = new ListNBT();
		msg.books.forEach((location, book) -> books.add(book.serialize(location)));
		nbt.put("books", books);
		msg.puzzles.forEach((location, puzzle) -> puzzles.add(puzzle.getPassData()));
		nbt.put("puzzles", puzzles);
		buffer.writeCompoundTag(nbt);
	}
	
	public static PkSyncResearch decode(PacketBuffer buffer){
		CompoundNBT nbt = buffer.readCompoundTag();
		PkSyncResearch msg = new PkSyncResearch(new LinkedHashMap<>(), new LinkedHashMap<>());
		if(nbt != null){
			ListNBT books = nbt.getList("books", 10);
			for(INBT bookElement : books){
				CompoundNBT book = (CompoundNBT)bookElement;
				// deserialize book
				ResearchBook book1 = ResearchBook.deserialize(book);
				msg.books.put(book1.getKey(), book1);
			}
			ListNBT puzzles = nbt.getList("puzzles", 10);
			for(INBT puzzleElement : puzzles){
				CompoundNBT puzzle = (CompoundNBT)puzzleElement;
				// deserialize puzzle
				Puzzle puzzleObject = Puzzle.deserialize(puzzle);
				if(puzzleObject != null)
					msg.puzzles.put(puzzleObject.getKey(), puzzleObject);
				else
					LOGGER.error(format("An error occurred syncing research puzzles with client: could not deserialize Puzzle with type \"%s\"; invalid type.", puzzle.getString("type")));
			}
		}else
			LOGGER.error("An error occurred syncing research data with client: no or null NBT data was received.");
		return msg;
	}
	
	public static void handle(PkSyncResearch msg, Supplier<NetworkEvent.Context> supplier){
		supplier.get().enqueueWork(() -> {
			ResearchBooks.books = msg.books;
			ResearchBooks.puzzles = msg.puzzles;
		});
		supplier.get().setPacketHandled(true);
	}
}