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
import net.minecraft.world.storage.loot.functions.CopyName;
import net.minecraft.world.storage.loot.functions.CopyNbt;
import net.minecraft.world.storage.loot.functions.SetContents;
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
		addStandardTable("dair_fence_gate", DAIR_FENCE_GATE);
		addStandardTable("dead_fence_gate", DEAD_FENCE_GATE);
		addStandardTable("eucalyptus_fence_gate", EUCALYPTUS_FENCE_GATE);
		addStandardTable("hawthorn_fence_gate", HAWTHORN_FENCE_GATE);
		addStandardTable("greatwood_fence_gate", GREATWOOD_FENCE_GATE);
		addStandardTable("silverwood_fence_gate", SILVERWOOD_FENCE_GATE);
		addStandardTable("trypophobius_fence_gate", TRYPOPHOBIUS_FENCE_GATE);
		addStandardTable("willow_fence_gate", WILLOW_FENCE_GATE);
		
		Map<ResourceLocation, LootTable> tables = new HashMap<>();
		for(Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet())
			tables.put(entry.getKey().getLootTable(), entry.getValue().setParameterSet(LootParameterSets.BLOCK).build());
		writeTables(cache, tables);
	}
	
	protected void addStandardTable(String name, @Nonnull Supplier<? extends Block> block){
		addStandardTable(name, block.get());
	}
	
	protected void addStandardTable(String name, Block block){
		lootTables.put(block, createStandardTable(name, block));
	}
	
	protected LootTable.Builder createStandardTable(String name, Block block){
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