package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.client.ClientUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ScribbledNoteItem extends Item{
	
	public ScribbledNoteItem(Properties properties){
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand){
		ClientUtils.openScribbledNotesUI();
		return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}
}
