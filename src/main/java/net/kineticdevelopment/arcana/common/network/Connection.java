package net.kineticdevelopment.arcana.common.network;

import net.kineticdevelopment.arcana.core.Main;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Connection{
	
	public static SimpleNetworkWrapper network;
	private static int id = 0;
	
	public static void init(){
		network = NetworkRegistry.INSTANCE.newSimpleChannel(Main.MODID);
		
		network.registerMessage(PktSyncBooksHandler.class, PktSyncBooks.class, id++, Side.CLIENT);
	}
}