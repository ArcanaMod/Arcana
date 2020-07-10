package net.arcanamod.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.arcanamod.blocks.ArcanaBlocks.*;

public class LootTables extends LootTableProvider{
	
	private DataGenerator generator;
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
	
	public LootTables(DataGenerator dataGenerator){
		super(dataGenerator);
		generator = dataGenerator;
	}
	
	// TODO: check if there's a better way of doing this
	
	public void act(@Nonnull DirectoryCache cache){
		addStandardTable(DAIR_FENCE_GATE);
		addStandardTable(DEAD_FENCE_GATE);
		addStandardTable(EUCALYPTUS_FENCE_GATE);
		addStandardTable(HAWTHORN_FENCE_GATE);
		addStandardTable(GREATWOOD_FENCE_GATE);
		addStandardTable(SILVERWOOD_FENCE_GATE);
		addStandardTable(TRYPOPHOBIUS_FENCE_GATE);
		addStandardTable(WILLOW_FENCE_GATE);
		
		addStandardTable(DUNGEON_BRICKS);
		addStandardTable(DUNGEON_BRICKS_SLAB);
		addStandardTable(DUNGEON_BRICKS_STAIRS);
		addStandardTable(DUNGEON_BRICKS_PRESSURE_PLATE);
		addStandardTable(DUNGEON_BRICKS_WALL);
		
		addStandardTable(MOSSY_DUNGEON_BRICKS);
		addStandardTable(MOSSY_DUNGEON_BRICKS_SLAB);
		addStandardTable(MOSSY_DUNGEON_BRICKS_STAIRS);
		addStandardTable(MOSSY_DUNGEON_BRICKS_PRESSURE_PLATE);
		addStandardTable(MOSSY_DUNGEON_BRICKS_WALL);
		
		addStandardTable(CRACKED_DUNGEON_BRICKS);
		addStandardTable(CRACKED_DUNGEON_BRICKS_SLAB);
		addStandardTable(CRACKED_DUNGEON_BRICKS_STAIRS);
		addStandardTable(CRACKED_DUNGEON_BRICKS_PRESSURE_PLATE);
		addStandardTable(CRACKED_DUNGEON_BRICKS_WALL);

		addStandardTable(PRIDEFUL_GOLD_PILLAR);
		addStandardTable(PRIDESTONE);
		addStandardTable(PRIDESTONE_BRICKS);
		addStandardTable(PRIDESTONE_PILLAR);
		addStandardTable(PRIDESTONE_PILLAR_COAL);
		addStandardTable(PRIDESTONE_SMALL_BRICKS);
		addStandardTable(PRIDESTONE_TILE);
		addStandardTable(SMOOTH_PRIDESTONE);
		addStandardTable(SMOOTH_PRIDESTONE_TILE);
		addStandardTable(ROUGH_LIMESTONE);
		addStandardTable(SMOOTH_LIMESTONE);
		addStandardTable(LIMESTONE_TILE);
		addStandardTable(PRIDECLAY);
		addStandardTable(GILDED_PRIDECLAY);
		addStandardTable(PRIDEFUL_GOLD_BLOCK);
		addStandardTable(PRIDEFUL_GOLD_TILE);
		addStandardTable(CARVED_PRIDEFUL_GOLD_BLOCK);
		addStandardTable(CHISELED_PRIDEFUL_GOLD_BLOCK);
		
		addStandardTable(PEDESTAL);
		addStandardTable(ARCANE_CRAFTING_TABLE);
		addStandardTable(SEE_NO_EVIL_STATUE);
		addStandardTable(HEAR_NO_EVIL_STATUE);
		addStandardTable(SPEAK_NO_EVIL_STATUE);
		addStandardTable(SILVER_BLOCK);
		addStandardTable(SILVER_ORE);
		addStandardTable(VOID_METAL_BLOCK);

		addStandardTable(TAINTED_OAK_LOG);
		addStandardTable(TAINTED_OAK_PLANKS);
		addStandardTable(TAINTED_OAK_SAPLING);
		addStandardTable(TAINTED_OAK_SLAB);
		addStandardTable(TAINTED_OAK_STAIRS);

		addStandardTable(TAINTED_SPRUCE_LOG);
		addStandardTable(TAINTED_SPRUCE_PLANKS);
		addStandardTable(TAINTED_SPRUCE_SAPLING);
		addStandardTable(TAINTED_SPRUCE_SLAB);
		addStandardTable(TAINTED_SPRUCE_STAIRS);

		addStandardTable(TAINTED_JUNGLE_LOG);
		addStandardTable(TAINTED_JUNGLE_PLANKS);
		addStandardTable(TAINTED_JUNGLE_SAPLING);
		addStandardTable(TAINTED_JUNGLE_SLAB);
		addStandardTable(TAINTED_JUNGLE_STAIRS);

		addStandardTable(TAINTED_WILLOW_LOG);
		addStandardTable(TAINTED_WILLOW_PLANKS);
		addStandardTable(TAINTED_WILLOW_SAPLING);
		addStandardTable(TAINTED_WILLOW_SLAB);
		addStandardTable(TAINTED_WILLOW_STAIRS);

		addStandardTable(TAINTED_HAWTHORN_LOG);
		addStandardTable(TAINTED_HAWTHORN_PLANKS);
		addStandardTable(TAINTED_HAWTHORN_SAPLING);
		addStandardTable(TAINTED_HAWTHORN_SLAB);
		addStandardTable(TAINTED_HAWTHORN_STAIRS);

		addStandardTable(TAINTED_GREATWOOD_LOG);
		addStandardTable(TAINTED_GREATWOOD_PLANKS);
		addStandardTable(TAINTED_GREATWOOD_SAPLING);
		addStandardTable(TAINTED_GREATWOOD_SLAB);
		addStandardTable(TAINTED_GREATWOOD_STAIRS);

		addStandardTable(TAINTED_DARKOAK_LOG);
		addStandardTable(TAINTED_DARKOAK_PLANKS);
		addStandardTable(TAINTED_DARKOAK_SAPLING);
		addStandardTable(TAINTED_DARKOAK_SLAB);
		addStandardTable(TAINTED_DARKOAK_STAIRS);

		addStandardTable(TAINTED_BIRCH_LOG);
		addStandardTable(TAINTED_BIRCH_PLANKS);
		addStandardTable(TAINTED_BIRCH_SAPLING);
		addStandardTable(TAINTED_BIRCH_SLAB);
		addStandardTable(TAINTED_BIRCH_STAIRS);

		addStandardTable(TAINTED_ACACIA_LOG);
		addStandardTable(TAINTED_ACACIA_PLANKS);
		addStandardTable(TAINTED_ACACIA_SAPLING);
		addStandardTable(TAINTED_ACACIA_SLAB);
		addStandardTable(TAINTED_ACACIA_STAIRS);

		addStandardTable(TAINTED_DAIR_LOG);
		addStandardTable(TAINTED_DAIR_PLANKS);
		addStandardTable(TAINTED_DAIR_SAPLING);
		addStandardTable(TAINTED_DAIR_SLAB);
		addStandardTable(TAINTED_DAIR_STAIRS);

		addStandardTable(TAINTED_COAL_BLOCK);
		addStandardTable(TAINTED_IRON_BLOCK);
		addStandardTable(TAINTED_THAUMIUM_BLOCK);
		addStandardTable(TAINTED_LAPIS_BLOCK);
		addStandardTable(TAINTED_REDSTONE_BLOCK);
		addStandardTable(TAINTED_GOLD_BLOCK);
		addStandardTable(TAINTED_DIAMOND_BLOCK);
		addStandardTable(TAINTED_EMERALD_BLOCK);
		addStandardTable(TAINTED_ARCANIUM_BLOCK);

		addStandardTable(TAINTED_GRAVEL);
		addStandardTable(TAINTED_PUMPKIN);
		addStandardTable(TAINTED_CARVED_PUMPKIN);
		addStandardTable(TAINTED_JACK_OLANTERN);
		addStandardTable(TAINTED_SAND);
		addStandardTable(TAINTED_SOIL);
		addStandardTable(TAINTED_ROCK);
		addStandardTable(TAINTED_ROCK_SLAB);
		addStandardTable(TAINTED_CRUST);
		addStandardTable(TAINTED_CRUST_SLAB);

		addStandardTable(TAINTED_FLOWER);

		Map<ResourceLocation, LootTable> tables = new HashMap<>();
		for(Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet())
			tables.put(entry.getKey().getLootTable(), entry.getValue().setParameterSet(LootParameterSets.BLOCK).build());
		writeTables(cache, tables);
	}
	
	protected void addStandardTable(@Nonnull Supplier<? extends Block> block){
		addStandardTable(block.get());
	}
	
	protected void addStandardTable(Block block){
		lootTables.put(block, createStandardTable(block));
	}
	
	protected LootTable.Builder createStandardTable(Block block){
		LootPool.Builder builder = LootPool.builder()
				.rolls(ConstantRange.of(1))
				.addEntry(ItemLootEntry.builder(block))
				.acceptCondition(SurvivesExplosion.builder());
		return LootTable.builder().addLootPool(builder);
	}
	
	private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables){
		Path outputFolder = generator.getOutputFolder();
		tables.forEach((key, lootTable) -> {
			Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
			try{
				IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
			}catch(IOException e){
				LOGGER.error("Couldn't write loot table {}", path, e);
			}
		});
	}
	
	@Nonnull
	public String getName(){
		return "Arcana Loot Tables";
	}
}