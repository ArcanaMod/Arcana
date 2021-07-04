package net.arcanamod.client.event;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.client.model.baked.WardenedBlockBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class BakeEventHandler {

	private static Logger LOGGER = LogManager.getLogger();

	@SubscribeEvent
	public static void onModelBake(@Nonnull ModelBakeEvent event){
		// Find the existing mappings for CamouflageBakedModel - they will have been added automatically because
		// of our blockstates file for the BlockCamouflage.
		// Replace the mapping with our CamouflageBakedModel.
		// we only have one BlockState variant but I've shown code that loops through all of them, in case you have more than one.
		for (BlockState blockState : ArcanaBlocks.WARDENED_BLOCK.get().getStateContainer().getValidStates()) {
			ModelResourceLocation variantMRL = BlockModelShapes.getModelLocation(blockState);
			IBakedModel existingModel = event.getModelRegistry().get(variantMRL);
			if (existingModel == null) {
				LOGGER.warn("Did not find the expected vanilla baked model(s) for blockCamouflage in registry");
			} else if (existingModel instanceof WardenedBlockBakedModel) {
				LOGGER.warn("Tried to replace CamouflagedBakedModel twice");
			} else {
				WardenedBlockBakedModel customModel = new WardenedBlockBakedModel(existingModel);
				event.getModelRegistry().put(variantMRL, customModel);
			}
		}
	}
}