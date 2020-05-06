package net.arcanamod;

import net.arcanamod.aspects.VisHandlerCapability;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.client.Sounds;
import net.arcanamod.event.WorldTickHandler;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.research.EntrySection;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.Requirement;
import net.arcanamod.research.impls.ResearcherCapability;
import net.arcanamod.spells.SpellEffectHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
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
	
	public static ItemGroup ITEMS = new SupplierItemGroup(MODID, () -> new ItemStack(ArcanaBlocks.ARCANE_STONE.get()));
	// public static ItemGroup TAINTED_BLOCKS = new SupplierItemGroup(MODID, () -> new ItemStack(ArcanaBlocks.TAINTED_GRASS.get()));
	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	
	public static ItemGroup ASPECTS = new SupplierItemGroup(MODID, proxy::getAspectItemStackForDisplay);
	
	public Arcana(){
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
		
		// deffered registry registration
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		ArcanaBlocks.BLOCKS.register(modEventBus);
		ArcanaItems.ITEMS.register(modEventBus);
		// ArcanaTileEntities.TES.register(modEventBus);
		// ArcanaRecipes.RECIPE_SERIALIZERS.register(modEventBus);
		// etc
	}
	
	private void setup(FMLCommonSetupEvent event){
		EntrySection.init();
		Requirement.init();
		ResearcherCapability.init();
		VisHandlerCapability.init();
		Puzzle.init();
		
		proxy.preInit(event);
		
		//
		
		MinecraftForge.EVENT_BUS.register(WorldTickHandler.instance);
		
		//Connection.init();
		//NetworkRegistry.INSTANCE.registerGuiHandler(Arcana.instance, new ArcanaGuiHandler());
		
		Sounds.registerSounds();
		SpellEffectHandler.init();
	}
	
	private void setupClient(FMLClientSetupEvent event){
	}
	
	private void enqueueIMC(InterModEnqueueEvent event){
	}
	
	private void processIMC(InterModProcessEvent event){
	}
}