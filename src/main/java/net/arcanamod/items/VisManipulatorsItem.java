package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.AspectBattery;
import net.arcanamod.aspects.AspectHandlerCapability;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VisManipulatorsItem extends Item{
	
	public VisManipulatorsItem(Properties properties){
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		onItemUseFirst(null,context);
		return ActionResultType.SUCCESS;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		if (Arcana.debug)
			if (context.getWorld().getTileEntity(context.getPos())!=null) {
				if (context.getWorld().getTileEntity(context.getPos()).getCapability(AspectHandlerCapability.ASPECT_HANDLER).orElse(null) != null)
					context.getPlayer().sendMessage(
							new StringTextComponent(((AspectBattery) context.getWorld().getTileEntity(context.getPos()).getCapability(AspectHandlerCapability.ASPECT_HANDLER).orElse(null) + "REMOTE: " + context.getWorld().isRemote).toString())
									.applyTextStyle(context.getWorld().isRemote ? TextFormatting.RED : TextFormatting.BLUE)
					);
			}
		return ActionResultType.SUCCESS;
	}
}