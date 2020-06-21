package net.arcanamod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.*;
import net.arcanamod.items.PhialItem;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkModifyResearch;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.ResearchEntry;
import net.arcanamod.research.Researcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;
import static net.minecraft.command.arguments.ResourceLocationArgument.resourceLocation;

public class FillPhialCommand
{
    private static final SuggestionProvider<CommandSource> SUGGEST_FILL_PHIAL = (ctx, builder) -> ISuggestionProvider.func_212476_a(Arrays.stream(Aspect.values()).map(aspect -> new ResourceLocation(Arcana.MODID,aspect.toString().toLowerCase())), builder);

    public static void register(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(
                literal("arcana-phial").requires(source -> source.hasPermissionLevel(2))
                        .then(literal("fill")
                                .then(argument("targets", EntityArgument.players())
                                        .then(argument("amount", IntegerArgumentType.integer())
                                                .then(argument("aspect", resourceLocation()).executes(FillPhialCommand::fill).suggests(SUGGEST_FILL_PHIAL))))

                )
        );
    }

    public static int fill(CommandContext<CommandSource> ctx) throws CommandSyntaxException{
        // return number of players affected successfully
        AtomicInteger ret = new AtomicInteger();
        EntityArgument.getPlayers(ctx, "targets").forEach(serverPlayerEntity -> {
            ItemStack is = serverPlayerEntity.getHeldItemMainhand();
            IAspectHandler vis = is.getCapability(AspectHandlerCapability.ASPECT_HANDLER).orElse(null);
            ResourceLocation aspect_name = ResourceLocationArgument.getResourceLocation(ctx, "aspect");
            int amount = IntegerArgumentType.getInteger(ctx, "amount");
            if(vis != null){
                if(is.getItem() instanceof PhialItem){
                    Aspect targettedStack = Aspects.getAspectByName(aspect_name.getPath());
                    if(targettedStack != null){
                        vis.insert(0, new AspectStack(targettedStack, amount), false);
                        if(is.getTag() == null){
                            is.setTag(is.getShareTag());
                        }
                    }else{
                        serverPlayerEntity.sendMessage(new TranslationTextComponent("commands.arcana.fill.invalid_aspect", aspect_name).applyTextStyle(TextFormatting.RED));
                    }
                }else
                    serverPlayerEntity.sendMessage(new TranslationTextComponent("commands.arcana.fill.invalid_item", is.getItem().getRegistryName().toString()).applyTextStyle(TextFormatting.RED));
            }else
                serverPlayerEntity.sendMessage(new TranslationTextComponent("commands.arcana.fill.invalid_item", is.getItem().getRegistryName().toString()).applyTextStyle(TextFormatting.RED));
            ret.getAndIncrement();
        });
        return ret.get();
    }
}
