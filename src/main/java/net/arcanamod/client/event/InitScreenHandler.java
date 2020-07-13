package net.arcanamod.client.event;

import net.arcanamod.Arcana;
import net.arcanamod.client.gui.ArcanaDevOptionsScreen;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Arcana.MODID)
public class InitScreenHandler {
	@SubscribeEvent
	public static void onInitGuiEvent(final GuiScreenEvent.InitGuiEvent event) {
		final Screen gui = event.getGui();
		if (gui instanceof IngameMenuScreen) {
			Widget rm_button = null;
			for (final Widget button : event.getWidgetList()) {
				if (button.getMessage().equals(I18n.format("menu.reportBugs")))
					rm_button = button;
			} // You can't report bugs because forge is installed
			event.removeWidget(rm_button);
			event.addWidget(new Button(gui.width / 2 + 4, gui.height / 4 + 72 + -16, 98, 20,"More...",p_onPress_1_ -> {
				event.getGui().getMinecraft().displayGuiScreen(new ArcanaDevOptionsScreen());
			}));
		}
	}
}
