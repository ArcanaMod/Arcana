package net.arcanamod.client.event;

import com.google.common.collect.ImmutableList;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.mixin.AccessorMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientTickHandler {
	private ClientTickHandler() {}

	public static int ticksWithLexicaOpen = 0;
	public static int pageFlipTicks = 0;
	public static int ticksInGame = 0;
	public static float partialTicks = 0;
	public static float delta = 0;
	public static float total = 0;

	private static void calcDelta() {
		float oldTotal = total;
		total = ticksInGame + partialTicks;
		delta = total - oldTotal;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void renderTick(TickEvent.RenderTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (event.phase == TickEvent.Phase.START) {
			partialTicks = event.renderTickTime;

			if (mc.isGamePaused()) {
				// If game is paused, need to use the saved value. The event is always fired with the "true" value which
				// keeps updating when paused. See RenderTickEvent fire site for details, remove when MinecraftForge#6991 is resolved
				partialTicks = ((AccessorMinecraft) mc).getRenderPartialTicksPaused();
			}
		} else {
			calcDelta();
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void clientTickEnd(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			if (!Minecraft.getInstance().isGamePaused()) {
				ticksInGame++;
				partialTicks = 0;

				PlayerEntity player = Minecraft.getInstance().player;
				if (player != null) {
					int ticksToOpen = 10;

					Hand hand = null;
					if (player.getHeldItem(Hand.MAIN_HAND).getItem() == ArcanaItems.ARCANUM.get())
						hand = Hand.MAIN_HAND;
					if (player.getHeldItem(Hand.OFF_HAND).getItem() == ArcanaItems.ARCANUM.get())
						hand = Hand.OFF_HAND;

					if (hand != null)
						if (player.getHeldItem(hand).getOrCreateTag().getBoolean("open")) {
							if (ticksWithLexicaOpen < 0) {
								ticksWithLexicaOpen = 0;
							}
							if (ticksWithLexicaOpen < ticksToOpen) {
								ticksWithLexicaOpen++;
							}
							if (pageFlipTicks > 0) {
								pageFlipTicks--;
							}
						} else {
							pageFlipTicks = 0;
							if (ticksWithLexicaOpen > 0) {
								if (ticksWithLexicaOpen > ticksToOpen) {
									ticksWithLexicaOpen = ticksToOpen;
								}
								ticksWithLexicaOpen--;
							}
						}
				}
			}

			calcDelta();
		}
	}

	public static void notifyPageChange() {
		if (pageFlipTicks == 0) {
			pageFlipTicks = 5;
		}
	}
}
