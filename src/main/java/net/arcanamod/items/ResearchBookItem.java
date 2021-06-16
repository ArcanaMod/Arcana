package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.client.ClientUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ResearchBookItem extends Item{
	ResourceLocation book;
	public ResearchBookItem(Properties properties, ResourceLocation book){
		super(properties);
		this.book = book;
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand){
		player.getHeldItem(hand).getOrCreateTag().putBoolean("open",true);

		ClientUtils.openResearchBookUI(book, null, player.getHeldItem(hand));
		return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}
}