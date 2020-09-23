package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.CastAspect;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.Spells;
import net.arcanamod.systems.spell.impls.Spell;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VisManipulatorsItem extends Item{
	
	public VisManipulatorsItem(Properties properties){
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context){
			onItemUseFirst(null, context);
			return ActionResultType.SUCCESS;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		ItemStack toSet = new ItemStack(ArcanaItems.DEFAULT_FOCUS.get(),1);
		int r = random.nextInt(4);
		if (r==0){
			toSet.getOrCreateTag().put("Spell", Spell.serializeNBT(Spells.MINING_SPELL.build(
					Collections.emptyList(),
					Collections.singletonList(new CastAspect(Aspects.CHAOS,Aspects.GREED)),
					new CompoundNBT())));
		} else if (r==1) {
			toSet.getOrCreateTag().put("Spell",Spell.serializeNBT(Spells.EXCHANGE_SPELL.build(
					Collections.emptyList(),
					Collections.emptyList(),
					new CompoundNBT())));

		} else {
			toSet.getOrCreateTag().put("Spell",Spell.serializeNBT(Spells.FABRIC_SPELL.build(
					Collections.emptyList(),
					Collections.singletonList(new CastAspect(Aspects.EARTH,Aspects.EMPTY)),
					new CompoundNBT())));
		}
		toSet.getOrCreateTag().putInt("style",random.nextInt(14));
		context.getPlayer().addItemStackToInventory(toSet);
		return super.onItemUseFirst(stack, context);
	}

	public ISpell getSpell(){
		return Spells.MINING_SPELL.build(Collections.emptyList(), Collections.emptyList(), new CompoundNBT());
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return getSpell().getSpellDuration(); // One hour
	}
}