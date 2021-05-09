package net.arcanamod.client.event;

import net.arcanamod.Arcana;
import net.arcanamod.client.gui.ArcanaDevOptionsScreen;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Arcana.MODID, value = Dist.CLIENT)
public class InitScreenHandler{
	
	public static final boolean DEBUG_MODE = false;
	
	@SubscribeEvent
	public static void onInitGuiEvent(final GuiScreenEvent.InitGuiEvent event){
		if(DEBUG_MODE){
			final Screen gui = event.getGui();
			if(gui instanceof IngameMenuScreen){
				Widget rm_button = null;
				for(final Widget button : event.getWidgetList()){
					if(button.getMessage().getString().equals(I18n.format("menu.reportBugs")))
						rm_button = button;
				} // You can't report bugs because forge is installed
				event.removeWidget(rm_button);
				event.addWidget(new Button(gui.width / 2 + 4, gui.height / 4 + 72 + -16, 98, 20, new TranslationTextComponent("devtools.more"), p_onPress_1_ -> event.getGui().getMinecraft().displayGuiScreen(new ArcanaDevOptionsScreen())));
			}
		}
	}
}
