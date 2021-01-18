package net.arcanamod.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.arcanamod.util.Pair;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

import static net.arcanamod.world.WorldInteractions.freezable;

public class WorldInteractionsRegistry extends JsonReloadListener {

	private static final Logger LOGGER = LogManager.getLogger();
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static boolean processing = false;

	private MinecraftServer server;

	public WorldInteractionsRegistry(MinecraftServer server){
		super(GSON, "arcana/interactions");
		this.server = server;
	}

	public static void applyJson(ResourceLocation location, JsonObject object){
		if (location.getPath().equals("freeze")) {
			for (JsonElement element : object.get("values").getAsJsonArray()) {
				String from = element.getAsJsonObject().get("from").getAsString();
				String to = element.getAsJsonObject().get("to").getAsString();
				String cover = "minecraft:air";
				if (element.getAsJsonObject().has("cover")) {
					cover = element.getAsJsonObject().get("cover").getAsString();
				}
				LOGGER.info("Added freezable block:" + from);

				freezable.put(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(from)),
						Pair.of(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(to)),
								ForgeRegistries.BLOCKS.getValue(new ResourceLocation(cover))));
			}
		}
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonObject> objects, IResourceManager resourceManagerIn, IProfiler profilerIn) {
		processing = true;

		objects.forEach((r,o) -> WorldInteractionsRegistry.applyJson(r,o));

		processing = false;
	}
}
