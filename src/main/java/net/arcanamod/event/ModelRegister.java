package net.arcanamod.event;

import net.arcanamod.Arcana;
import net.arcanamod.items.attachment.Focus;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.arcanamod.Arcana.arcLoc;

/**
 * Event Handler for ModelBakeEvent
 *
 * @author Merijn
 */
@Mod.EventBusSubscriber(modid = Arcana.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelRegister{
	
	// public static List<IBakedModel> CAP_MODELS = new ArrayList<>();
	// public static List<IBakedModel> FOCI_MODELS = new ArrayList<>();
	// public static List<IBakedModel> VARIANT_MODELS = new ArrayList<>();
	
	@SubscribeEvent
	public static void onBake(ModelRegistryEvent event){
		for(Focus focus : Focus.FOCI){
			ResourceLocation loc = focus.getModelLocation();
			ModelLoader.addSpecialModel(new ResourceLocation(loc.getNamespace(), "item/wands/foci/" + loc.getPath()));
		}
		// TODO: variants
		ModelLoader.addSpecialModel(arcLoc("item/wands/caps/wand"));
		ModelLoader.addSpecialModel(arcLoc("item/wands/variants/wand"));
		/*for(ItemWand wand : ItemWand.WANDS){
			models = new IBakedModel[EnumAttachmentType.values().length][];
			for(EnumAttachmentType type : EnumAttachmentType.values()){
				i = type.getSlot();
				models[i] = new IBakedModel[wand.getAmountForSlot(type)];
				int j = 0;
				for(AttachmentItem attachment : wand.getAttachments()[i]){
					models[i][j] = event.getModelRegistry().get(new ModelResourceLocation(Arcana.MODID + ":wands/" + attachment.getRegistryName().getPath(), "inventory"));
					j++;
				}
			}
			
			mrl = new ModelResourceLocation(Arcana.MODID + ":wands/" + wand.getRegistryName().getPath(), "inventory");
			main = event.getModelRegistry().get(mrl);
			event.getModelRegistry().put(mrl, new BakedModelWand(main, models));
		}*/
	}
}