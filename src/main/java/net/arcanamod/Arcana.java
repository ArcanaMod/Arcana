package net.arcanamod;

import net.arcanamod.aspects.*;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.systems.taint.Taint;
import net.arcanamod.blocks.tiles.ArcanaTiles;
import net.arcanamod.capabilities.TaintTrackableCapability;
import net.arcanamod.containers.ArcanaContainers;
import net.arcanamod.effects.ArcanaEffects;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.fluids.ArcanaFluids;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.WandItem;
import net.arcanamod.network.Connection;
import net.arcanamod.systems.research.EntrySection;
import net.arcanamod.systems.research.Puzzle;
import net.arcanamod.systems.research.Requirement;
import net.arcanamod.systems.research.ResearchLoader;
import net.arcanamod.capabilities.ResearcherCapability;
import net.arcanamod.items.recipes.ArcanaRecipes;
import net.arcanamod.world.NodeType;
import net.arcanamod.capabilities.AuraChunkCapability;
import net.arcanamod.world.WorldInteractionsRegistry;
import net.arcanamod.worldgen.ArcanaBiomes;
import net.arcanamod.worldgen.ArcanaFeatures;
import net.arcanamod.worldgen.FeatureGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
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
 */
@Mod(Arcana.MODID)
public class Arcana{

	// Main
	public static final String MODID = "arcana";
	public static final Logger logger = LogManager.getLogger("Arcana");
	public static Arcana instance;

	// Json Registry
	public static ResearchLoader researchManager;
	public static ItemAspectRegistry itemAspectRegistry;
	public static WorldInteractionsRegistry worldInteractionsRegistry;

	// Item Groups
	public static ItemGroup ITEMS = new SupplierItemGroup(MODID, () -> new ItemStack(ArcanaBlocks.ARCANE_STONE.get()))
			.setHasSearchBar(true)
			.setBackgroundImage(new ResourceLocation("textures/gui/container/creative_inventory/tab_item_search.png"));
	public static ItemGroup TAINT = new SupplierItemGroup("taint", () -> new ItemStack(ArcanaBlocks.TAINTED_GRASS_BLOCK.get()));

	// Proxy
	public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

	// Debug Mode
	public static final boolean debug = true;
	
	public Arcana(){
//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
//
//		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ArcanaConfig.COMMON_SPEC);
		
		// deferred registry registration
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//		NodeType.init();
//		Aspects.init();

//		ArcanaBlocks.BLOCKS.register(modEventBus);
//		ArcanaEntities.ENTITY_TYPES.register(modEventBus);
//		ArcanaItems.ITEMS.register(modEventBus);
//		ArcanaEffects.EFFECTS.register(modEventBus);
//		ArcanaRecipes.Serializers.SERIALIZERS.register(modEventBus);
//		ArcanaTiles.TES.register(modEventBus);
//		ArcanaContainers.CON.register(modEventBus);
//		ArcanaFeatures.FEATURES.register(modEventBus);
//		ArcanaBiomes.BIOMES.register(modEventBus);
//		ArcanaFluids.FLUIDS.register(modEventBus);

//		proxy.construct();
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
		net.minecraftforge.fml.StartupMessageManager.addModMessage("Arcana: Research registration completed");
		
		proxy.preInit(event);

		Connection.init();
		
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

		FeatureGenerator.setupFeatureGeneration();
	}
	
	private void setupClient(FMLClientSetupEvent event){
		// Moved to Client Proxy
	}
	
	private void enqueueIMC(InterModEnqueueEvent event){
		// tell curios or whatever about our baubles
	}
	
	private void processIMC(InterModProcessEvent event){
		// handle aspect registration from addons?
	}
}