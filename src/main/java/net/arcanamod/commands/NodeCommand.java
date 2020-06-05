package net.arcanamod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.ResearchEntry;
import net.arcanamod.world.INodeView;
import net.arcanamod.world.Node;
import net.arcanamod.world.NodeType;
import net.arcanamod.world.ServerNodeView;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class NodeCommand{
	
	private static final SuggestionProvider<CommandSource> SUGGEST_TYPES = (ctx, builder) -> ISuggestionProvider.func_212476_a(NodeType.TYPES.keySet().stream(), builder);
	
	public static void register(CommandDispatcher<CommandSource> dispatcher){
		// arcana-nodes <add|remove|modify>
		// arcana-nodes add <type> <x> <y> <z>
		// arcana-nodes remove <nodes>
		// arcana-nodes modify <nodes> <new type>
		// arcana-nodes info <nodes>
		// <nodes> = nearest | in[x1,y1,z1,x2,y2,z2] | nearest[max]
		// nearest is limited to 300x300 blocks centred on caller
		dispatcher.register(
				literal("arcana-nodes").requires(source -> source.hasPermissionLevel(2))
				.then(literal("add")
						.then(argument("type", ResourceLocationArgument.resourceLocation())
								.then(argument("position", Vec3Argument.vec3())
										.executes(NodeCommand::add)
								)
						)
				)
				.then(literal("remove")
						.then(argument("nodes", NodeArgument.nodes())
								.executes(NodeCommand::remove)
						)
				)
				.then(literal("modify")
						.then(argument("nodes", NodeArgument.nodes())
								.then(argument("type", ResourceLocationArgument.resourceLocation())
										.executes(NodeCommand::modify)
								)
						)
				)
				.then(literal("info")
						.then(argument("nodes", NodeArgument.nodes())
								.executes(NodeCommand::info)
						)
				)
		);
	}
	
	public static int add(CommandContext<CommandSource> ctx) throws CommandSyntaxException{
		ResourceLocation type = ResourceLocationArgument.getResourceLocation(ctx, "type");
		NodeType nt;
		if(!NodeType.TYPES.containsKey(type)){
			// throw exception "nonexistent type"
			Message noSuchEntry = new TranslationTextComponent("command.arcana.nodes.no_type", type.toString());
			throw new CommandSyntaxException(new SimpleCommandExceptionType(noSuchEntry), noSuchEntry);
		}else
			nt = NodeType.TYPES.get(type);
		Vec3d loc = Vec3Argument.getVec3(ctx, "position");
		Node node = new Node(nt.genNodeAspects(new BlockPos(loc), ctx.getSource().getWorld(), new Random()), nt, loc.x, loc.y, loc.z);
		INodeView view = new ServerNodeView(ctx.getSource().getWorld());
		// Send PkSyncChunkNodes
		return view.addNode(node) ? 1 : 0;
	}
	
	public static int remove(CommandContext<CommandSource> ctx) throws CommandSyntaxException{
		return 0;
	}
	
	public static int modify(CommandContext<CommandSource> ctx) throws CommandSyntaxException{
		return 0;
	}
	
	public static int info(CommandContext<CommandSource> ctx) throws CommandSyntaxException{
		return 0;
	}
}