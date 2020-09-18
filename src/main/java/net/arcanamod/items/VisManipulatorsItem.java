package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectHandlerCapability;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.blocks.Taint;
import net.arcanamod.blocks.tiles.AspectTubeTileEntity;
import net.arcanamod.systems.spell.CastAspect;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.SpellExtraData;
import net.arcanamod.systems.spell.Spells;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
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
	public ActionResultType onItemUse(ItemUseContext context){
		TileEntity entity = context.getWorld().getTileEntity(context.getPos());
		if(entity != null && entity.getCapability(AspectHandlerCapability.ASPECT_HANDLER).isPresent()){
			onItemUseFirst(null, context);
			return ActionResultType.SUCCESS;
		}
		return super.onItemUse(context);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		/*if (Arcana.debug)
			if (context.getWorld().getTileEntity(context.getPos())!=null) {
				if (context.getWorld().getTileEntity(context.getPos()).getCapability(AspectHandlerCapability.ASPECT_HANDLER).orElse(null) != null){
					if (context.getWorld().getTileEntity(context.getPos()) instanceof AspectTubeTileEntity){
						AspectTubeTileEntity h = (AspectTubeTileEntity)context.getWorld().getTileEntity(context.getPos()).getCapability(AspectHandlerCapability.ASPECT_HANDLER).orElse(null);
						context.getPlayer().sendMessage(
								new StringTextComponent((AspectUtils.aspectHandlerToJson(h) + "REMOTE: " + context.getWorld().isRemote))
										.applyTextStyle(context.getWorld().isRemote ? TextFormatting.RED : TextFormatting.BLUE)
						);
					}
					else
					context.getPlayer().sendMessage(
							new StringTextComponent((context.getWorld().getTileEntity(context.getPos()).getCapability(AspectHandlerCapability.ASPECT_HANDLER).orElse(null) + "REMOTE: " + context.getWorld().isRemote))
									.applyTextStyle(context.getWorld().isRemote ? TextFormatting.RED : TextFormatting.BLUE)
					);
					return ActionResultType.SUCCESS;
				}
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
			return ActionResultType.SUCCESS;
		}*/
		Spells.MINING_SPELL.build(new Aspect[0],new CastAspect[0],new SpellExtraData()).use(context.getPlayer(), ISpell.Action.USE);
		return super.onItemUseFirst(stack, context);
	}
}