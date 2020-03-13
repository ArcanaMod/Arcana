package net.kineticdevelopment.arcana.common.network;

import net.kineticdevelopment.arcana.client.research.ClientBooks;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PktSyncBooksHandler implements IMessageHandler<PktSyncBooks, IMessage>{
	
	public IMessage onMessage(PktSyncBooks message, MessageContext ctx){
		// from server -> client
		// TODO: this should probably be made into something somewhat better
		ClientBooks.books = message.data;
		return null;
	}
}