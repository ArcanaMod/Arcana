package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectHandlerCapability;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
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
		getSpell().use(context.getPlayer(), ISpell.Action.USE);
		return super.onItemUseFirst(stack, context);
	}

	public ISpell getSpell(){
		return Spells.MINING_SPELL.build(
			new Aspect[]{Aspects.EMPTY,Aspects.EMPTY,Aspects.EMPTY},
			new CastAspect[]{CastAspect.getEmpty(),CastAspect.getEmpty(),CastAspect.getEmpty()},
			new SpellExtraData());
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return getSpell().getSpellDuration(); // One hour
	}
}