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
import net.minecraft.util.text.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;

@Mod.EventBusSubscriber
public class PlayerTickHandler{
	
	@SubscribeEvent
	public static void tick(TickEvent.PlayerTickEvent event){
		PlayerEntity player = event.player;
		
		if(player instanceof ServerPlayerEntity && event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END){
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
			// If the player is near a node,
			INodeView view = new ServerNodeView(serverPlayerEntity.getServerWorld());
			Collection<Node> ranged = new ArrayList<>(view.getNodesWithinAABB(player.getBoundingBox().grow(2)));
			
			// and is holding the scribbled notes item,
			if(!ranged.isEmpty() && player.inventory.hasItemStack(new ItemStack(ArcanaItems.SCRIBBLED_NOTES.get()))){
				// it switches it for a complete version,
				player.inventory.setInventorySlotContents(player.inventory.getSlotFor(new ItemStack(ArcanaItems.SCRIBBLED_NOTES.get())), new ItemStack(ArcanaItems.SCRIBBLED_NOTES_COMPLETE.get()));
				// and gives them a status message.
				ITextComponent status = new TranslationTextComponent("status.get_complete_note").applyTextStyles(TextFormatting.ITALIC, TextFormatting.LIGHT_PURPLE);
				serverPlayerEntity.sendStatusMessage(status, false);
			}
		}
	}
}
