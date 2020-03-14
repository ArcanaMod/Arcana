package net.kineticdevelopment.arcana.core.research;

import com.google.gson.*;
import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.common.network.PktSyncBooks;
import net.kineticdevelopment.arcana.core.research.impls.ResearchEntryImpl;
import net.minecraft.item.Item;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * TODO: once we update to 1.14, change this to use JsonReloadManager so we can use datapacks instead.
 */
public class ResearchLoader{
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static Map<ResourceLocation, JsonArray> bookQueue = new LinkedHashMap<>();
	private static Map<ResourceLocation, JsonArray> categoryQueue = new LinkedHashMap<>();
	private static Map<ResourceLocation, JsonArray> entryQueue = new LinkedHashMap<>();
	
	public static void applyJson(String json, ResourceLocation rl){
		JsonObject jsonObject = JsonUtils.fromJson(GSON, json, JsonObject.class, false);
		if(jsonObject != null)
			applyJson(jsonObject, rl);
	}
	
	private static void applyBooksArray(ResourceLocation rl, JsonArray books){
		for(JsonElement bookElement : books){
			if(!bookElement.isJsonObject())
				LOGGER.error("Non-object found in books array in " + rl + "!");
			else{
				JsonObject book = bookElement.getAsJsonObject();
				// expecting key, prefix
				ResourceLocation key = new ResourceLocation(book.get("key").getAsString());
				String prefix = book.get("prefix").getAsString();
				ResearchBook bookObject = new ResearchBook(key, new LinkedHashMap<>(), prefix);
				ServerBooks.books.putIfAbsent(key, bookObject);
			}
		}
	}
	
	private static void applyCategoriesArray(ResourceLocation rl, JsonArray categories){
		for(JsonElement categoryElement : categories){
			if(!categoryElement.isJsonObject())
				LOGGER.error("Non-object found in categories array in " + rl + "!");
			else{
				JsonObject category = categoryElement.getAsJsonObject();
				// expecting key, in, icon, (later) background
				ResourceLocation key = new ResourceLocation(category.get("key").getAsString());
				ResourceLocation icon = new ResourceLocation(category.get("icon").getAsString());
				String name = category.get("name").getAsString();
				icon = new ResourceLocation(icon.getResourceDomain(), "textures/" + icon.getResourcePath());
				ResearchBook in = ServerBooks.books.get(new ResourceLocation(category.get("in").getAsString()));
				ResearchCategory categoryObject = new ResearchCategory(new LinkedHashMap<>(), key, icon, name, in);
				in.categories.putIfAbsent(key, categoryObject);
			}
		}
	}
	
	private static void applyEntriesArray(ResourceLocation rl, JsonArray entries){
		for(JsonElement entryElement : entries){
			if(!entryElement.isJsonObject())
				LOGGER.error("Non-object found in entries array in " + rl + "!");
			else{
				JsonObject entry = entryElement.getAsJsonObject();
				
				// expecting key, name, desc, icons, category, x, y, sections
				ResourceLocation key = new ResourceLocation(entry.get("key").getAsString());
				String name = entry.get("name").getAsString();
				String desc = entry.get("desc").getAsString();
				List<Item> icons = idsToItems(entry.getAsJsonArray("icons"));
				ResearchCategory category = ServerBooks.getCategory(new ResourceLocation(entry.get("category").getAsString()));
				int x = entry.get("x").getAsInt();
				int y = entry.get("y").getAsInt();
				List<EntrySection> sections = jsonToSections(entry.getAsJsonArray("sections"), rl);
				
				// optionally parents, meta
				List<ResourceLocation> parents = new ArrayList<>();
				if(entry.has("parents"))
					parents = StreamSupport.stream(entry.getAsJsonArray("meta").spliterator(), false).map(JsonElement::getAsString).map(ResourceLocation::new).collect(Collectors.toList());
				
				List<String> meta = new ArrayList<>();
				if(entry.has("meta"))
					meta = StreamSupport.stream(entry.getAsJsonArray("meta").spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toList());
				
				ResearchEntry entryObject = new ResearchEntryImpl(key, sections, icons, meta, parents, category, name, desc, x, y);
				category.entries.putIfAbsent(key, entryObject);
			}
		}
	}
	
	public static void applyJson(JsonObject json, ResourceLocation rl){
		// LOGGER.info(json);
		if(json.has("books")){
			JsonArray books = json.getAsJsonArray("books");
			bookQueue.put(rl, books);
		}
		if(json.has("categories")){
			JsonArray categories = json.getAsJsonArray("categories");
			categoryQueue.put(rl, categories);
		}
		if(json.has("entries")){
			JsonArray entries = json.getAsJsonArray("entries");
			entryQueue.put(rl, entries);
		}
	}
	
	private static List<Item> idsToItems(JsonArray itemIds){
		return StreamSupport.stream(itemIds.spliterator(), false)
				.map(element -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(element.getAsString())))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	
	private static List<EntrySection> jsonToSections(JsonArray sections, ResourceLocation file){
		List<EntrySection> ret = new ArrayList<>();
		for(JsonElement sectionElement : sections){
			// expecting type, content
			if(sectionElement.isJsonObject()){
				JsonObject section = sectionElement.getAsJsonObject();
				String type = section.get("type").getAsString();
				String content = section.get("content").getAsString();
				EntrySection es = EntrySection.makeSection(type, content);
				if(es != null)
					ret.add(es);
				else if(EntrySection.getFactory(type) == null)
					LOGGER.error("Invalid EntrySection type \"" + type + "\" referenced in " + file + "!");
				else
					LOGGER.error("Invalid EntrySection content \"" + content + "\" for type \"" + type + "\" used in file " + file + "!");
			}else
				LOGGER.error("Non-object found in sections array in " + file + "!");
		}
		return ret;
	}
	
	public static void load(){
		for(ModContainer mod : Loader.instance().getActiveModList()){
			CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/research", /*pre-processing?*/ null, (root, file) -> {
				String relative = root.relativize(file).toString();
				if(!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
					return true;
				
				String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
				ResourceLocation key = new ResourceLocation(mod.getModId(), name);
				
				BufferedReader reader = null;
				
				try{
					reader = Files.newBufferedReader(file);
					String contents = IOUtils.toString(reader);
					applyJson(contents, key);
				}catch(JsonParseException jsonparseexception){
					LOGGER.error("Parsing error loading research " + key, jsonparseexception);
					return false;
				}catch(IOException ioexception){
					LOGGER.error("Couldn't read research " + key + " from " + file, ioexception);
					return false;
				}finally{
					IOUtils.closeQuietly(reader);
				}
				
				return false;
				}, true, true);
		}
		
		bookQueue.forEach(ResearchLoader::applyBooksArray);
		categoryQueue.forEach(ResearchLoader::applyCategoriesArray);
		entryQueue.forEach(ResearchLoader::applyEntriesArray);
		
		if(FMLCommonHandler.instance().getMinecraftServerInstance() != null)
			Connection.network.sendToAll(new PktSyncBooks());
	}
}