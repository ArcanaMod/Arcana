package net.arcanamod;

import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.ResearchBookGUI;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.client.research.PuzzleRenderer;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.event.ResearchEvent;
import net.arcanamod.items.ItemWand;
import net.arcanamod.research.ResearchBooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

/**
 * Client Proxy
 *
 * @author Atlas, Luna
 */
public class ClientProxy extends CommonProxy{
	
	@Override
	public void registerItemRenderer(Item item, int meta, String id){
		//ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	@Override
	public void preInit(FMLCommonSetupEvent event){
		super.preInit(event);
		EntrySectionRenderer.init();
		RequirementRenderer.init();
		PuzzleRenderer.init();
	}
	
	@Override
	public void registerWand(IForgeRegistry<Item> registry, ItemWand wand){
		super.registerWand(registry, wand);
		
		ModelResourceLocation main = new ModelResourceLocation(Arcana.MODID + ":wands/" + wand.getRegistryName().getPath(), "inventory");
		//ModelLoader.setCustomModelResourceLocation(wand, 0, main);
		
		ArrayList<ModelResourceLocation> list = new ArrayList<>();
		list.add(main);
		
		/*for(int i = 0; i < EnumAttachmentType.values().length; ++i){
			for(ItemAttachment attachment : wand.getAttachments()[i]){
				list.add(new ModelResourceLocation(Arcana.MODID + ":wands/" + attachment.getRegistryName().getPath(), "inventory"));
			}
		}*/
		
		//ModelBakery.registerItemVariants(wand, list.toArray(new ModelResourceLocation[0]));
	}
	
	public void openResearchBookUI(ResourceLocation book){
		Minecraft.getInstance().displayGuiScreen(new ResearchBookGUI(ResearchBooks.books.get(book)));
	}
	
	public void onResearchChange(ResearchEvent event){
		/*if(Minecraft.getInstance().currentScreen instanceof ResearchEntryGUI)
			((ResearchEntryGUI)Minecraft.getInstance().currentScreen).updateButtonVisibility();*/
	}
	
	public PlayerEntity getPlayerOnClient(){
		return Minecraft.getInstance().player;
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