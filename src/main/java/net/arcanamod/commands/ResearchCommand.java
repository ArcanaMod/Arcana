package net.arcanamod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkModifyResearch;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.ResearchEntry;
import net.arcanamod.capabilities.Researcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;
import static net.minecraft.command.arguments.ResourceLocationArgument.resourceLocation;

public class ResearchCommand{
	
	private static final SuggestionProvider<CommandSource> SUGGEST_RESEARCH = (ctx, builder) -> ISuggestionProvider.func_212476_a(ResearchBooks.streamEntries().map(ResearchEntry::key), builder);
	
	public static void register(CommandDispatcher<CommandSource> dispatcher){
		dispatcher.register(
				literal("arcana-research").requires(source -> source.hasPermissionLevel(2))
				.then(argument("targets", EntityArgument.players())
						.then(literal("complete-all").executes(ResearchCommand::giveAll))
						.then(literal("reset-all").executes(ResearchCommand::resetAll))
						.then(literal("complete").then(argument("research", resourceLocation()).executes(context -> modify(context, Diff.complete)).suggests(SUGGEST_RESEARCH)))
						.then(literal("try-advance").then(argument("research", resourceLocation()).executes(context -> modify(context, Diff.advance)).suggests(SUGGEST_RESEARCH)))
						.then(literal("force-advance").then(argument("research", resourceLocation()).executes(context -> modify(context, Diff.forceAdvance)).suggests(SUGGEST_RESEARCH)))
						.then(literal("reset").then(argument("research", resourceLocation()).executes(context -> modify(context, Diff.reset)).suggests(SUGGEST_RESEARCH)))
				)
				// arcana-research reload would go here
		);
		
		// arcana-research targets give-all/reset-all | arcana-research targets give/try-advance/force-advance/reset research
	}
	
	public static int giveAll(CommandContext<CommandSource> ctx) throws CommandSyntaxException{
		// return number of players affected successfully
		AtomicInteger ret = new AtomicInteger();
		EntityArgument.getPlayers(ctx, "targets").forEach(entity -> {
			ResearchBooks.streamEntries().forEach(entry -> {
				Researcher from = Researcher.getFrom(entity);
				if(from != null){
					from.completeEntry(entry);
					Connection.sendModifyResearch(PkModifyResearch.Diff.complete, entry.key(), entity);
				}
			});
			ret.getAndIncrement();
		});
		return ret.get();
	}
	
	public static int resetAll(CommandContext<CommandSource> ctx) throws CommandSyntaxException{
		// return number of players affected successfully
		AtomicInteger ret = new AtomicInteger();
		EntityArgument.getPlayers(ctx, "targets").forEach(entity -> {
			ResearchBooks.streamEntries().forEach(entry -> {
				Researcher from = Researcher.getFrom(entity);
				if(from != null){
					from.resetEntry(entry);
					Connection.sendModifyResearch(PkModifyResearch.Diff.reset, entry.key(), entity);
				}
			});
			ret.getAndIncrement();
		});
		return ret.get();
	}
	
	public static int modify(CommandContext<CommandSource> ctx, Diff diff) throws CommandSyntaxException{
		AtomicInteger ret = new AtomicInteger();
		EntityArgument.getPlayers(ctx, "targets").forEach(entity -> {
			ResourceLocation key = ResourceLocationArgument.getResourceLocation(ctx, "research");
			ResearchEntry entry = ResearchBooks.streamEntries().filter(e -> e.key().equals(key)).findFirst().orElseThrow(() -> {
				Message noSuchEntry = new TranslationTextComponent("commands.arcana.research.no_entry", key.toString());
				return new RuntimeException(new CommandSyntaxException(new SimpleCommandExceptionType(noSuchEntry), noSuchEntry));
			});
			Researcher researcher = Researcher.getFrom(entity);
			if(researcher != null){
				if(diff == Diff.complete){
					researcher.completeEntry(entry);
					Connection.sendModifyResearch(PkModifyResearch.Diff.complete, key, entity);
				}else if(diff == Diff.advance){
					if(Researcher.canAdvanceEntry(researcher, entry)){
						Researcher.takeRequirementsAndAdvanceEntry(researcher, entry);
						Connection.sendModifyResearch(PkModifyResearch.Diff.advance, key, entity);
					}
				}else if(diff == Diff.forceAdvance){
					researcher.advanceEntry(entry);
					Connection.sendModifyResearch(PkModifyResearch.Diff.advance, key, entity);
				}else{
					researcher.resetEntry(entry);
					Connection.sendModifyResearch(PkModifyResearch.Diff.reset, key, entity);
				}
				ret.getAndIncrement();
			}
		});
		return ret.get();
	}
	
	private enum Diff{
		complete, advance, forceAdvance, reset
	}
}