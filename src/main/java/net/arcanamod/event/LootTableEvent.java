package net.arcanamod.event;

import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.ArtifactItem;
import net.arcanamod.worldgen.LootTableGroups;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class LootTableEvent {
	@SubscribeEvent
	public static void lootTables(LootTableLoadEvent event){
		if(event.getName().getPath().contains("chests")) {
			Random rnd = new Random();
			LootPool.Builder pool = LootPool.builder().name("arcana_artifacts");
			for (ArtifactItem artifact : ArcanaItems._ARTIFACTS) {
				if (artifact.isChestLootable()&&itemIsLootableInCertainChest(event.getName(),artifact))
					pool.addEntry(ItemLootEntry.builder(artifact)).rolls(RandomValueRange.of(1,2)).build();
			}
			event.getTable().addPool(pool.build());
		}
	}

	public static boolean itemIsLootableInCertainChest(ResourceLocation location, ArtifactItem item){
		for (LootTableGroups group : LootTableGroups.values()){
			for (ResourceLocation lootTable : group.lootTables){
				if (lootTable == location)
					return item.isChestLootable(group);
			}
		}
		return false;
	}
}
