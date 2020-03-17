package net.kineticdevelopment.arcana.common.commands;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.core.research.ResearchEntry;
import net.kineticdevelopment.arcana.core.research.ResearchLoader;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.kineticdevelopment.arcana.core.research.ServerBooks;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

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
					if(Researcher.canAdvance(Researcher.getFrom(player), entry, player)){
						Researcher.getFrom(player).advance(entry);
						Connection.sendAdvance(entry, player);
					}
				}else if(args[2].equalsIgnoreCase("forceAdvance")){
					Researcher.getFrom(player).advance(entry);
					Connection.sendAdvance(entry, player);
				}else
					// document these somewhere that doesn't require an error
					throw new WrongUsageException("commands.arcanaresearch.operations");
			}else
				throw new WrongUsageException("commands.arcanaresearch.invalid_entry", entryKey);
		}else
			throw new WrongUsageException("commands.arcanaresearch.usage");
	}
}