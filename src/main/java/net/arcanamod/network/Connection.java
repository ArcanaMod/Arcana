package net.arcanamod.network;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.blocks.tiles.FociForgeTileEntity;
import net.arcanamod.containers.AspectContainer;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.systems.research.Pin;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import javax.annotation.Nonnull;

import static net.arcanamod.Arcana.arcLoc;

public class Connection{
	
	private static int id = -100;
	private static final String PROTOCOL_RELEASE = "Arcana_2";
	
	public static SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
			.named(arcLoc("main"))
			.clientAcceptedVersions(PROTOCOL_RELEASE::equals)
			.serverAcceptedVersions(PROTOCOL_RELEASE::equals)
			.networkProtocolVersion(() -> PROTOCOL_RELEASE)
			.simpleChannel();
	
	public static void init(){
		INSTANCE.registerMessage(id++, PkSyncResearch.class, PkSyncResearch::encode, PkSyncResearch::decode, PkSyncResearch::handle);
		INSTANCE.registerMessage(id++, PkModifyResearch.class, PkModifyResearch::encode, PkModifyResearch::decode, PkModifyResearch::handle);
		INSTANCE.registerMessage(id++, PkSyncPlayerResearch.class, PkSyncPlayerResearch::encode, PkSyncPlayerResearch::decode, PkSyncPlayerResearch::handle);
		INSTANCE.registerMessage(id++, PkTryAdvance.class, PkTryAdvance::encode, PkTryAdvance::decode, PkTryAdvance::handle);
		INSTANCE.registerMessage(id++, PkAspectClick.class, PkAspectClick::encode, PkAspectClick::decode, PkAspectClick::handle);
		INSTANCE.registerMessage(id++, PkSyncAspectContainer.class, PkSyncAspectContainer::encode, PkSyncAspectContainer::decode, PkSyncAspectContainer::handle);
		INSTANCE.registerMessage(id++, PkGetNote.class, PkGetNote::encode, PkGetNote::decode, PkGetNote::handle);
		INSTANCE.registerMessage(id++, PkSyncChunkNodes.class, PkSyncChunkNodes::encode, PkSyncChunkNodes::decode, PkSyncChunkNodes::handle);
		INSTANCE.registerMessage(id++, PkRequestNodeSync.class, PkRequestNodeSync::encode, PkRequestNodeSync::decode, PkRequestNodeSync::handle);
		INSTANCE.registerMessage(id++, PkClientSlotDrain.class, PkClientSlotDrain::encode, PkClientSlotDrain::decode, PkClientSlotDrain::handle);
		INSTANCE.registerMessage(id++, PkSyncPlayerFlux.class, PkSyncPlayerFlux::encode, PkSyncPlayerFlux::decode, PkSyncPlayerFlux::handle);
		INSTANCE.registerMessage(id++, PkSwapFocus.class, PkSwapFocus::encode, PkSwapFocus::decode, PkSwapFocus::handle);
		INSTANCE.registerMessage(id++, PkFociForgeAction.class, PkFociForgeAction::encode, PkFociForgeAction::decode, PkFociForgeAction::handle);
		INSTANCE.registerMessage(id++, PkModifyPins.class, PkModifyPins::encode, PkModifyPins::decode, PkModifyPins::handle);
		INSTANCE.registerMessage(id++, PkWriteSpellToFoci.class, PkWriteSpellToFoci::encode, PkWriteSpellToFoci::decode, PkWriteSpellToFoci::handle);
	}
	
	public static void sendTo(Object packet, ServerPlayerEntity target){
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), packet);
	}
	
	public static void sendToServer(Object packet){
		INSTANCE.send(PacketDistributor.SERVER.noArg(), packet);
	}
	
	public static void sendModifyResearch(PkModifyResearch.Diff change, ResourceLocation research, ServerPlayerEntity target){
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new PkModifyResearch(change, research));
	}
	
	public static void sendTryAdvance(ResourceLocation research){
		INSTANCE.sendToServer(new PkTryAdvance(research));
	}
	
	public static void sendSyncPlayerResearch(Researcher from, ServerPlayerEntity target){
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new PkSyncPlayerResearch(from.serializeNBT()));
	}

	public static void sendAspectClick(int windowId, int slotId, PkAspectClick.ClickType type, @Nonnull Aspect expectedAspect){
		INSTANCE.sendToServer(new PkAspectClick(windowId, slotId, type, expectedAspect));
	}

	public static void sendSyncAspectContainer(AspectContainer container, ServerPlayerEntity target) {
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new PkSyncAspectContainer(container));
	}

	public static void sendGetNoteHandler(ResourceLocation id, String pageName) {
		INSTANCE.sendToServer(new PkGetNote(id, pageName));
	}

	public static void sendClientSlotDrain(int windowId, int slotId, PkAspectClick.ClickType type, ServerPlayerEntity target) {
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new PkClientSlotDrain(windowId, slotId, type));
	}

	public static void sendFociForgeAction(int windowId, PkFociForgeAction.Type action, int ax, int ay, int bx, int by, int sequence, Aspect aspect){
		INSTANCE.sendToServer(new PkFociForgeAction(windowId, action, ax, ay, bx, by, sequence, aspect));
	}

	public static void sendModifyPins(Pin pin, PkModifyPins.Diff diff){
		INSTANCE.sendToServer(new PkModifyPins(diff, pin.getEntry().key(), pin.getStage()));
	}

	public static void sendWriteSpell(ItemStack focus, CompoundNBT nbt) {
		INSTANCE.sendToServer(new PkWriteSpellToFoci(focus,nbt));
	}
}