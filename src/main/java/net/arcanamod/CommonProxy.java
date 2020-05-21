package net.arcanamod;

import net.arcanamod.aspects.Aspects;
//import net.arcanamod.blocks.bases.LeavesBase;
import net.arcanamod.event.ResearchEvent;
import net.arcanamod.items.OldItemWand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Common Proxy
 *
 * @author Atlas
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy{
	
	public void registerItemRenderer(Item item, int meta, String id){
	}
	
	public void preInit(FMLCommonSetupEvent event){
	}
	
	public void registerWand(OldItemWand wand){
	
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
	
	public PlayerEntity getPlayerOnClient(){
		return null;
	}
	
	public void scheduleOnClient(Runnable runnable){
	}
	
	public ItemStack getAspectItemStackForDisplay(){
		return Aspects.aspectStacks.get(0);
	}
}