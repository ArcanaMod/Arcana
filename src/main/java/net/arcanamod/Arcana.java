package net.arcanamod;

import net.arcanamod.aspects.AspectHandlerCapability;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.ArcanaTiles;
import net.arcanamod.client.Sounds;
import net.arcanamod.client.gui.ResearchTableScreen;
import net.arcanamod.client.render.*;
import net.arcanamod.containers.ArcanaContainers;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.ArcanaRecipes;
import net.arcanamod.network.Connection;
import net.arcanamod.research.EntrySection;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.Requirement;
import net.arcanamod.research.ResearchLoader;
import net.arcanamod.research.impls.ResearcherCapability;
import net.arcanamod.spells.SpellEffectHandler;
import net.arcanamod.world.NodeType;
import net.arcanamod.world.impl.NodeChunkCapability;
import net.arcanamod.worldgen.ArcanaFeatures;
import net.arcanamod.worldgen.FeatureGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base Arcana Class
 *
 * @author Atlas
 */
@Mod(Arcana.MODID)
public class Arcana{
	public static final String MODID = "arcana";
	
	public static final Logger logger = LogManager.getLogger("Arcana");
	public static Arcana instance;
	public static ResearchLoader researchManager;
	
	public static ItemGroup ITEMS = new SupplierItemGroup(MODID, () -> new ItemStack(ArcanaBlocks.ARCANE_STONE.get()));
	
	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	public static ItemGroup ASPECTS = new SupplierItemGroup("aspects", proxy::getAspectItemStackForDisplay);

	public static final boolean debug = true;
	
	public Arcana(){
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ArcanaConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ArcanaConfig.CLIENT_SPEC);
		
		// deferred registry registration
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		NodeType.init();

		ArcanaBlocks.BLOCKS.register(modEventBus);
		ArcanaEntities.ENTITY_TYPES.register(modEventBus);
		ArcanaItems.ITEMS.register(modEventBus);
		ArcanaRecipes.SERIALIZERS.register(modEventBus);
		ArcanaTiles.TES.register(modEventBus);
		ArcanaContainers.CON.register(modEventBus);
		ArcanaFeatures.FEATURES.register(modEventBus);
		// ArcanaRecipes.RECIPE_SERIALIZERS.register(modEventBus);
		// etc

		proxy.construct();
	}
	
	public static ResourceLocation arcLoc(String path){
		return new ResourceLocation(MODID, path);
	}
	
	private void setup(FMLCommonSetupEvent event){
		// init, init, init, init, init, init, init, init
		EntrySection.init();
		Requirement.init();
		ResearcherCapability.init();
		AspectHandlerCapability.init();
		NodeChunkCapability.init();
		Puzzle.init();
		
		proxy.preInit(event);

		Connection.init();
		//NetworkRegistry.INSTANCE.registerGuiHandler(Arcana.instance, new ArcanaGuiHandler());
		
		Sounds.registerSounds();
		SpellEffectHandler.init();

		FeatureGenerator.setupFeatureGeneration();
	}
	
	private void setupClient(FMLClientSetupEvent event){
		// TODO: move to ClientProxy for servers

		//Render Layers for Blocks
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.RESEARCH_TABLE.get(), RenderType.getTranslucent());
		
		//RenderTypeLookup.setRenderLayer(ArcanaBlocks.NORMAL_NODE.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.SILVERWOOD_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.DAIR_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.EUCALYPTUS_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.GREATWOOD_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.HAWTHORN_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.WILLOW_SAPLING.get(), RenderType.getCutout());

		RenderTypeLookup.setRenderLayer(ArcanaBlocks.DAIR_DOOR.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.EUCALYPTUS_DOOR.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.GREATWOOD_DOOR.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.SILVERWOOD_DOOR.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.HAWTHORN_DOOR.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.WILLOW_DOOR.get(), RenderType.getCutout());

		RenderTypeLookup.setRenderLayer(ArcanaBlocks.DAIR_TRAPDOOR.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.SILVERWOOD_TRAPDOOR.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.EUCALYPTUS_TRAPDOOR.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.GREATWOOD_TRAPDOOR.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.HAWTHORN_TRAPDOOR.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.WILLOW_TRAPDOOR.get(), RenderType.getCutout());

		//Tile Entity Special Render
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.JAR_TE.get(), JarTileEntityRender::new);

		//Screens
		ScreenManager.registerFactory(ArcanaContainers.REASERCH_TABLE.get(), ResearchTableScreen::new);

		//Special Render
		ModelLoader.addSpecialModel(new ResourceLocation(MODID,"item/phial"));

		//Entity Render
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.KOALA_ENTITY.get(), KoalaEntityRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.DAIR_SPIRIT.get(), DairSpiritRenderer::new);

		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.NODE_PARTICLE.get(), new NodeParticle.Factory());
		Minecraft.getInstance().particles.registerFactory(ArcanaParticles.ASPECT_PARTICLE.get(), new AspectParticle.Factory());
	}
	
	private void enqueueIMC(InterModEnqueueEvent event){
		// tell curios or whatever about our baubles
	}
	
	private void processIMC(InterModProcessEvent event){
		// handle aspect registration from addons?
	}
}