package net.arcanamod;

import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.ArcanaTiles;
import net.arcanamod.blocks.tiles.AspectWindowTileEntity;
import net.arcanamod.client.ClientUtils;
import net.arcanamod.client.event.*;
import net.arcanamod.client.gui.*;
import net.arcanamod.client.model.WandModelLoader;
import net.arcanamod.client.model.tainted.TaintedFoxModel;
import net.arcanamod.client.model.tainted.TaintedSheepModel;
import net.arcanamod.client.model.tainted.TaintedWolfModel;
import net.arcanamod.client.model.tainted.TaintedZombieModel;
import net.arcanamod.client.render.*;
import net.arcanamod.client.render.particles.ArcanaParticles;
import net.arcanamod.client.render.tainted.*;
import net.arcanamod.client.render.tiles.*;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.client.research.PuzzleRenderer;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.containers.ArcanaContainers;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.entities.tainted.TaintedEntity;
import net.arcanamod.event.ResearchEvent;
import net.arcanamod.fluids.ArcanaFluids;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.attachment.FocusItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

import static net.arcanamod.Arcana.MODID;
import static net.arcanamod.Arcana.arcLoc;

/**
 * Handles client-side only things (that would otherwise crash dedicated servers).
 */
public class ClientProxy extends CommonProxy{
	
	public static KeyBinding SWAP_FOCUS_BINDING = new KeyBinding("key.arcana.swap_focus", GLFW.GLFW_KEY_G, "key.categories.mod.arcana");
	
	public void construct(){
		super.construct();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ArcanaConfig.CLIENT_SPEC);
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
				() -> (mc, screen) -> new ConfigScreen(screen));
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(TextureStitchHandler::onTextureStitch);
		bus.addListener(BakeEventHandler::onModelBake);
		bus.addListener(ParticleFactoryEvent::onParticleFactoryRegister);
		bus.addListener((ModelRegistryEvent event) -> ModelLoaderRegistry.registerLoader(arcLoc("wand_loader"), new WandModelLoader()));
		
		MinecraftForge.EVENT_BUS.addListener(RenderTooltipHandler::makeTooltip);
		MinecraftForge.EVENT_BUS.addListener(FogHandler::setFogColour);
		MinecraftForge.EVENT_BUS.addListener(FogHandler::setFogDensity);
		MinecraftForge.EVENT_BUS.addListener(FogHandler::setFogLength);
		MinecraftForge.EVENT_BUS.addListener(InitScreenHandler::onInitGuiEvent);
		MinecraftForge.EVENT_BUS.register(ClientTickHandler.class);
		MinecraftForge.EVENT_BUS.register(ParticleFactoryEvent.class);
		
		ArcanaParticles.PARTICLE_TYPES.register(bus);
	}
	
	@Override
	public void preInit(FMLCommonSetupEvent event){
		super.preInit(event);
		setRenderLayers();
		registerScreens();
		registerRenders();
		EntrySectionRenderer.init();
		RequirementRenderer.init();
		PuzzleRenderer.init();
		
		// there's an event for this, but putting it here seems to affect literally nothing. huh.
		// I'm not at all surprised.
		
		ItemModelsProperties.registerProperty(ArcanaItems.PHIAL.get(), new ResourceLocation("aspect"), (itemStack, world, livingEntity) -> {
			IAspectHandler vis = IAspectHandler.getFrom(itemStack);
			if(vis == null)
				return -1;
			if(vis.getHoldersAmount() == 0)
				return -1;
			if(vis.getHolder(0) == null)
				return -1;
			return vis.getHolder(0).getContainedAspect().getId() - 1;
		});
		ItemModelsProperties.registerProperty(ArcanaItems.ARCANUM.get(), new ResourceLocation("open"), (itemStack, world, livingEntity) -> {
			if(!itemStack.getOrCreateTag().contains("open"))
				return 0;
			if(itemStack.getOrCreateTag().getBoolean("open"))
				return 1;
			return 0;
		});
		
		Minecraft inst = Minecraft.getInstance();
		inst.getBlockColors().register((state, access, pos, index) -> {
					if(access == null || pos == null || access.getTileEntity(pos) == null || !(access.getTileEntity(pos) instanceof AspectWindowTileEntity))
						return 0xFF1F0D0B;
					return ((AspectWindowTileEntity)access.getTileEntity(pos)).getColor();
				}, ArcanaBlocks.ASPECT_WINDOW.get()
		);
		
		inst.getBlockColors().register((state, access, pos, index) ->
						access != null && pos != null ? BiomeColors.getWaterColor(access, pos) : -1,
				ArcanaBlocks.CRUCIBLE.get()
		);
		
		inst.getItemColors().register((stack, layer) ->
						layer == 1 ? FocusItem.getColourAspect(stack) : 0xffffffff,
				ArcanaItems.DEFAULT_FOCUS::get
		);
		
		inst.getBlockColors().register((state, access, pos, index) ->
						access != null && pos != null ? BiomeColors.getFoliageColor(access, pos) : -1,
				ArcanaBlocks.GREATWOOD_LEAVES.get(),
				ArcanaBlocks.WILLOW_LEAVES.get(),
				ArcanaBlocks.EUCALYPTUS_LEAVES.get(),
				ArcanaBlocks.DAIR_LEAVES.get()
		);
		
		inst.getItemColors().register((stack, layer) ->
						0x529c34,
				ArcanaBlocks.GREATWOOD_LEAVES.get(),
				ArcanaBlocks.WILLOW_LEAVES.get(),
				ArcanaBlocks.EUCALYPTUS_LEAVES.get(),
				ArcanaBlocks.DAIR_LEAVES.get()
		);
		
		// this should go to client init but again it works here
		
		ClientRegistry.registerKeyBinding(SWAP_FOCUS_BINDING);
	}
	
	@SubscribeEvent
	// can't be private
	public static void fireResearchChange(ResearchEvent even){
		ClientUtils.onResearchChange(even);
	}
	
	public PlayerEntity getPlayerOnClient(){
		return Minecraft.getInstance().player;
	}
	
	public World getWorldOnClient(){
		return Minecraft.getInstance().world;
	}
	
	public void scheduleOnClient(Runnable runnable){
		Minecraft.getInstance().deferTask(runnable);
	}
	
	public ItemStack getAspectItemStackForDisplay(){
		if(Minecraft.getInstance().player == null)
			return super.getAspectItemStackForDisplay();
		else
			return AspectUtils.aspectStacks.get((Minecraft.getInstance().player.ticksExisted / 20) % AspectUtils.aspectStacks.size());
	}
	
	@SuppressWarnings("rawtypes")
	protected void registerRenders(){
		//Tile Entity Special Render
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.JAR_TE.get(), JarTileEntityRender::new);
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.ASPECT_SHELF_TE.get(), AspectBookshelfTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.VACUUM_TE.get(), VacuumTileEntityRender::new);
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.PEDESTAL_TE.get(), PedestalTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.ASPECT_VALVE_TE.get(), AspectValveTileEntityRenderer::new);
		
		//Special Render
		ModelLoader.addSpecialModel(new ResourceLocation(MODID, "item/phial"));
		ModelLoader.addSpecialModel(new ResourceLocation(MODID, "item/arcanum"));
		ModelLoader.addSpecialModel(new ResourceLocation(MODID, "item/cheaters_arcanum"));
		ModelLoader.addSpecialModel(new ResourceLocation(MODID, "item/illustrious_grimoire"));
		ModelLoader.addSpecialModel(new ResourceLocation(MODID, "item/tainted_codex"));
		ModelLoader.addSpecialModel(new ResourceLocation(MODID, "item/crimson_rites"));
		
		//Entity Render
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.KOALA_ENTITY.get(), KoalaEntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.DAIR_SPIRIT.get(), DairSpiritRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.WILLOW_SPIRIT.get(), WillowSpiritRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.SPELL_CLOUD.get(), SpellCloudEntityRenderer::new);
		
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.SPELL_EGG.get(), SpellEggEntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.BIG_SPELL_EGG.get(), SpellEggEntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINT_BOTTLE.get(), TaintBottleEntityRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_BAT.get(), TaintedBatRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_BEE.get(), manager -> new TaintedEntityRender(manager, new BeeModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_CAT.get(), manager -> new TaintedEntityRender(manager, new CatModel(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_CAVE_SPIDER.get(), TaintedCaveSpiderRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_DONKEY.get(), manager -> new TaintedEntityRender(manager, new HorseModel(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_EVOKER.get(), manager -> new TaintedEntityRender(manager, new IllagerModel(0.0F, 0.0F, 64, 64)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_FOX.get(), manager -> new TaintedEntityRender(manager, new TaintedFoxModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_HORSE.get(), manager -> new TaintedEntityRender(manager, new HorseModel(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ILLUSIONER.get(), manager -> new TaintedEntityRender(manager, new IllagerModel(0.0F, 0.0F, 64, 64)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_LLAMA.get(), manager -> new TaintedEntityRender(manager, new LlamaModel(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PANDA.get(), manager -> new TaintedEntityRender(manager, new PandaModel(9, 0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PARROT.get(), manager -> new TaintedEntityRender(manager, new ParrotModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PUFFERFISH.get(), manager -> new TaintedEntityRender(manager, new PufferFishMediumModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_POLAR_BEAR.get(), manager -> new TaintedEntityRender(manager, new PolarBearModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_RABBIT.get(), manager -> new TaintedEntityRender(manager, new RabbitModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SHEEP.get(), manager -> new TaintedEntityRender(manager, new TaintedSheepModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SKELETON.get(), manager -> new TaintedEntityRender(manager, new SkeletonModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SLIME.get(), TaintedSlimeRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SNOW_GOLEM.get(), manager -> new TaintedEntityRender(manager, new SnowManModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_TRADER_LLAMA.get(), manager -> new TaintedEntityRender(manager, new LlamaModel(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_VINDICATOR.get(), manager -> new TaintedEntityRender(manager, new IllagerModel(0.0F, 0.0F, 64, 64)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PILLAGER.get(), manager -> new TaintedEntityRender(manager, new IllagerModel(0.0F, 0.0F, 64, 64)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_WOLF.get(), manager -> new TaintedEntityRender(manager, new TaintedWolfModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ZOMBIE.get(), manager -> new TaintedEntityRender(manager, new TaintedZombieModel()));
		
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_DROWNED.get(), manager -> new TaintedEntityRender(manager, new DrownedModel(0.5f, true)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ELDER_GUARDIAN.get(), manager -> new TaintedEntityRender(manager, new GuardianModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_GIANT.get(), manager -> new TaintedEntityRender(manager, new GiantModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_GUARDIAN.get(), manager -> new TaintedEntityRender(manager, new GuardianModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_HUSK.get(), manager -> new TaintedEntityRender(manager, new ZombieModel(0.0F, false)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_MAGMA_CUBE.get(), manager -> new TaintedEntityRender(manager, new MagmaCubeModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_MULE.get(), manager -> new TaintedEntityRender(manager, new HorseModel(0.0F)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_CREEPER.get(), manager -> new TaintedEntityRender(manager, new CreeperModel<TaintedCreeperEntity>()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ZOMBIEFIED_PIGLIN.get(), manager -> new TaintedEntityRender(manager, new ZombieModel(0.0F, false))); // 1.16 -> ZombiefiedPiglin
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_STRAY.get(), manager -> new TaintedEntityRender(manager, new SkeletonModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_TROPICAL_FISH.get(), manager -> new TaintedEntityRender(manager, new TropicalFishAModel(0.0F)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_TURTLE.get(), manager -> new TaintedEntityRender(manager, new TurtleModel(0.0F)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_VEX.get(), manager -> new TaintedEntityRender(manager, new VexModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_RAVAGER.get(), manager -> new TaintedEntityRender(manager, new RavagerModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_WITHER.get(), manager -> new TaintedEntityRender(manager, new WitherModel(0.0F))); // Check this.
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_WITHER_SKELETON.get(), manager -> new TaintedEntityRender(manager, new SkeletonModel()));
		
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_COW.get(), manager -> new TaintedEntityRender(manager, new CowModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PIG.get(), manager -> new TaintedEntityRender(manager, new PigModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SPIDER.get(), manager -> new TaintedEntityRender(manager, new SpiderModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_BLAZE.get(), manager -> new TaintedEntityRender(manager, new BlazeModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_CHICKEN.get(), manager -> new TaintedEntityRender(manager, new ChickenModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_COD.get(), manager -> new TaintedEntityRender(manager, new CodModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_DOLPHIN.get(), manager -> new TaintedEntityRender(manager, new DolphinModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ENDERMAN.get(), manager -> new TaintedEntityRender(manager, new EndermanModel<TaintedEntity>(0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ENDERMITE.get(), manager -> new TaintedEntityRender(manager, new EndermiteModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_GHAST.get(), TaintedGhastRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_MOOSHROOM.get(), manager -> new TaintedEntityRender(manager, new CowModel<TaintedEntity>())); // No tainted_warts on top
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_OCELOT.get(), manager -> new TaintedEntityRender(manager, new OcelotModel<TaintedEntity>(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SALMON.get(), manager -> new TaintedEntityRender(manager, new SalmonModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SILVERFISH.get(), manager -> new TaintedEntityRender(manager, new SilverfishModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SQUID.get(), TaintedSquidRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_VILLAGER.get(), manager -> new TaintedEntityRender(manager, new VillagerModel<TaintedEntity>(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_WANDERING_TRADER.get(), manager -> new TaintedEntityRender(manager, new VillagerModel<TaintedEntity>(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_WITCH.get(), manager -> new TaintedEntityRender(manager, new WitchModel<TaintedEntity>(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PHANTOM.get(), manager -> new TaintedEntityRender(manager, new PhantomModel<TaintedEntity>()));
	}
	
	protected void registerScreens(){
		//Screens
		ScreenManager.registerFactory(ArcanaContainers.FOCI_FORGE.get(), FociForgeScreen::new);
		ScreenManager.registerFactory(ArcanaContainers.RESEARCH_TABLE.get(), ResearchTableScreen::new);
		ScreenManager.registerFactory(ArcanaContainers.ARCANE_CRAFTING_TABLE.get(), ArcaneCraftingTableScreen::new);
		ScreenManager.registerFactory(ArcanaContainers.ASPECT_CRYSTALLIZER.get(), AspectCrystallizerScreen::new);
	}
	
	protected void setRenderLayers(){
		//Render Layers for Blocks
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.WARDENED_BLOCK.get(), RenderType.getTranslucent());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.SECURE_JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.VOID_JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.VACUUM_JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.PRESSURE_JAR.get(), RenderType.getTranslucent());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.RESEARCH_TABLE.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.RESEARCH_TABLE_COMPONENT.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.ASPECT_WINDOW.get(), RenderType.getTranslucent());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.HARDENED_GLASS.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.SMOKEY_GLASS.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.LUMINIFEROUS_GLASS.get(), RenderType.getTranslucent());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.DEAD_FLOWER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.DEAD_GRASS.get(), RenderType.getCutout());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_GRASS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_FLOWER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_MUSHROOM.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_VINE.get(), RenderType.getCutout());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.SILVERWOOD_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.DAIR_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.EUCALYPTUS_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.GREATWOOD_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.HAWTHORN_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.WILLOW_SAPLING.get(), RenderType.getCutout());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.MAGIC_MUSHROOM.get(), RenderType.getCutout());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_DAIR_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_ACACIA_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_BIRCH_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_DARKOAK_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_EUCALYPTUS_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_GREATWOOD_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_HAWTHORN_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_JUNGLE_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_OAK_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_SPRUCE_SAPLING.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_WILLOW_SAPLING.get(), RenderType.getCutout());
		
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
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_DAIR_LEAVES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_ACACIA_LEAVES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_BIRCH_LEAVES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_DARKOAK_LEAVES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_EUCALYPTUS_LEAVES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_GREATWOOD_LEAVES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_HAWTHORN_LEAVES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_JUNGLE_LEAVES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_OAK_LEAVES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_SPRUCE_LEAVES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_WILLOW_LEAVES.get(), RenderType.getCutout());
		
		RenderTypeLookup.setRenderLayer(ArcanaFluids.TAINT_FLUID_BLOCK.get(), RenderType.getTranslucent());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.FIRE_CLUSTER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.WATER_CLUSTER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.AIR_CLUSTER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.EARTH_CLUSTER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.ORDER_CLUSTER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.CHAOS_CLUSTER.get(), RenderType.getCutout());

//		RenderTypeLookup.setRenderLayer(ArcanaBlocks.FIRE_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
//		RenderTypeLookup.setRenderLayer(ArcanaBlocks.WATER_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
//		RenderTypeLookup.setRenderLayer(ArcanaBlocks.AIR_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
//		RenderTypeLookup.setRenderLayer(ArcanaBlocks.EARTH_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
//		RenderTypeLookup.setRenderLayer(ArcanaBlocks.ORDER_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
//		RenderTypeLookup.setRenderLayer(ArcanaBlocks.CHAOS_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
	}
}