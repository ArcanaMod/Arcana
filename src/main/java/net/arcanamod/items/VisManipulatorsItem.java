package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectHandlerCapability;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.Taint;
import net.arcanamod.blocks.tiles.AspectTubeTileEntity;
import net.arcanamod.items.attachment.FocusItem;
import net.arcanamod.systems.spell.CastAspect;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.SpellExtraData;
import net.arcanamod.systems.spell.Spells;
import net.arcanamod.systems.spell.impls.Spell;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VisManipulatorsItem extends Item{
	
	public VisManipulatorsItem(Properties properties){
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context){
		/*TileEntity entity = context.getWorld().getTileEntity(context.getPos());
		if(entity != null && entity.getCapability(AspectHandlerCapability.ASPECT_HANDLER).isPresent()){*/
			onItemUseFirst(null, context);
			return ActionResultType.SUCCESS;
		//}
		//return super.onItemUse(context);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		ItemStack toSet = new ItemStack(ArcanaItems.DEFAULT_FOCUS.get(),1);
		int r = random.nextInt(4);
		if (r==0){
			toSet.getOrCreateTag().put("Spell", Spell.serializeNBT(Spells.MINING_SPELL.build(
					new Aspect[]{Aspects.EMPTY, Aspects.EMPTY, Aspects.EMPTY},
					new CastAspect[]{CastAspect.getEmpty(), CastAspect.getEmpty(), CastAspect.getEmpty()},
					new SpellExtraData())));
		} else if (r==1) {
			toSet.getOrCreateTag().put("Spell",Spell.serializeNBT(Spells.EXCHANGE_SPELL.build(
					new Aspect[]{Aspects.EMPTY,Aspects.EMPTY,Aspects.EMPTY},
					new CastAspect[]{CastAspect.getEmpty(),CastAspect.getEmpty(),CastAspect.getEmpty()},
					new SpellExtraData())));

		} else {
			toSet.getOrCreateTag().put("Spell",Spell.serializeNBT(Spells.FABRIC_SPELL.build(
					new Aspect[]{Aspects.EMPTY,Aspects.EMPTY,Aspects.EMPTY},
					new CastAspect[]{new CastAspect(Aspects.EARTH,Aspects.EMPTY),CastAspect.getEmpty(),CastAspect.getEmpty()},
					new SpellExtraData())));
		}
		toSet.getOrCreateTag().putInt("style",random.nextInt(14));
		context.getPlayer().addItemStackToInventory(toSet);
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