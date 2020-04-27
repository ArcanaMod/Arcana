package net.arcanamod.commands;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.spells.FociHelper;
import net.arcanamod.spells.SpellEffectHandler;
import net.arcanamod.spells.effects.ISpellEffect;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandFocus extends CommandBase{
	@Override
	public String getName(){
		return "createfocus";
	}
	
	@Override
	public String getUsage(ICommandSender sender){
		return "createfocus <skin> <power> <core> <name> <effects>";
	}
	
	@Override
	public List<String> getAliases(){
		return new ArrayList<>(Arrays.asList("focus", "forgefocus"));
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
		if(sender instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)sender;
			if(args.length > 5){
				try{
					List<ISpellEffect> effects = new ArrayList<>();
					for(int i = 5; i < args.length; i++){
						ISpellEffect effect = SpellEffectHandler.getEffect(args[i]);
						if(effect != null){
							effects.add(effect);
						}
					}
					player.inventory.addItemStackToInventory(FociHelper.generateFocus(Integer.parseInt(args[0]), effects.toArray(new ISpellEffect[0]), Integer.parseInt(args[1]), Aspect.valueOf(args[2]), args[3]));
					sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Given a new focus!"));
				}catch(NumberFormatException ex){
					sender.sendMessage(new TextComponentString(TextFormatting.RED + "One of the entered numbers is invalid!"));
				}
			}
		}
	}
	
	//Add node before final build
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender){
		return true;
	}
	
	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos){
		return Collections.emptyList();
	}
	
}
