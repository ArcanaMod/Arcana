package net.arcanamod.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.arcanamod.world.Node;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collection;

public class NodeArgument implements ArgumentType<NodeSelector>{
	
	private static final Collection<String> EXAMPLES = Arrays.asList("@n", "@i[0, 0, 0, 20, 20, 20]", "@n[5]");
	public static final SimpleCommandExceptionType SELECTOR_TYPE_INVALID = new SimpleCommandExceptionType(new TranslationTextComponent("argument.arcana.node.selector.invalid"));
	
	public NodeSelector parse(StringReader reader) throws CommandSyntaxException{
		// check if it starts with "@", otherwise throw
		if(reader.peek() == '@'){
			reader.skip();
			// if its then 'n', parse nearest
			if(reader.peek() == 'n'){
				reader.skip();
				int max = 1;
				// if it has '[', parse maximum
				if(reader.canRead() && reader.peek() == '['){
					skipAndWhitespace(reader);
					max = reader.readInt();
					reader.skipWhitespace();
					reader.skip();
				}
				// return nearest selector with given max
				return new NodeSelector(false, null, max);
			}else if(reader.peek() == 'i'){
				reader.skip();
				int x0 = 0, y0 = 0, z0 = 0, x1 = 0, y1 = 0, z1 = 0;
				if(reader.peek() == '['){
					skipAndWhitespace(reader);
					x0 = reader.readInt();
					skipAndWhitespace(reader);
					y0 = reader.readInt();
					skipAndWhitespace(reader);
					z0 = reader.readInt();
					skipAndWhitespace(reader);
					x1 = reader.readInt();
					skipAndWhitespace(reader);
					y1 = reader.readInt();
					skipAndWhitespace(reader);
					z1 = reader.readInt();
					reader.skipWhitespace();
					reader.skip();
				}
				return new NodeSelector(true, new AxisAlignedBB(x0, y0, z0, x1, y1, z1), 0);
			}
		}
		throw SELECTOR_TYPE_INVALID.createWithContext(reader);
	}
	
	private void skipAndWhitespace(StringReader reader){
		reader.skipWhitespace();
		reader.skip();
		reader.skipWhitespace();
	}
	
	public static NodeArgument nodes(){
		return new NodeArgument();
	}
	
	public Collection<String> getExamples(){
		return EXAMPLES;
	}
	
	public static Collection<Node> getNodes(CommandContext<CommandSource> context, String name){
		return context.getArgument(name, NodeSelector.class).select(context.getSource());
	}
}