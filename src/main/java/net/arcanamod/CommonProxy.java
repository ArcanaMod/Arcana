package net.arcanamod;

import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.event.ResearchEvent;
import net.arcanamod.systems.research.ResearchEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nullable;

/**
 * Common Proxy
 *
 * @author Atlas
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy{
	
	public void construct(){
	}
	
	public void preInit(FMLCommonSetupEvent event){
	}
	
	public void openResearchBookUI(ResourceLocation book, Screen parentScreen){
	}
	
	public void openScribbledNotesUI(){
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
	
	public World getWorldOnClient(){
		return null;
	}
	
	public void scheduleOnClient(Runnable runnable){
	}
	
	public ItemStack getAspectItemStackForDisplay(){
		return AspectUtils.aspectStacks.get(0);
	}
	
	public void displayPuzzleToast(@Nullable ResearchEntry entry){
	}
}