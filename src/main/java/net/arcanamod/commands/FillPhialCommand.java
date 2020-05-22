package net.arcanamod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.VisHandler;
import net.arcanamod.aspects.VisHandlerCapability;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkModifyResearch;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.ResearchEntry;
import net.arcanamod.research.Researcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.item.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;
import static net.minecraft.command.arguments.ResourceLocationArgument.resourceLocation;

public class FillPhialCommand
{
    private static final SuggestionProvider<CommandSource> SUGGEST_FILL_PHIAL = (ctx, builder) -> ISuggestionProvider.func_212476_a(ResearchBooks.streamEntries().map(ResearchEntry::key), builder);

    public static void register(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(
                literal("arcana-phial").requires(source -> source.hasPermissionLevel(2))
                        .then(argument("targets", EntityArgument.players())
                                .then(literal("fill").executes(FillPhialCommand::fill))
                        )
        );
    }

    public static int fill(CommandContext<CommandSource> ctx) throws CommandSyntaxException
    {
        // return number of players affected successfully
        AtomicInteger ret = new AtomicInteger();
        EntityArgument.getPlayers(ctx, "targets").forEach(serverPlayerEntity -> {
            VisHandler vis = serverPlayerEntity.getHeldItemMainhand().getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null);
            if (vis!=null)
            {
                ItemStack is = serverPlayerEntity.getHeldItemMainhand();
                vis.insert(Aspect.EXCHANGE, 8, false);
                if(is.getTag() == null){
                    is.setTag(is.getShareTag());
                }
            }
            ret.getAndIncrement();
        });
        return ret.get();
    }
}
