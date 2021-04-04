package net.arcanamod.items;

import net.arcanamod.aspects.AspectStack;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.systems.spell.Spell;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StaffItem extends MagicDeviceItem{
	public StaffItem(Item.Properties properties){
		super(properties);
	}

	@Override
	public boolean canCraft() {
		return false;
	}

	@Override
	public boolean canUseSpells() {
		return true;
	}

	@Override
	public String getDeviceName() {
		return "item.arcana.wand.variant.staff";
	}

	@Override
	protected float getVisModifier() {
		return 2.5f;
	}

	@Override
	protected float getDifficultyModifier() {
		return 1;
	}

	@Override
	protected float getComplexityModifier() {
		return 1;
	}

	public static ItemStack withCapAndCore(String cap, String core){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("cap", cap);
		nbt.putString("core", core);
		ItemStack stack = new ItemStack(ArcanaItems.WAND.get(), 1);
		stack.setTag(nbt);
		return stack;
	}

	public static ItemStack withCapAndCore(ResourceLocation cap, ResourceLocation core){
		return withCapAndCore(cap.toString(), core.toString());
	}

	public static ItemStack withCapAndCore(Cap cap, Core core){
		return withCapAndCore(cap.getId(), core.getId());
	}

	public int getUseDuration(ItemStack stack){
		return 72000;
	}

	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		if(isInGroup(group)){
			// iron/wooden, silver/dair, gold/greatwood, thaumium/silverwood, void/arcanium
			items.add(withCapAndCoreForCt("iron_cap", "wood_wand"));
			items.add(withCapAndCoreForCt("silver_cap", "dair_wand"));
			items.add(withCapAndCoreForCt("gold_cap", "greatwood_wand"));
			items.add(withCapAndCoreForCt("thaumium_cap", "silverwood_wand"));
			items.add(withCapAndCoreForCt("void_cap", "arcanium_wand"));
		}
	}

	public static ItemStack withCapAndCoreForCt(String cap, String core){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("cap", "arcana:" + cap);
		nbt.putString("core", "arcana:" + core);
		ItemStack stack = new ItemStack(ArcanaItems.STAFF.get(), 1);
		stack.setTag(nbt);
		return stack;
	}

	@OnlyIn(Dist.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag){
		super.addInformation(stack, world, tooltip, flag);
		// Add focus info
		Spell spell = getFocus(stack).getSpell(stack);
		if(spell != null){
			Optional<ITextComponent> name = spell.getName(getFocusData(stack).getCompound("Spell"));
			name.ifPresent(e -> tooltip.add(new TranslationTextComponent("tooltip.arcana.spell", e,
					spell.getSpellCosts().toList().stream()
							.map(AspectStack::getAspect)
							.map(aspect -> I18n.format("aspect." + aspect.name()))
							.collect(Collectors.joining(", ")))));
		}
	}
}
