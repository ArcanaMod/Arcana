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
		
		addStandardTable(INFUSION_PEDESTAL);
		addStandardTable(ARCANE_CRAFTING_TABLE);
		addStandardTable(SEE_NO_EVIL_STATUE);
		addStandardTable(HEAR_NO_EVIL_STATUE);
		addStandardTable(SPEAK_NO_EVIL_STATUE);
		addStandardTable(SILVER_BLOCK);
		addStandardTable(SILVER_ORE);
		addStandardTable(VOID_METAL_BLOCK);
		
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