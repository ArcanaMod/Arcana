package net.arcanamod;

import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.bases.LeavesBase;
import net.arcanamod.client.gui.ResearchEntryGUI;
import net.arcanamod.event.ResearchEvent;
import net.arcanamod.items.ItemWand;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Common Proxy
 *
 * @author Atlas
 */
@Mod.EventBusSubscriber
public class CommonProxy{
	
	public void registerItemRenderer(Item item, int meta, String id){
	}
	
	public void preInit(FMLPreInitializationEvent event){
	}
	
	public void init(FMLInitializationEvent event){
	}
	
	public void postInit(FMLPostInitializationEvent event){
	}
	
	public void registerWand(IForgeRegistry<Item> registry, ItemWand wand){
		registry.register(wand);
	}
	
	public void setGraphicsLevel(LeavesBase parBlock, boolean parFancyEnabled){
	}
	
	public void openResearchBookUI(ResourceLocation book){
	}
	
	public void onResearchChange(ResearchEvent event){
	}
	
	@SubscribeEvent
	// can't be private
	public static void fireResearchChange(ResearchEvent even){
		Arcana.proxy.onResearchChange(even);
	}
	
	public EntityPlayer getPlayerOnClient(){
		return null;
	}
	
	public void scheduleOnClient(Runnable runnable){
	}
	
	public ItemStack getAspectItemStackForDisplay(){
		return Aspects.aspectStacks.get(0);
	}
}