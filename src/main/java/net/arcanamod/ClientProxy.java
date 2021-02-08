package net.arcanamod;

import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.ArcanaTiles;
import net.arcanamod.blocks.tiles.AspectWindowTileEntity;
import net.arcanamod.client.BookRenderer;
import net.arcanamod.client.event.*;
import net.arcanamod.client.gui.*;
import net.arcanamod.client.model.WandModelLoader;
import net.arcanamod.client.render.DairSpiritRenderer;
import net.arcanamod.client.render.KoalaEntityRenderer;
import net.arcanamod.client.render.WillowSpiritRenderer;
import net.arcanamod.client.render.aspects.ArcanaParticles;
import net.arcanamod.client.render.tiles.*;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.client.research.PuzzleRenderer;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.containers.ArcanaContainers;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.event.ResearchEvent;
import net.arcanamod.fluids.ArcanaFluids;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.attachment.FocusItem;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.ResearchEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;

import static net.arcanamod.Arcana.MODID;
import static net.arcanamod.Arcana.arcLoc;

/**
 * Handles client-side only things (that would otherwise crash dedicated servers).
 */
public class ClientProxy extends CommonProxy{
	
	public static KeyBinding SWAP_FOCUS_BINDING = new KeyBinding("key.arcana.swap_focus", GLFW.GLFW_KEY_G, "key.categories.mod.arcana");
	
	public void construct(){
		super.construct();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(TextureStitchHandler::onTextureStitch);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(BakeEventHandler::onModelBake);
		
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(RenderTooltipHandler::makeTooltip);
		modEventBus.addListener(FogHandler::setFogColour);
		modEventBus.addListener(FogHandler::setFogDensity);
		modEventBus.addListener(FogHandler::setFogLength);
		modEventBus.addListener(InitScreenHandler::onInitGuiEvent);
		modEventBus.addListener(ParticleFactoryEvent::onParticleFactoryRegister);
		
		MinecraftForge.EVENT_BUS.register(ClientTickHandler.class);
		MinecraftForge.EVENT_BUS.register(InitScreenHandler.class);
		MinecraftForge.EVENT_BUS.register(BookRenderer.class);
		MinecraftForge.EVENT_BUS.register(ParticleFactoryEvent.class);
		
		ArcanaParticles.PARTICLE_TYPES.register(modEventBus);
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
		ModelLoaderRegistry.registerLoader(arcLoc("wand_loader"), new WandModelLoader());
		
		// there's an event for this, but putting it here seems to affect literally nothing. huh.
		// I'm not at all surprised.
		
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
		
		// this should go to client init but again it works here
		
		ClientRegistry.registerKeyBinding(SWAP_FOCUS_BINDING);
	}
	
	@Override
	public void openResearchBookUI(ResourceLocation book, Screen parentScreen, ItemStack sender){
		if(!ResearchBooks.disabled.contains(book))
			Minecraft.getInstance().displayGuiScreen(new ResearchBookScreen(ResearchBooks.books.get(book), parentScreen, sender));
		else
			Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("message.arcana.disabled").applyTextStyle(TextFormatting.RED));
	}
	
	@Override
	public void openScribbledNotesUI(){
		Minecraft.getInstance().displayGuiScreen(new ScribbledNoteScreen(new StringTextComponent("")));
	}
	
	public void onResearchChange(ResearchEvent event){
		if(Minecraft.getInstance().currentScreen instanceof ResearchEntryScreen)
			((ResearchEntryScreen)Minecraft.getInstance().currentScreen).updateButtons();
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
	
	public void displayPuzzleToast(@Nullable ResearchEntry entry){
		Minecraft.getInstance().getToastGui().add(new CompletePuzzleToast(entry));
	}
	
	protected void registerRenders(){
		//Tile Entity Special Render
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.JAR_TE.get(), JarTileEntityRender::new);
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.VACUUM_TE.get(), VacuumTileEntityRender::new);
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.PEDESTAL_TE.get(), PedestalTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.ASPECT_VALVE_TE.get(), AspectValveTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.RESEARCH_TABLE_TE.get(), ResearchTableTileEntityRender::new);
		
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
		
		ArcanaEntities.render();
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
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.WARDENED_BLOCK.get(), RenderType.getSolid());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.SECURE_JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.VOID_JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.VACUUM_JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.PRESSURE_JAR.get(), RenderType.getTranslucent());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.RESEARCH_TABLE.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.ASPECT_WINDOW.get(), RenderType.getTranslucent());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.HARDENED_GLASS.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.SMOKEY_GLASS.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.LUMINIFEROUS_GLASS.get(), RenderType.getTranslucent());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.DEAD_FLOWER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.DEAD_GRASS.get(), RenderType.getCutout());
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_GRASS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_FLOWER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.TAINTED_MUSHROOM.get(), RenderType.getCutout());
		
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
		
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.FIRE_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.WATER_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.AIR_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.EARTH_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.ORDER_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.CHAOS_CRYSTAL_FRAGMENTS.get(), RenderType.getCutout());
	}
}