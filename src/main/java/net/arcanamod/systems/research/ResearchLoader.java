package net.arcanamod.systems.research;

import com.google.gson.*;
import net.arcanamod.systems.research.impls.ItemRequirement;
import net.arcanamod.systems.research.impls.ItemTagRequirement;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
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
 */
@ParametersAreNonnullByDefault
public class ResearchLoader extends JsonReloadListener {
	
	private static final Logger LOGGER = LogManager.getLogger();
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	
	private static Map<ResourceLocation, JsonArray> bookQueue = new LinkedHashMap<>();
	private static Map<ResourceLocation, JsonArray> categoryQueue = new LinkedHashMap<>();
	private static Map<ResourceLocation, JsonArray> entryQueue = new LinkedHashMap<>();
	private static Map<ResourceLocation, JsonArray> puzzleQueue = new LinkedHashMap<>();
	
	public ResearchLoader(){
		super(GSON, "arcana/research");
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
				LOGGER.info("Loaded book " + key);
			}
		}
	}
	
	private static void applyCategoriesArray(ResourceLocation rl, JsonArray categories){
		for(JsonElement categoryElement : categories){
			if(!categoryElement.isJsonObject())
				LOGGER.error("Non-object found in categories array in " + rl + "!");
			else{
				JsonObject category = categoryElement.getAsJsonObject();
				// expecting key, in, icon, bg, optionally bgs
				ResourceLocation key = new ResourceLocation(category.get("key").getAsString());
				ResourceLocation bg = new ResourceLocation(category.get("bg").getAsString());
				bg = new ResourceLocation(bg.getNamespace(), "textures/" + bg.getPath());
				ResourceLocation icon = new ResourceLocation(category.get("icon").getAsString());
				icon = new ResourceLocation(icon.getNamespace(), "textures/" + icon.getPath());
				String name = category.get("name").getAsString();
				ResourceLocation requirement = category.has("requires") ? new ResourceLocation(category.get("requires").getAsString()) : null;
				ResearchBook in = ResearchBooks.books.get(new ResourceLocation(category.get("in").getAsString()));
				ResearchCategory categoryObject = new ResearchCategory(new LinkedHashMap<>(), key, icon, bg, requirement, name, in);
				if(category.has("bgs")){
					JsonArray layers = category.getAsJsonArray("bgs");
					for(JsonElement layerElem : layers){
						JsonObject layerObj = layerElem.getAsJsonObject();
						BackgroundLayer layer = BackgroundLayer.makeLayer(
								new ResourceLocation(layerObj.getAsJsonPrimitive("type").getAsString()),
								layerObj,
								rl,
								layerObj.getAsJsonPrimitive("speed").getAsFloat(),
								layerObj.has("vanishZoom") ? layerObj.getAsJsonPrimitive("vanishZoom").getAsFloat() : -1);
						if(layer != null)
							categoryObject.getBgs().add(layer);
					}
				}
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
				List<Icon> icons = idsToIcons(entry.getAsJsonArray("icons"), rl);
				ResearchCategory category = ResearchBooks.getCategory(new ResourceLocation(entry.get("category").getAsString()));
				int x = entry.get("x").getAsInt();
				int y = entry.get("y").getAsInt();
				List<EntrySection> sections = jsonToSections(entry.getAsJsonArray("sections"), rl);
				
				// optionally parents, meta
				List<Parent> parents = new ArrayList<>();
				if(entry.has("parents"))
					parents = StreamSupport.stream(entry.getAsJsonArray("parents").spliterator(), false).map(JsonElement::getAsString).map(Parent::parse).collect(Collectors.toList());
				
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
	
	private static List<Icon> idsToIcons(JsonArray itemIds, ResourceLocation rl){
		List<Icon> ret = new ArrayList<>();
		for(JsonElement element : itemIds){
			ret.add(Icon.fromString(element.getAsString()));
		}
		if(ret.isEmpty())
			LOGGER.error("An entry has 0 icons in " + rl + "!");
		return ret;
	}
	
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
				// If this has a "{" it has parameters; remove those
				if(desc.contains("{") && desc.endsWith("}")){
					String[] param_parts = desc.split("\\{", 2);
					desc = param_parts[0];
					params = Arrays.asList(param_parts[1].substring(0, param_parts[1].length() - 1).split(", "));
				}
				// If this has "::" it's a custom requirement
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
				// if this begins with a hash
				}else if(desc.startsWith("#")){
					// its a tag
					ResourceLocation itemTagLoc = new ResourceLocation(desc.substring(1));
					ITag<Item> itemTag = ItemTags.getCollection().get(itemTagLoc);
					if(itemTag != null){
						ItemTagRequirement tagReq = new ItemTagRequirement(itemTag, itemTagLoc);
						tagReq.amount = amount;
						ret.add(tagReq);
					}else
						LOGGER.error("Invalid item tag " + itemTagLoc + " found in file " + file + "!");
				}else{
					// its an item
					ResourceLocation item = new ResourceLocation(desc);
					Item value = ForgeRegistries.ITEMS.getValue(item);
					if(value != null){
						ItemRequirement add = new ItemRequirement(value);
						add.amount = amount;
						ret.add(add);
					}else
						LOGGER.error("Invalid item " + item + " found in file " + file + "!");
				}
			}
		}
		return ret;
	}
	
	protected void apply(Map<ResourceLocation, JsonElement> object, IResourceManager resourceManager, IProfiler profiler){
		bookQueue.clear();
		categoryQueue.clear();
		entryQueue.clear();
		puzzleQueue.clear();
		
		object.forEach((location, object1) -> {
			if(object1.isJsonObject())
				applyJson(object1.getAsJsonObject(), location);
		});
		
		bookQueue.forEach(ResearchLoader::applyBooksArray);
		categoryQueue.forEach(ResearchLoader::applyCategoriesArray);
		entryQueue.forEach(ResearchLoader::applyEntriesArray);
		puzzleQueue.forEach(ResearchLoader::applyPuzzlesArray);
	}
}