package net.arcanamod.datagen;

import com.google.common.collect.Maps;
import net.arcanamod.Arcana;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.Map;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ArcanaDataGenerators{
	
	public static ResourceLocation DAIR = new ResourceLocation(Arcana.MODID, "block/dair_planks");
	public static ResourceLocation DEAD = new ResourceLocation(Arcana.MODID, "block/dead_planks");
	public static ResourceLocation EUCALYPTUS = new ResourceLocation(Arcana.MODID, "block/eucalyptus_planks");
	public static ResourceLocation HAWTHORN = new ResourceLocation(Arcana.MODID, "block/hawthorn_planks");
	public static ResourceLocation GREATWOOD = new ResourceLocation(Arcana.MODID, "block/greatwood_planks");
	public static ResourceLocation SILVERWOOD = new ResourceLocation(Arcana.MODID, "block/silverwood_planks");
	public static ResourceLocation TRYPOPHOBIUS = new ResourceLocation(Arcana.MODID, "block/trypophobius_planks");
	public static ResourceLocation WILLOW = new ResourceLocation(Arcana.MODID, "block/willow_planks");
	
	public static Map<String, ResourceLocation> LIVING_WOODS = Maps.newHashMap();
	public static Map<String, ResourceLocation> WOODS = Maps.newHashMap();
	
	static{
		LIVING_WOODS.put("dair", DAIR);
		LIVING_WOODS.put("eucalyptus", EUCALYPTUS);
		LIVING_WOODS.put("hawthorn", HAWTHORN);
		LIVING_WOODS.put("greatwood", GREATWOOD);
		LIVING_WOODS.put("silverwood", SILVERWOOD);
		LIVING_WOODS.put("willow", WILLOW);
		WOODS.putAll(LIVING_WOODS);
		WOODS.put("trypophobius", TRYPOPHOBIUS);
		WOODS.put("dead", DEAD);
	}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event){
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper efh = event.getExistingFileHelper();
		if(event.includeClient()){
			generator.addProvider(new BlockModels(generator, efh));
			generator.addProvider(new Blockstates(generator, efh));
			generator.addProvider(new ItemModels(generator, efh));
		}
		if(event.includeServer()){
			generator.addProvider(new LootTables(generator));
		}
	}
}