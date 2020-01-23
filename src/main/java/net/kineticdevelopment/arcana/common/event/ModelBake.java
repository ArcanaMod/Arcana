package net.kineticdevelopment.arcana.common.event;

import net.kineticdevelopment.arcana.api.wand.EnumAttachmentType;
import net.kineticdevelopment.arcana.client.BakedModelWand;
import net.kineticdevelopment.arcana.common.items.ItemAttachment;
import net.kineticdevelopment.arcana.common.objects.items.ItemWand;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Event Handler for ModelBake
 * 
 * @author Merijn
 */
@EventBusSubscriber
public class ModelBake {

    @SubscribeEvent
    public static void onBake(ModelBakeEvent event) {
        ItemAttachment attachment;
        IBakedModel[][] models;
        ModelResourceLocation mrl;
        IBakedModel main;
        int i;
        int j;

        for(ItemWand wand : ItemWand.WANDS) {
            models = new IBakedModel[EnumAttachmentType.values().length][];

            for(EnumAttachmentType type : EnumAttachmentType.values()) {
                i = type.getSlot();

                models[i] = new IBakedModel[wand.getAmmountForSlot(type)];

                for(j = 0; j < models[i].length; ++j)  {
                    attachment = wand.getAttachment(type, j);
                    models[i][j] = event.getModelRegistry().getObject(new ModelResourceLocation(Main.MODID + ":wands/" + attachment.getRegistryName().getResourcePath(), "inventory"));
                }
            }

            mrl = new ModelResourceLocation(Main.MODID + ":wands/" + wand.getRegistryName().getResourcePath(), "inventory");

            main = event.getModelRegistry().getObject(mrl);

            event.getModelRegistry().putObject(mrl, new BakedModelWand(main, models));

        }
    }

}
