package net.kineticdevelopment.arcana.common.commands;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.core.research.ResearchLoader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Allows for the reloading, granting, removal and completion of research.
 * (Currently only reloading because player's don't have research capabilities yet. Sad.)
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ReloadResearchCommand extends CommandBase{
	
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
			// TODO: entry- player- operation-
		}else
			throw new WrongUsageException("commands.arcanaresearch.usage");
	}
}