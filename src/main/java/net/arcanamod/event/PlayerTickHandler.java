package net.arcanamod.event;

import net.arcanamod.items.ArcanaItems;
import net.arcanamod.world.INodeView;
import net.arcanamod.world.Node;
import net.arcanamod.world.ServerNodeView;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;

public class PlayerTickHandler {


    public static PlayerTickHandler instance = new PlayerTickHandler();

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent event){

        PlayerEntity player = event.player;
        BlockPos blockPos = new BlockPos(player.getPosition());


        if(player instanceof ServerPlayerEntity) {


            //Finds if a player is close to a node
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            INodeView view = new ServerNodeView(serverPlayerEntity.getServerWorld());
            Collection<Node> ranged = new ArrayList<>(view.getNodesWithinAABB(player.getBoundingBox().grow(2)));


            //If a player is holding the scribbled notes item it switches it for a complete version
            if (!ranged.isEmpty() && player.inventory.hasItemStack(new ItemStack(ArcanaItems.SCRIBBLED_NOTES.get()))) {
                ITextComponent iTextComponent = new StringTextComponent("\247d\247oYou hear whispers coming from this archaic source and gain a font of inspiration. Maybe you should look at those notes again");
                player.inventory.setInventorySlotContents(player.inventory.getSlotFor(new ItemStack(ArcanaItems.SCRIBBLED_NOTES.get())) ,new ItemStack(ArcanaItems.SCRIBBLED_NOTES_COMPLETE.get()));
                serverPlayerEntity.sendStatusMessage(iTextComponent , false);
            }
        }
    }
}
