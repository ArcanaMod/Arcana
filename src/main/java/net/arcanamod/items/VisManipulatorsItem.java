package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.AspectBattery;
import net.arcanamod.aspects.AspectHandlerCapability;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.blocks.AspectTubeBlock;
import net.arcanamod.blocks.Taint;
import net.arcanamod.blocks.tiles.AspectTubeTileEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.atomic.AtomicInteger;

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
					if (context.getWorld().getTileEntity(context.getPos()) instanceof AspectTubeTileEntity){
						AspectTubeTileEntity h = (AspectTubeTileEntity)context.getWorld().getTileEntity(context.getPos()).getCapability(AspectHandlerCapability.ASPECT_HANDLER).orElse(null);
						context.getPlayer().sendMessage(
								new StringTextComponent((AspectUtils.aspectHandlerToJson(h) + "REMOTE: " + context.getWorld().isRemote).toString())
										.applyTextStyle(context.getWorld().isRemote ? TextFormatting.RED : TextFormatting.BLUE)
						);
					}
					else
					context.getPlayer().sendMessage(
							new StringTextComponent(((AspectBattery) context.getWorld().getTileEntity(context.getPos()).getCapability(AspectHandlerCapability.ASPECT_HANDLER).orElse(null) + "REMOTE: " + context.getWorld().isRemote).toString())
									.applyTextStyle(context.getWorld().isRemote ? TextFormatting.RED : TextFormatting.BLUE)
					);
			}
		if (context.getWorld().getBlockState(context.getPos()).getBlock() == Blocks.SPAWNER) {
			AtomicInteger i = new AtomicInteger();
			Taint.getTaintedEntities().forEach(entityType -> {
				Entity e = entityType.create(context.getWorld());
				e.setPosition(context.getPos().getX(),context.getPos().getY()+2,context.getPos().getZ()+ i.get());
				//e.setNoGravity(true);
				e.addTag("NoAI");
				e.setMotion(0,0,0);
				context.getWorld().addEntity(e);
				i.addAndGet(2);
			});
		}
		return ActionResultType.SUCCESS;
	}
}