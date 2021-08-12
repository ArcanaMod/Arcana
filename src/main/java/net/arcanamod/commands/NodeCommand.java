package net.arcanamod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.arcanamod.world.Node;
import net.arcanamod.world.NodeType;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Collection;
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
								.suggests(SUGGEST_TYPES)
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
										.suggests(SUGGEST_TYPES)
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
			Message noSuchEntry = new TranslationTextComponent("commands.arcana.nodes.no_type", type.toString());
			throw new CommandSyntaxException(new SimpleCommandExceptionType(noSuchEntry), noSuchEntry);
		}else
			nt = NodeType.TYPES.get(type);
		Vector3d loc = Vec3Argument.getVec3(ctx, "position");
		Node node = new Node(nt.genBattery(new BlockPos(loc), ctx.getSource().getWorld(), new Random()), nt, loc.x, loc.y, loc.z, 0);
		ServerAuraView view = new ServerAuraView(ctx.getSource().getWorld());
		boolean added = view.addNode(node);
		// Send PkSyncChunkNodes and feedback
		if(added){
			view.sendChunkToClients(node);
			ctx.getSource().sendFeedback(new TranslationTextComponent("commands.arcana.nodes.add_success"), true);
			return 1;
		}
		ctx.getSource().sendErrorMessage(new TranslationTextComponent("commands.arcana.nodes.add_fail"));
		return 0;
	}
	
	public static int remove(CommandContext<CommandSource> ctx){
		Collection<Node> nodes = NodeArgument.getNodes(ctx, "nodes");
		if(nodes.size() == 0){
			// send feedback
			ctx.getSource().sendErrorMessage(new TranslationTextComponent("commands.arcana.nodes.empty_selection"));
			return 0;
		}
		
		ServerAuraView view = new ServerAuraView(ctx.getSource().getWorld());
		Collection<Node> removed = new ArrayList<>();
		for(Node node : nodes)
			if(view.removeNode(node))
				removed.add(node);
			
		if(removed.size() == 0)
			ctx.getSource().sendErrorMessage(new TranslationTextComponent("commands.arcana.nodes.remove_fail"));
		else if(removed.size() == 1)
			ctx.getSource().sendFeedback(new TranslationTextComponent("commands.arcana.nodes.remove_success.single"), true);
		else
			ctx.getSource().sendFeedback(new TranslationTextComponent("commands.arcana.nodes.remove_success.many", removed.size()), true);
		
		view.sendAllChunksToClients(removed);
		return removed.size();
	}
	
	public static int modify(CommandContext<CommandSource> ctx) throws CommandSyntaxException{
		Collection<Node> nodes = NodeArgument.getNodes(ctx, "nodes");
		if(nodes.size() == 0){
			// send feedback
			ctx.getSource().sendErrorMessage(new TranslationTextComponent("commands.arcana.nodes.empty_selection"));
			return 0;
		}
		
		ResourceLocation type = ResourceLocationArgument.getResourceLocation(ctx, "type");
		NodeType nt;
		if(!NodeType.TYPES.containsKey(type)){
			// throw exception "nonexistent type"
			Message noSuchEntry = new TranslationTextComponent("commands.arcana.nodes.no_type", type.toString());
			throw new CommandSyntaxException(new SimpleCommandExceptionType(noSuchEntry), noSuchEntry);
		}else
			nt = NodeType.TYPES.get(type);
		
		ServerAuraView view = new ServerAuraView(ctx.getSource().getWorld());
		for(Node node : nodes)
			node.setType(nt);
		
		if(nodes.size() == 1)
			ctx.getSource().sendFeedback(new TranslationTextComponent("commands.arcana.nodes.modify_success.single"), true);
		else
			ctx.getSource().sendFeedback(new TranslationTextComponent("commands.arcana.nodes.modify_success.many", nodes.size()), true);
		
		view.sendAllChunksToClients(nodes);
		return nodes.size();
	}
	
	public static int info(CommandContext<CommandSource> ctx){
		Collection<Node> nodes = NodeArgument.getNodes(ctx, "nodes");
		ctx.getSource().sendFeedback(new TranslationTextComponent("commands.arcana.nodes.info", nodes), true);
		return nodes.size();
	}
}