package net.arcanamod;

import net.arcanamod.aspects.*;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.Taint;
import net.arcanamod.blocks.tiles.ArcanaTiles;
import net.arcanamod.capabilities.TaintTrackableCapability;
import net.arcanamod.client.gui.ArcaneCraftingTableScreen;
import net.arcanamod.client.gui.ResearchTableScreen;
import net.arcanamod.client.render.*;
import net.arcanamod.containers.ArcanaContainers;
import net.arcanamod.effects.ArcanaEffects;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.fluids.ArcanaFluids;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.WandItem;
import net.arcanamod.network.Connection;
import net.arcanamod.research.EntrySection;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.Requirement;
import net.arcanamod.research.ResearchLoader;
import net.arcanamod.capabilities.ResearcherCapability;
import net.arcanamod.util.recipes.ArcanaRecipes;
import net.arcanamod.world.NodeType;
import net.arcanamod.capabilities.AuraChunkCapability;
import net.arcanamod.worldgen.ArcanaFeatures;
import net.arcanamod.worldgen.FeatureGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.LazyOptional;
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

import javax.annotation.Nonnull;

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
	public static ItemAspectRegistry itemAspectRegistry;
	
	public static ItemGroup ITEMS = new SupplierItemGroup(MODID, () -> new ItemStack(ArcanaBlocks.ARCANE_STONE.get()))
			.setHasSearchBar(true)
			.setBackgroundImage(new ResourceLocation("textures/gui/container/creative_inventory/tab_item_search.png"));
	public static ItemGroup TAINT = new SupplierItemGroup("taint", () -> new ItemStack(ArcanaBlocks.TAINTED_GRASS_BLOCK.get()));
	
	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

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
		Aspects.init();

		ArcanaBlocks.BLOCKS.register(modEventBus);
		ArcanaEntities.ENTITY_TYPES.register(modEventBus);
		ArcanaItems.ITEMS.register(modEventBus);
		ArcanaEffects.EFFECTS.register(modEventBus);
		ArcanaRecipes.Serializers.SERIALIZERS.register(modEventBus);
		ArcanaTiles.TES.register(modEventBus);
		ArcanaContainers.CON.register(modEventBus);
		ArcanaFeatures.FEATURES.register(modEventBus);
		ArcanaFluids.FLUIDS.register(modEventBus);

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
		AuraChunkCapability.init();
		TaintTrackableCapability.init();
		Puzzle.init();
		Taint.init();
		
		proxy.preInit(event);

		Connection.init();

		FeatureGenerator.setupFeatureGeneration();
		
		// dispenser behaviour for wand conversion
		DispenserBlock.registerDispenseBehavior(ArcanaItems.WAND.get(), new OptionalDispenseBehavior(){
			@Nonnull
			protected ItemStack dispenseStack(@Nonnull IBlockSource source, @Nonnull ItemStack stack){
				World world = source.getWorld();
				BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
				ActionResultType convert = WandItem.convert(world, blockpos, null);
				if(convert.isSuccess()){
					successful = true;
					return stack;
				}else
					return super.dispenseStack(source, stack);
			}
		});
		// and phial usage
		DispenserBlock.registerDispenseBehavior(ArcanaItems.PHIAL.get(), new OptionalDispenseBehavior(){
			// copypasta from PhialItem
			// needs some reworking for readability...
			@Nonnull
			protected ItemStack dispenseStack(@Nonnull IBlockSource source, @Nonnull ItemStack stack){
				World world = source.getWorld();
				BlockPos pos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
				BlockState state = world.getBlockState(pos);
				TileEntity tile = world.getTileEntity(pos);
				DispenserTileEntity dispenser = source.getBlockTileEntity();
				if(tile != null){
					LazyOptional<IAspectHandler> cap = tile.getCapability(AspectHandlerCapability.ASPECT_HANDLER);
					if(cap.isPresent()){
						//noinspection ConstantConditions
						IAspectHandler tileHandle = cap.orElse(null);
						IAspectHolder myHandle = IAspectHandler.getFrom(stack).getHolder(0);
						if(myHandle.getCurrentVis() <= 0){
							for(IAspectHolder holder : tileHandle.getHolders())
								if(holder.getCurrentVis() > 0){
									int min = Math.min(holder.getCurrentVis(), 8);
									Aspect aspect = holder.getContainedAspect();
									ItemStack cappedItemStack = new ItemStack(ArcanaItems.PHIAL.get());
									IAspectHandler.getFrom(cappedItemStack).insert(0, new AspectStack(aspect, min), false);
									if(cappedItemStack.getTag() == null)
										cappedItemStack.setTag(cappedItemStack.getShareTag());
									stack.shrink(1);
									if(!stack.isEmpty())
										if(dispenser.addItemStack(stack) == -1){
											Direction direction = source.getBlockState().get(DispenserBlock.FACING);
											IPosition iposition = DispenserBlock.getDispensePosition(source);
											doDispense(source.getWorld(), stack, 6, direction, iposition);
										}
									holder.drain(new AspectStack(aspect, min), false);
									successful = true;
									world.notifyBlockUpdate(pos, state, state, 2);
									return cappedItemStack;
								}
						}else{
							for(IAspectHolder holder : tileHandle.getHolders())
								if((holder.getCapacity() - holder.getCurrentVis() > 0 || holder.isIgnoringFullness()) && (holder.getContainedAspect() == myHandle.getContainedAspect() || holder.getContainedAspect() == Aspects.EMPTY)){
									int inserted = holder.insert(new AspectStack(myHandle.getContainedAspect(), myHandle.getCurrentVis()), false);
									if(inserted != 0){
										ItemStack newPhial = new ItemStack(ArcanaItems.PHIAL.get(), 1);
										IAspectHolder oldHolder = IAspectHandler.getFrom(stack).getHolder(0);
										IAspectHolder newHolder = IAspectHandler.getFrom(newPhial).getHolder(0);
										newHolder.insert(new AspectStack(oldHolder.getContainedAspect(), inserted), false);
										newPhial.setTag(newPhial.getShareTag());
										world.notifyBlockUpdate(pos, state, state, 2);
										return newPhial;
									}else{
										world.notifyBlockUpdate(pos, state, state, 2);
										stack.shrink(1);
										if(!stack.isEmpty())
											if(dispenser.addItemStack(stack) == -1){
												Direction direction = source.getBlockState().get(DispenserBlock.FACING);
												IPosition iposition = DispenserBlock.getDispensePosition(source);
												doDispense(source.getWorld(), stack, 6, direction, iposition);
											}
										return new ItemStack(ArcanaItems.PHIAL.get());
									}
								}
						}
					}
				}
				return super.dispenseStack(source, stack);
			}
		});
	}
	
	private void setupClient(FMLClientSetupEvent event){
		// TODO: move to ClientProxy for servers

		//Render Layers for Blocks
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.SECURE_JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.VOID_JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.RESEARCH_TABLE.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ArcanaBlocks.ASPECT_WINDOW.get(), RenderType.getTranslucent());

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

		//Tile Entity Special Render
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.JAR_TE.get(), JarTileEntityRender::new);
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.PEDESTAL_TE.get(), PedestalTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ArcanaTiles.ASPECT_VALVE_TE.get(), AspectValveTileEntityRenderer::new);

		//Screens
		ScreenManager.registerFactory(ArcanaContainers.RESEARCH_TABLE.get(), ResearchTableScreen::new);
		ScreenManager.registerFactory(ArcanaContainers.ARCANE_CRAFTING_TABLE.get(), ArcaneCraftingTableScreen::new);

		//Special Render
		ModelLoader.addSpecialModel(new ResourceLocation(MODID,"item/phial"));

		//Entity Render
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.KOALA_ENTITY.get(), KoalaEntityRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.DAIR_SPIRIT.get(), DairSpiritRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.WILLOW_SPIRIT.get(), WillowSpiritRenderer::new);

		ArcanaEntities.render();
	}
	
	private void enqueueIMC(InterModEnqueueEvent event){
		// tell curios or whatever about our baubles
	}
	
	private void processIMC(InterModProcessEvent event){
		// handle aspect registration from addons?
	}
}