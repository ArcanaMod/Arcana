package net.kineticdevelopment.arcana.core.research;

import com.google.gson.*;
import net.kineticdevelopment.arcana.common.network.Connection;
import net.kineticdevelopment.arcana.common.network.PktSyncBooksHandler;
import net.kineticdevelopment.arcana.core.research.impls.ItemRequirement;
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
 * This class handles loading research entries, categories, and books from JSON files. Specifically, it looks
 * for JSON files in assets/&lt;modid&gt;/research/, and looks for arrays named "books", "categories", and "entries"
 * at the root level.
 *
 * <p>This class has some rather verbose logging, since this will probably be a source of problems or
 * confusion for addon makers. (If you don't think its verbose, add more logging.)
 *
 * <p>TODO: once we update to 1.14, change this to use JsonReloadManager so we can use datapacks instead.
 */
public class ResearchLoader{
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static Map<ResourceLocation, JsonArray> bookQueue = new LinkedHashMap<>();
	private static Map<ResourceLocation, JsonArray> categoryQueue = new LinkedHashMap<>();
	private static Map<ResourceLocation, JsonArray> entryQueue = new LinkedHashMap<>();
	private static Map<ResourceLocation, JsonArray> puzzleQueue = new LinkedHashMap<>();
	
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
				ResearchBooks.books.putIfAbsent(key, bookObject);
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
				ResourceLocation bg = new ResourceLocation(category.get("bg").getAsString());
				bg = new ResourceLocation(bg.getResourceDomain(), "textures/" + bg.getResourcePath());
				ResourceLocation icon = new ResourceLocation(category.get("icon").getAsString());
				icon = new ResourceLocation(icon.getResourceDomain(), "textures/" + icon.getResourcePath());
				String name = category.get("name").getAsString();
				ResourceLocation requirement = category.has("requires") ? new ResourceLocation(category.get("requires").getAsString()) : null;
				ResearchBook in = ResearchBooks.books.get(new ResourceLocation(category.get("in").getAsString()));
				ResearchCategory categoryObject = new ResearchCategory(new LinkedHashMap<>(), key, icon, bg, requirement, name, in);
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
				String desc = entry.has("desc") ? entry.get("desc").getAsString() : "";
				List<Item> icons = idsToItems(entry.getAsJsonArray("icons"), rl);
				ResearchCategory category = ResearchBooks.getCategory(new ResourceLocation(entry.get("category").getAsString()));
				int x = entry.get("x").getAsInt();
				int y = entry.get("y").getAsInt();
				List<EntrySection> sections = jsonToSections(entry.getAsJsonArray("sections"), rl);
				
				// optionally parents, meta
				List<ResourceLocation> parents = new ArrayList<>();
				if(entry.has("parents"))
					parents = StreamSupport.stream(entry.getAsJsonArray("parents").spliterator(), false).map(JsonElement::getAsString).map(ResourceLocation::new).collect(Collectors.toList());
				
				List<String> meta = new ArrayList<>();
				if(entry.has("meta"))
					meta = StreamSupport.stream(entry.getAsJsonArray("meta").spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toList());
				
				ResearchEntry entryObject = new ResearchEntry(key, sections, icons, meta, parents, category, name, desc, x, y);
				category.entries.putIfAbsent(key, entryObject);
				sections.forEach(section -> section.entry = entryObject.key());
			}
		}
	}
	
	private static void applyPuzzlesArray(ResourceLocation rl, JsonArray puzzles){
		for(JsonElement puzzleElement : puzzles)
			if(!puzzleElement.isJsonObject())
				LOGGER.error("Non-object found in puzzles array in " + rl + "!");
			else{
				JsonObject puzzle = puzzleElement.getAsJsonObject();
				// expecting key, type
				// then I pass the object along
				ResourceLocation key = new ResourceLocation(puzzle.get("key").getAsString());
				String type = puzzle.get("type").getAsString();
				// optionally desc, icon
				ResourceLocation icon = puzzle.has("icon") ? new ResourceLocation(puzzle.get("icon").getAsString()) : null;
				String desc = puzzle.has("desc") ? puzzle.get("desc").getAsString() : null;
				// make it
				Puzzle puzzleObject = Puzzle.makePuzzle(type, desc, key, icon, puzzle, rl);
				if(puzzleObject != null)
					ResearchBooks.puzzles.putIfAbsent(key, puzzleObject);
				else if(Puzzle.getBlank(type) == null)
					LOGGER.error("Invalid Puzzle type \"" + type + "\" in file " + rl + "!");
			}
	}
	
	public static void applyJson(JsonObject json, ResourceLocation rl){
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
		if(json.has("puzzles")){
			JsonArray puzzles = json.getAsJsonArray("puzzles");
			puzzleQueue.put(rl, puzzles);
		}
	}
	
	private static List<Item> idsToItems(JsonArray itemIds, ResourceLocation rl){
		/*return StreamSupport.stream(itemIds.spliterator(), false)
				.map(element -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(element.getAsString())))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());*/
		List<Item> ret = new ArrayList<>();
		for(JsonElement element : itemIds){
			ResourceLocation id = new ResourceLocation(element.getAsString());
			Item icon = ForgeRegistries.ITEMS.getValue(id);
			if(icon == null)
				LOGGER.error("Invalid item \"" + id + "\" found in " + rl + "!");
			else
				ret.add(icon);
		}
		if(ret.isEmpty())
			LOGGER.error("An entry has 0 valid icons in " + rl + "! Trying to open its category will crash the game!");
		return ret;
	}
	
	/*private static List<Guesswork> jsonToGuessworks(JsonArray puzzles, ResourceLocation file){
		List<Guesswork> ret = new ArrayList<>();
		for(JsonElement puzzleElement : puzzles){
			if(puzzleElement.isJsonObject()){
				JsonObject puzzle = puzzleElement.getAsJsonObject();
				// expecting recipe (string), hints (array)
				ResourceLocation recipe = new ResourceLocation(puzzle.get("recipe").getAsString());
				JsonArray hints = puzzle.getAsJsonArray("hints");
				Map<ResourceLocation, String> hintMap = new LinkedHashMap<>();
				for(JsonElement hint : hints){
					if(hint.isJsonPrimitive()){
						String hintSt = hint.getAsString();
						if(hintSt.contains("=")){
							ResourceLocation key = new ResourceLocation(hintSt.split("=")[0]);
							String value = hintSt.split("=")[1];
							hintMap.put(key, value);
						}else
							LOGGER.error("String not containing \"=\" found in puzzle in " + file + "!");
					}else
						LOGGER.error("Non-String found in hints array in puzzle in " + file + "!");
				}
				ret.add(new Guesswork(recipe, hintMap));
			}
		}
		return ret;
	}*/
	
	private static List<EntrySection> jsonToSections(JsonArray sections, ResourceLocation file){
		List<EntrySection> ret = new ArrayList<>();
		for(JsonElement sectionElement : sections)
			if(sectionElement.isJsonObject()){
				// expecting type, content
				JsonObject section = sectionElement.getAsJsonObject();
				String type = section.get("type").getAsString();
				String content = section.get("content").getAsString();
				EntrySection es = EntrySection.makeSection(type, content);
				if(es != null){
					if(section.has("requirements"))
						if(section.get("requirements").isJsonArray()){
							for(Requirement requirement : jsonToRequirements(section.get("requirements").getAsJsonArray(), file))
								if(requirement != null)
									es.addRequirement(requirement);
						}else
							LOGGER.error("Non-array named \"requirements\" found in " + file + "!");
					es.addOwnRequirements();
					ret.add(es);
				}else if(EntrySection.getFactory(type) == null)
					LOGGER.error("Invalid EntrySection type \"" + type + "\" referenced in " + file + "!");
				else
					LOGGER.error("Invalid EntrySection content \"" + content + "\" for type \"" + type + "\" used in file " + file + "!");
			}else
				LOGGER.error("Non-object found in sections array in " + file + "!");
		return ret;
	}
	
	private static List<Requirement> jsonToRequirements(JsonArray requirements, ResourceLocation file){
		List<Requirement> ret = new ArrayList<>();
		for(JsonElement requirementElement : requirements){
			if(requirementElement.isJsonPrimitive()){
				String desc = requirementElement.getAsString();
				int amount = 1;
				// if it has * in it, then its amount is not one
				if(desc.contains("*")){
					String[] parts = desc.split("\\*");
					if(parts.length != 2)
						LOGGER.error("Multiple \"*\"s found in requirement in " + file + "!");
					desc = parts[parts.length - 1];
					amount = Integer.parseInt(parts[0]);
				}
				List<String> params = new ArrayList<>();
				// document this better.
				if(desc.contains("{") && desc.endsWith("}")){
					String[] paramparts = desc.split("\\{");
					if(paramparts.length != 2)
						LOGGER.error("Multiple \"{\"s found in requirement in " + file + "!");
					desc = paramparts[0];
					params = Arrays.asList(paramparts[1].substring(0, paramparts[1].length() - 1).split(", "));
				}
				if(desc.contains("::")){
					String[] parts = desc.split("::");
					if(parts.length != 2)
						LOGGER.error("Multiple \"::\"s found in requirement in " + file + "!");
					ResourceLocation type = new ResourceLocation(parts[0], parts[1]);
					Requirement add = Requirement.makeRequirement(type, params);
					if(add != null){
						add.amount = amount;
						ret.add(add);
					}else
						LOGGER.error("Invalid requirement type " + type + " found in file " + file + "!");
				}else{
					// its an item
					ResourceLocation item = new ResourceLocation(desc);
					ItemRequirement add = new ItemRequirement(ForgeRegistries.ITEMS.getValue(item));
					// a / means meta (intentionally temporary until we upgrade)
					// any other param will be NBT eventually
					// TODO: document properly
					if(params.size() > 0 && params.get(0).startsWith("/")){
						try{
							add.setMeta(Integer.parseInt(params.get(0).substring(1)));
						}catch(NumberFormatException exception){
							LOGGER.error("Tried to set an item requirement's item's meta to a non-integer! (If the first parameter of an item requirement begins with a /, it will be parsed as meta.)");
						}
					}
					add.amount = amount;
					ret.add(add);
				}
			}
		}
		return ret;
	}
	
	public static void load(){
		bookQueue.clear();
		categoryQueue.clear();
		entryQueue.clear();
		puzzleQueue.clear();
		
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
		puzzleQueue.forEach(ResearchLoader::applyPuzzlesArray);
		
		if(FMLCommonHandler.instance().getMinecraftServerInstance() != null)
			Connection.network.sendToAll(new PktSyncBooksHandler.PktSyncBooks(ResearchBooks.books, ResearchBooks.puzzles));
	}
}