package net.arcanamod.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.arcanamod.util.Pair;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Map;

import static net.arcanamod.world.WorldInteractions.freezable;

public class WorldInteractionsRegistry extends JsonReloadListener{
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	
	public WorldInteractionsRegistry(){
		super(GSON, "arcana/interactions");
	}
	
	public static void applyJson(ResourceLocation location, JsonElement e){
		if(e.isJsonObject()){
			JsonObject object = e.getAsJsonObject();
			if(location.getPath().equals("freeze"))
				for(JsonElement element : object.get("values").getAsJsonArray()){
					String from = element.getAsJsonObject().get("from").getAsString();
					String to = element.getAsJsonObject().get("to").getAsString();
					String cover = "minecraft:air";
					if(element.getAsJsonObject().has("cover"))
						cover = element.getAsJsonObject().get("cover").getAsString();
					
					freezable.put(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(from)),
							Pair.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(to)),
									ForgeRegistries.BLOCKS.getValue(new ResourceLocation(cover))));
				}
		}
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objects, @Nonnull IResourceManager resourceManager, @Nonnull IProfiler profiler){
		objects.forEach(WorldInteractionsRegistry::applyJson);
	}
}
