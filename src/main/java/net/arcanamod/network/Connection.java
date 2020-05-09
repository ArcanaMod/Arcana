package net.arcanamod.network;

import net.arcanamod.Arcana;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Connection{
	
	private static int id = -100;
	private static final String PROTOCOL_RELEASE = "Arcana1";
	
	public static SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(Arcana.MODID, "main"))
			.clientAcceptedVersions(PROTOCOL_RELEASE::equals)
			.serverAcceptedVersions(PROTOCOL_RELEASE::equals)
			.networkProtocolVersion(() -> PROTOCOL_RELEASE)
			.simpleChannel();
	
	public static void init(){
		INSTANCE.registerMessage(id++, PkSyncResearch.class, PkSyncResearch::encode, PkSyncResearch::decode, PkSyncResearch::handle);
	}
	
	/*@SuppressWarnings("Convert2MethodRef")
	static void init(){
		INSTANCE.registerMessage(id++, PktSyncResearch.class, PktSyncResearch::encode, PktSyncResearch::decode, PktSyncResearch::handle);
		INSTANCE.registerMessage(id++, PktResetResearch.class, (m, packetBuffer) -> PktResetResearch.encode(m, packetBuffer), buffer -> PktResetResearch.decode(buffer), (m, c) -> PktResetResearch.handle(m, c));
		INSTANCE.registerMessage(id++, PktAdvanceResearch.class, (m, p) -> PktAdvanceResearch.encode(m, p), buffer -> PktAdvanceResearch.decode(buffer), (m, c) -> PktAdvanceResearch.handle(m, c));
		INSTANCE.registerMessage(id++, PktCompleteResearch.class, (m, packetBuffer) -> PktCompleteResearch.encode(m, packetBuffer), buffer -> PktCompleteResearch.decode(buffer), (m, c) -> PktCompleteResearch.handle(m, c));
		INSTANCE.registerMessage(id++, PktTryAdvanceResearch.class, (m, p) -> PktTryAdvanceResearch.encode(m, p), buffer -> PktTryAdvanceResearch.decode(buffer), (m, c) -> PktTryAdvanceResearch.handle(m, c));
	}*/
}