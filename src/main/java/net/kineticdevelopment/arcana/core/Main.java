package net.kineticdevelopment.arcana.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
	public static final String MODID = "arcana";
	public static final String NAME = "Arcana";
	public static final String VERSION = "1.0";
	
	public static final Logger logger = LogManager.getLogger("Arcana");

	@Mod.Instance
	public static Main instance;


	@SidedProxy(clientSide="net.kineticdevelopment.arcana.core.ClientProxy", serverSide="net.kineticdevelopment.arcana.core.CommonProxy")
    public static CommonProxy proxy;



	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	public static CreativeTabs ARCANA = (new CreativeTabs("tabArcana") {
        @Override
        public ItemStack getTabIconItem() {
        	ItemStack stack = new ItemStack(ItemInit.WOOD_WAND);
			NBTTagCompound compound = getNBT(stack);
			compound.setInteger("cap", 1);
			stack.setTagCompound(compound);
            return stack;
        }
    });


	public static NBTTagCompound getNBT(ItemStack itemStack)
	{
		if(!itemStack.hasTagCompound())
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		return itemStack.getTagCompound();
	}
}