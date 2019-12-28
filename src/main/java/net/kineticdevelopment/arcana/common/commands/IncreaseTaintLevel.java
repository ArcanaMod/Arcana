package net.kineticdevelopment.arcana.common.commands;

import net.kineticdevelopment.arcana.util.handlers.TaintLevelSaveHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class IncreaseTaintLevel extends CommandBase 
{
	@Override
	public String getName() 
	{
		return "increasetaintlevel";
	}

	@Override
	public String getUsage(ICommandSender sender) 
	{
		return "increasetaintlevel";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException 
	{
		int amount = Integer.parseInt(args[0]);
		TaintLevelSaveHandler.increaseTaintLevel(server.getEntityWorld(), amount);
	}
}
