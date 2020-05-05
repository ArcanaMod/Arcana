package net.arcanamod.commands;

import com.google.common.collect.Lists;
import net.arcanamod.util.taint.TaintLevelHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Command for changing taint level
 *
 * @author Atlas
 */
public class TaintLevelCommand extends CommandBase{
	
	@Override
	public String getName(){
		return "taintlevel";
	}
	
	@Override
	public String getUsage(ICommandSender sender){
		return "taintlevel";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
		switch(args.length){
			case 0:
				sender.sendMessage(new StringTextComponent("Please specify an operation"));
				break;
			case 1:
				if(args[0].contentEquals("raise")){
					TaintLevelHandler.increaseTaintLevel(sender.getEntityWorld(), 1);
					sender.sendMessage(new StringTextComponent("Taint Level Raised by one, now " + TaintLevelHandler.getTaintLevel(sender.getEntityWorld())));
				}
				if(args[0].contentEquals("lower")){
					TaintLevelHandler.decreaseTaintLevel(sender.getEntityWorld(), 1);
					sender.sendMessage(new StringTextComponent("Taint Level Raised by one, now " + TaintLevelHandler.getTaintLevel(sender.getEntityWorld())));
				}
				break;
			case 2:
				if(args[0].contentEquals("set")){
					sender.sendMessage(new StringTextComponent("Set taint level to " + args[1]));
					TaintLevelHandler.setTaintLevel(sender.getEntityWorld(), Integer.parseInt(args[1]));
				}
				break;
			default:
				sender.sendMessage(new StringTextComponent("Usage: taintlevel add/lower, taintlevel set <level>"));
		}
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos){
		List<String> zero = Lists.newArrayList();
		zero.add("raise");
		zero.add("lower");
		zero.add("set");
		
		return zero;
	}
}
