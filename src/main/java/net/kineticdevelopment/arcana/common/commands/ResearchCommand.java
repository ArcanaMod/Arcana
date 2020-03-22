package net.kineticdevelopment.arcana.common.commands;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.core.research.ResearchLoader;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.kineticdevelopment.arcana.core.research.ServerBooks;
import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Allows for the reloading, granting, removal and completion of research.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResearchCommand extends CommandBase{
	
	public String getName(){
		return "arcana-research";
	}
	
	public String getUsage(ICommandSender sender){
		return "commands.arcanaresearch.usage";
	}
	
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
		if(args.length == 1 && args[0].equals("reload"))
			ResearchLoader.load();
		else if(args.length == 3){
			ResourceLocation entryKey = new ResourceLocation(args[0]);
			ResearchEntry entry = ServerBooks.getEntry(entryKey);
			if(entry != null){
				EntityPlayerMP player = getPlayer(server, sender, args[1]);
				if(args[2].equalsIgnoreCase("reset")){
					Researcher.getFrom(player).reset(entry);
					Connection.sendReset(entry, player);
				}else if(args[2].equalsIgnoreCase("complete")){
					Researcher.getFrom(player).complete(entry);
					Connection.sendComplete(entry, player);
				}else if(args[2].equalsIgnoreCase("tryAdvance")){
					if(Researcher.canAdvance(Researcher.getFrom(player), entry)){
						Researcher.getFrom(player).advance(entry);
						Connection.sendAdvance(entry, player);
					}
				}else if(args[2].equalsIgnoreCase("forceAdvance")){
					Researcher.getFrom(player).advance(entry);
					Connection.sendAdvance(entry, player);
				}else
					throw new WrongUsageException("commands.arcanaresearch.operations");
			}else
				throw new WrongUsageException("commands.arcanaresearch.invalid_entry", entryKey);
		}else
			throw new WrongUsageException("commands.arcanaresearch.usage");
	}
	
	public boolean isUsernameIndex(String[] args, int index){
		return index == 1 && !args[0].equalsIgnoreCase("reload");
	}
	
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos){
		// if args-length = 1: reload OR entries
		// if args-length = 2 AND we're not reloading: players
		// if args-length = 3 AND we're not reloading: operations
		// can't wait for brigadier to do this all for me :)
		if(args.length == 1){
			List<String> possibilties = getListOfStringsMatchingLastWord(args, ServerBooks.streamEntries().map(ResearchEntry::key).collect(Collectors.toList()));
			possibilties.addAll(getListOfStringsMatchingLastWord(args, "reload"));
			return possibilties;
		}else if(args.length == 2 && !args[0].equalsIgnoreCase("reload")){
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}else if(args.length == 3 && !args[0].equalsIgnoreCase("reload")){
			return getListOfStringsMatchingLastWord(args, "reset", "tryAdvance", "forceAdvance", "complete");
		}
		
		return super.getTabCompletions(server, sender, args, targetPos);
	}
}