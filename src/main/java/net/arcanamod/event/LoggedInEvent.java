package net.arcanamod.event;

import net.arcanamod.ArcanaConfig;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber
public class LoggedInEvent{

    //gives a player the scribbled notes item
    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (ArcanaConfig.SPAWN_WITH_NOTES.get()) {
            PlayerEntity player =event.getPlayer();
            CompoundNBT data = player.getPersistentData();
            if(!data.contains(PlayerEntity.PERSISTED_NBT_TAG)){
                event.getPlayer().inventory.addItemStackToInventory(new ItemStack(ArcanaItems.SCRIBBLED_NOTES.get()));
                data.put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());
            }
        }
    }
}