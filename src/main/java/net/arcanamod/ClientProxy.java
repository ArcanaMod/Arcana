package net.arcanamod;

import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.ResearchBookGUI;
import net.arcanamod.client.gui.ResearchEntryGUI;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.client.research.PuzzleRenderer;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.event.ResearchEvent;
import net.arcanamod.research.ResearchBooks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Client Proxy
 *
 * @author Atlas, Luna
 */
public class ClientProxy extends CommonProxy{
	
	@Override
	public void preInit(FMLCommonSetupEvent event){
		super.preInit(event);
		EntrySectionRenderer.init();
		RequirementRenderer.init();
		PuzzleRenderer.init();
	}
	
	public void openResearchBookUI(ResourceLocation book){
		Minecraft.getInstance().displayGuiScreen(new ResearchBookGUI(ResearchBooks.books.get(book)));
	}
	
	public void onResearchChange(ResearchEvent event){
		if(Minecraft.getInstance().currentScreen instanceof ResearchEntryGUI)
			((ResearchEntryGUI)Minecraft.getInstance().currentScreen).updateButtonVisibility();
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
			return Aspects.aspectStacks.get((Minecraft.getInstance().player.ticksExisted / 20) % Aspects.aspectStacks.size());
	}
}