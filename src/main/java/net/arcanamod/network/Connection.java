package net.arcanamod.network;

import net.arcanamod.Arcana;
import net.arcanamod.research.Researcher;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
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
		INSTANCE.registerMessage(id++, PkModifyResearch.class, PkModifyResearch::encode, PkModifyResearch::decode, PkModifyResearch::handle);
		INSTANCE.registerMessage(id++, PkSyncPlayerResearch.class, PkSyncPlayerResearch::encode, PkSyncPlayerResearch::decode, PkSyncPlayerResearch::handle);
	}
	
	public static void sendModifyResearch(PkModifyResearch.Diff change, ResourceLocation research, ServerPlayerEntity target){
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new PkModifyResearch(change, research));
	}
	
	public static void sendSyncPlayerResearch(Researcher from, ServerPlayerEntity target){
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new PkSyncPlayerResearch(from.getEntryData(), from.getPuzzleData()));
	}
}