package net.arcanamod.event;

import net.arcanamod.ArcanaConfig;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber
public class LoggedInEvent{

    //gives a player the scribbled notes item
    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        /*
        if (ArcanaConfig.SPAWN_WITH_NOTES.get()) {
            event.getPlayer().inventory.addItemStackToInventory(new ItemStack(ArcanaItems.SCRIBBLED_NOTES.get()));
        }

         */
    }
}