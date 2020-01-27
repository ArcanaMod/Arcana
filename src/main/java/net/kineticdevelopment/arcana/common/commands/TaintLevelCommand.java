package net.kineticdevelopment.arcana.common.commands;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.kineticdevelopment.arcana.utilities.taint.TaintLevelHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

/**
 * Command for changing taint level
 * @author Atlas
 *
 */
public class TaintLevelCommand extends CommandBase {

	@Override
	public String getName() {
		return "taintlevel";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "taintlevel";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		switch (args.length) {
		case 0 : sender.sendMessage(new TextComponentString("Please specify an operation")); break;
		case 1 : if(args[0].contentEquals("raise")) { TaintLevelHandler.increaseTaintLevel(sender.getEntityWorld(), 1); sender.sendMessage(new TextComponentString("Taint Level Raised by one, now "+TaintLevelHandler.getTaintLevel(sender.getEntityWorld()))); }; if(args[0].contentEquals("lower")) { TaintLevelHandler.decreaseTaintLevel(sender.getEntityWorld(), 1); sender.sendMessage(new TextComponentString("Taint Level Raised by one, now "+TaintLevelHandler.getTaintLevel(sender.getEntityWorld())));}; break;
		case 2 : if(args[0].contentEquals("set")) {sender.sendMessage(new TextComponentString("Set taint level to "+args[1])); TaintLevelHandler.setTaintLevel(sender.getEntityWorld(), Integer.parseInt(args[1]));} break;
		default : sender.sendMessage(new TextComponentString("Usage: taintlevel add/lower, taintlevel set <level>"));
		}
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		List<String> zero = Lists.<String>newArrayList();
		zero.add("raise");
		zero.add("lower");
		zero.add("set");
		
        return zero;
    }
}
