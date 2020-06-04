package net.arcanamod;

import net.arcanamod.aspects.VisHandlerCapability;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.ArcanaTiles;
import net.arcanamod.client.Sounds;
import net.arcanamod.client.event.TextureStitch;
import net.arcanamod.client.model.WandModelLoader;
import net.arcanamod.client.render.JarTileEntityRender;
import net.arcanamod.event.WorldTickHandler;
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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
	
	public Arcana(){
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(TextureStitch::onTextureStitch);
		
		// TODO: split into server and common config?
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ArcanaConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ArcanaConfig.CLIENT_SPEC);
		
		// deferred registry registration
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		NodeType.init();
		
		ArcanaBlocks.BLOCKS.register(modEventBus);
		ArcanaItems.ITEMS.register(modEventBus);
		ArcanaRecipes.SERIALIZERS.register(modEventBus);
		ArcanaTiles.TES.register(modEventBus);
		ArcanaFeatures.FEATURES.register(modEventBus);
		// ArcanaRecipes.RECIPE_SERIALIZERS.register(modEventBus);
		// etc
	}
	
	public static ResourceLocation arcLoc(String path){
		return new ResourceLocation(MODID, path);
	}
	
	private void setup(FMLCommonSetupEvent event){
		// init, init, init, init, init, init, init, init
		EntrySection.init();
		Requirement.init();
		ResearcherCapability.init();
		VisHandlerCapability.init();
		NodeChunkCapability.init();
		Puzzle.init();
		
		proxy.preInit(event);
		
		MinecraftForge.EVENT_BUS.register(WorldTickHandler.instance);
		
		Connection.init();
		ModelLoaderRegistry.registerLoader(new ResourceLocation(MODID, "wand_loader"), new WandModelLoader());
		//NetworkRegistry.INSTANCE.registerGuiHandler(Arcana.instance, new ArcanaGuiHandler());
		
		Sounds.registerSounds();
		SpellEffectHandler.init();

		FeatureGenerator.setupFeatureGeneraton();
	}
	
	private void setupClient(FMLClientSetupEvent event){
		// TODO: move to ClientProxy for servers
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.RESEARCH_TABLE.get(), RenderType.getTranslucent());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.NORMAL_NODE.get(), RenderType.getCutout());
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
		
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.JAR_TE.get(), JarTileEntityRender::new);

		ModelLoader.addSpecialModel(new ResourceLocation(MODID,"item/phial"));
	}
	
	private void enqueueIMC(InterModEnqueueEvent event){
		// tell curios or whatever about our baubles
	}
	
	private void processIMC(InterModProcessEvent event){
		// handle aspect registration from addons?
	}
}