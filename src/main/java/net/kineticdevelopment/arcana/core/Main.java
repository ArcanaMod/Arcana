package net.kineticdevelopment.arcana.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.kineticdevelopment.arcana.common.commands.IncreaseTaintLevel;
import net.kineticdevelopment.arcana.common.worldgen.OreWorldGen;
import net.kineticdevelopment.arcana.init.EntityInit;
import net.kineticdevelopment.arcana.init.blockinit;
import net.kineticdevelopment.arcana.init.iteminit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid=Main.MODID, name=Main.NAME, version=Main.VERSION)
public class Main 
{
	public static final String MODID = "arcana";
	public static final String NAME = "Arcana";
	public static final String VERSION = "1.0";
	
	@SidedProxy(clientSide="net.kineticdevelopment.arcana.core.ClientProxy", serverSide="net.kineticdevelopment.arcana.core.CommonProxy")
    public static CommonProxy proxy;
	@Instance(MODID)
	public static Main instance;

    public static final Logger logger = LogManager.getLogger("Kinetic");
	
    @SideOnly(Side.CLIENT)
    @EventHandler
    public void preInitClient(FMLPreInitializationEvent e) 
	{
        EntityInit.initModels();
        System.out.println("Entity Models Registered!");
    }
    
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		GameRegistry.registerWorldGenerator(new OreWorldGen(), 3);
		EntityInit.init();
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event)
	{
		ResourceLocation groupname = new ResourceLocation("Recipes");
		GameRegistry.addShapedRecipe(new ResourceLocation("arcana:Amber_Block_Recipe"), groupname, new ItemStack(blockinit.AMBERBLOCK), new Object[] { "AAA", "AAA", "AAA", 'A', iteminit.AMBER });
		GameRegistry.addShapelessRecipe(new ResourceLocation("arcana:Amber_From_Block_Recipe"), groupname, new ItemStack(iteminit.AMBER, 9), new Ingredient[] {Ingredient.fromItem(Item.getItemFromBlock(blockinit.AMBERBLOCK))});
		GameRegistry.addShapelessRecipe(new ResourceLocation("arcana:Amber_Bricks_Recipe"), groupname, new ItemStack(blockinit.AMBERBRICK, 4), new Ingredient[] {Ingredient.fromItem(Item.getItemFromBlock(blockinit.AMBERBLOCK)), Ingredient.fromItem(Item.getItemFromBlock(blockinit.AMBERBLOCK)), Ingredient.fromItem(Item.getItemFromBlock(blockinit.AMBERBLOCK)), Ingredient.fromItem(Item.getItemFromBlock(blockinit.AMBERBLOCK))});
		GameRegistry.addShapelessRecipe(new ResourceLocation("arcana:Arcane_Bricks_Recipe"), groupname, new ItemStack(blockinit.ARCANESTONEBRICKS, 4), new Ingredient[] {Ingredient.fromItem(Item.getItemFromBlock(blockinit.ARCANESTONE)), Ingredient.fromItem(Item.getItemFromBlock(blockinit.ARCANESTONE)), Ingredient.fromItem(Item.getItemFromBlock(blockinit.ARCANESTONE)), Ingredient.fromItem(Item.getItemFromBlock(blockinit.ARCANESTONE))});
		GameRegistry.addShapelessRecipe(new ResourceLocation("arcana:Greatwood_Planks_Recipe"), groupname, new ItemStack(blockinit.GREATWOODPLANKS, 4), new Ingredient[] {Ingredient.fromItem(Item.getItemFromBlock(blockinit.GREATWOODLOG))});
		GameRegistry.addShapelessRecipe(new ResourceLocation("arcana:Silverwood_Planks_Recipe"), groupname, new ItemStack(blockinit.SILVERWOODPLANKS, 4), new Ingredient[] {Ingredient.fromItem(Item.getItemFromBlock(blockinit.SILVERWOODLOG))});
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new IncreaseTaintLevel());
	}
	
	public static final CreativeTabs tabArcana = (new CreativeTabs("tabArcana") 
	{
		@Override
		public ItemStack getTabIconItem() 
		{
			return new ItemStack(iteminit.THAUMONOMICON);
		}
		
		@Override
		public boolean hasSearchBar()
		{
			return true;
		}
	}).setBackgroundImageName("item_search.png");
}
