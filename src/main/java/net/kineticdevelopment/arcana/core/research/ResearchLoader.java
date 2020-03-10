package net.kineticdevelopment.arcana.core.research;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;

/**
 * TODO: once we update to 1.14 - which I hope is soon - change this to use JsonReloadManager so we can use datapacks instead.
 * If there's a way to implement that now, feel free to do so.
 */
public class ResearchLoader{
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void applyJson(String json, ResourceLocation rl){
		applyJson((JsonObject)JsonUtils.fromJson(GSON, json, JsonObject.class, false), rl);
	}
	
	public static void applyJson(JsonObject json, ResourceLocation rl){
		LOGGER.info(json);
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
	}
}