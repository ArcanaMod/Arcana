package net.arcanamod.network;

import net.arcanamod.Arcana;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.Researcher;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class PkSyncPlayerResearch{
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	Map<ResourceLocation, Integer> bookProgress;
	Set<ResourceLocation> puzzleProgress;
	
	public PkSyncPlayerResearch(Map<ResourceLocation, Integer> bookProgress, Set<ResourceLocation> puzzleProgress){
		this.bookProgress = bookProgress;
		this.puzzleProgress = puzzleProgress;
	}
	
	public static void encode(PkSyncPlayerResearch msg, PacketBuffer buffer){
		CompoundNBT compound = new CompoundNBT();
		
		CompoundNBT entries = new CompoundNBT();
		msg.bookProgress.forEach((key, value) -> entries.putInt(key.toString(), value));
		compound.put("entries", entries);
		
		ListNBT puzzles = new ListNBT();
		msg.puzzleProgress.forEach(puzzle -> puzzles.add(StringNBT.valueOf(puzzle.toString())));
		compound.put("puzzles", puzzles);
		
		buffer.writeCompoundTag(compound);
	}
	
	public static PkSyncPlayerResearch decode(PacketBuffer buffer){
		CompoundNBT data = buffer.readCompoundTag();
		
		Map<ResourceLocation, Integer> entryDat = new HashMap<>();
		CompoundNBT entries = data.getCompound("entries");
		for(String s : entries.keySet())
			entryDat.put(new ResourceLocation(s), entries.getInt(s));
		
		Set<ResourceLocation> puzzleDat = new HashSet<>();
		ListNBT puzzles = data.getList("puzzles", Constants.NBT.TAG_STRING);
		for(INBT key : puzzles)
			puzzleDat.add(new ResourceLocation(key.getString()));
		
		return new PkSyncPlayerResearch(entryDat, puzzleDat);
	}
	
	public static void handle(PkSyncPlayerResearch msg, Supplier<NetworkEvent.Context> supplier){
		// from server to client
		supplier.get().enqueueWork(() -> {
			Researcher researcher = Researcher.getFrom(Arcana.proxy.getPlayerOnClient());
			researcher.setEntryData(msg.bookProgress);
			researcher.setPuzzleData(msg.puzzleProgress);
		});
		supplier.get().setPacketHandled(true);
	}
}