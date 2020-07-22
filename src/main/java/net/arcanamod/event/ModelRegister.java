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
	
	@SubscribeEvent
	public static void onBake(ModelRegistryEvent event){
		for(Focus focus : Focus.FOCI){
			ResourceLocation loc = focus.getModelLocation();
			ModelLoader.addSpecialModel(new ResourceLocation(loc.getNamespace(), "item/wands/foci/" + loc.getPath()));
		}
		// TODO: variants
		ModelLoader.addSpecialModel(arcLoc("item/wands/caps/wand"));
		ModelLoader.addSpecialModel(arcLoc("item/wands/variants/wand"));
	}
}