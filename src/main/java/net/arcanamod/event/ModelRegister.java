package net.arcanamod.event;

import net.arcanamod.Arcana;
import net.minecraftforge.api.distmarker.Dist;
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
@Mod.EventBusSubscriber(modid = Arcana.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelRegister{
	
	@SubscribeEvent
	public static void onBake(ModelRegistryEvent event){
		// TODO: variants
		ModelLoader.addSpecialModel(arcLoc("item/wands/caps/wand"));
		ModelLoader.addSpecialModel(arcLoc("item/wands/variants/wand"));
	}
}