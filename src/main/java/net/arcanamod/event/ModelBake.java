package net.arcanamod.event;

import net.arcanamod.items.ItemAttachment;
import net.arcanamod.items.ItemWand;
import net.arcanamod.Arcana;
import net.arcanamod.client.model.BakedModelWand;
import net.arcanamod.wand.EnumAttachmentType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Event Handler for ModelBakeEvent
 *
 * @author Merijn
 */
@EventBusSubscriber(Side.CLIENT)
public class ModelBake{
	
	@SubscribeEvent
	public static void onBake(ModelBakeEvent event){
		IBakedModel[][] models;
		ModelResourceLocation mrl;
		IBakedModel main;
		int i;
		
		for(ItemWand wand : ItemWand.WANDS){
			models = new IBakedModel[EnumAttachmentType.values().length][];
			
			for(EnumAttachmentType type : EnumAttachmentType.values()){
				i = type.getSlot();
				
				models[i] = new IBakedModel[wand.getAmountForSlot(type)];
				int j = 0;
				for(ItemAttachment attachment : wand.getAttachments()[i]){
					models[i][j] = event.getModelRegistry().getObject(new ModelResourceLocation(Arcana.MODID + ":wands/" + attachment.getRegistryName().getResourcePath(), "inventory"));
					j++;
				}
				
			}
			
			mrl = new ModelResourceLocation(Arcana.MODID + ":wands/" + wand.getRegistryName().getResourcePath(), "inventory");
			
			main = event.getModelRegistry().getObject(mrl);
			
			event.getModelRegistry().putObject(mrl, new BakedModelWand(main, models));
			
		}
	}
	
}
