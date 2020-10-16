package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.Taint;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.SpellData;
import net.arcanamod.systems.spell.Spells;
import net.arcanamod.systems.spell.impls.Spell;
import net.arcanamod.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

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
		if (context.getWorld().getBlockState(context.getPos()).getBlock() == Blocks.SPAWNER) {
			AtomicInteger i = new AtomicInteger();
			AtomicInteger j = new AtomicInteger();
			Taint.getTaintedEntities().forEach(entityType -> {
				Entity e = entityType.create(context.getWorld());
				e.setPosition(context.getPos().getX()-j.get(),context.getPos().getY()+2,context.getPos().getZ()+ i.get());
				//e.setNoGravity(true);
				e.addTag("NoAI");
				e.setMotion(0,0,0);
				context.getWorld().addEntity(e);
				i.addAndGet(2);
				if (i.get()>=10){
					i.set(0);
					j.addAndGet(1);
				}
			});
			return ActionResultType.SUCCESS;
		}else{
			ItemStack toSet = new ItemStack(ArcanaItems.DEFAULT_FOCUS.get(), 1);
			int r = random.nextInt(5);
			if (r == 0) {
				toSet.getOrCreateTag().put("Spell", Spell.serializeNBT(Spells.MINING_SPELL.build(
						new SpellData(Aspects.EMPTY, Aspects.EMPTY, Aspects.EMPTY, Pair.of(Aspects.EARTH, Aspects.GLUTTONY), Pair.of(Aspects.EMPTY, Aspects.EMPTY)),
						new CompoundNBT())));
			} else if (r == 1) {
				toSet.getOrCreateTag().put("Spell", Spell.serializeNBT(Spells.EXCHANGE_SPELL.build(
						new SpellData(Aspects.EMPTY, Aspects.EMPTY, Aspects.EMPTY, Pair.of(Aspects.EARTH, Aspects.LUST), Pair.of(Aspects.EMPTY, Aspects.EMPTY)),
						new CompoundNBT())));

			} else if (r == 2) {
				toSet.getOrCreateTag().put("Spell", Spell.serializeNBT(Spells.FABRIC_SPELL.build(
						new SpellData(Aspects.EMPTY, Aspects.EMPTY, Aspects.EMPTY, Pair.of(Aspects.FIRE, Aspects.EMPTY), Pair.of(Aspects.EMPTY, Aspects.EMPTY)),
						new CompoundNBT())));
			} else {
				toSet.getOrCreateTag().put("Spell", Spell.serializeNBT(Spells.VACUUM_SPELL.build(
						new SpellData(Aspects.EARTH, Aspects.EARTH, Aspects.EARTH, Pair.of(Aspects.EARTH, Aspects.EMPTY), Pair.of(Aspects.EMPTY, Aspects.EMPTY)),
						new CompoundNBT())));
			}
			toSet.getOrCreateTag().putInt("style", random.nextInt(36));
			context.getPlayer().addItemStackToInventory(toSet);
		}
		return super.onItemUseFirst(stack, context);
	}

	public ISpell getSpell(){
		return Spells.MINING_SPELL.build(
				new SpellData(Aspects.EMPTY,Aspects.EMPTY,Aspects.EMPTY, Pair.of(Aspects.EARTH,Aspects.GLUTTONY), Pair.of(Aspects.EMPTY,Aspects.EMPTY)),
				new CompoundNBT());
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return getSpell().getSpellDuration();
	}
}