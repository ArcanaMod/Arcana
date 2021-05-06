package net.arcanamod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.arcanamod.aspects.*;
import net.arcanamod.items.MagicDeviceItem;
import net.arcanamod.items.PhialItem;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;
import static net.minecraft.command.arguments.ResourceLocationArgument.resourceLocation;

public class FillAspectCommand {
	private static final SuggestionProvider<CommandSource> SUGGEST_FILL_CONTAINER = (ctx, builder) -> ISuggestionProvider.func_212476_a(Arrays.stream(AspectUtils.primalAspects).map(Aspect::toResourceLocation), builder);

	public static void register(CommandDispatcher<CommandSource> dispatcher){
		dispatcher.register(
				literal("arcana-aspect").requires(source -> source.hasPermissionLevel(2))
						.then(literal("fill")
								.then(argument("targets", EntityArgument.players())
										.then(argument("amount", IntegerArgumentType.integer())
												.then(argument("aspect", resourceLocation()).executes(FillAspectCommand::fill).suggests(SUGGEST_FILL_CONTAINER))))

						)
		);
	}

	public static int fill(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		// return number of players affected successfully
		AtomicInteger ret = new AtomicInteger();
		EntityArgument.getPlayers(ctx, "targets").forEach(serverPlayerEntity -> {
			ItemStack is = serverPlayerEntity.getHeldItemMainhand();
			IAspectHandler vis = IAspectHandler.getFrom(is);
			ResourceLocation aspect_name = ResourceLocationArgument.getResourceLocation(ctx, "aspect");
			int amount = IntegerArgumentType.getInteger(ctx, "amount");
			if(vis != null){
				if(is.getItem() instanceof MagicDeviceItem || is.getItem() instanceof PhialItem){
					Aspect targettedStack = AspectUtils.getAspectByName(aspect_name.getPath());
					if(targettedStack != null){
						for (int i = 0; i < 6; i++) {
							vis.getHolder(i).insert(new AspectStack(targettedStack, amount), false);
						}
						if(is.getTag() == null)
							is.setTag(is.getShareTag());
					}else
						serverPlayerEntity.sendMessage(new TranslationTextComponent("commands.arcana.fill.invalid_aspect", aspect_name).mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
				}else
					serverPlayerEntity.sendMessage(new TranslationTextComponent("commands.arcana.fill.invalid_item", is.getItem().getRegistryName().toString()).mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
			}else
				serverPlayerEntity.sendMessage(new TranslationTextComponent("commands.arcana.fill.invalid_item", is.getItem().getRegistryName().toString()).mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
			ret.getAndIncrement();
		});
		return ret.get();
	}
}
