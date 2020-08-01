package net.arcanamod.items;

import net.arcanamod.Arcana;
import net.arcanamod.worldgen.LootTableGroups;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ArtifactItem extends Item {
	private final List<LootTableGroups> chestLootable;

	protected ArtifactItem(List<LootTableGroups> chestLootable) {
		super(new Item.Properties().group(Arcana.ITEMS).rarity(Rarity.UNCOMMON));
		this.chestLootable = chestLootable;
	}

	public static ArtifactItem create(LootTableGroups... lootTables){
		ArtifactItem artifact = new ArtifactItem(Arrays.asList(lootTables));
		ArcanaItems._ARTIFACTS.add(artifact);
		return artifact;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent("tooltip.artifact").applyTextStyle(TextFormatting.GRAY));
	}

	public boolean isChestLootable(){return chestLootable.contains(LootTableGroups.ORE);}

	public boolean isChestLootable(LootTableGroups lootTable){
		return chestLootable.contains(lootTable)||chestLootable.contains(LootTableGroups.EVERYWHERE);
	}
}
