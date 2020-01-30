package net.kineticdevelopment.arcana.core;

import java.util.ArrayList;

import net.kineticdevelopment.arcana.common.items.ItemAttachment;
import net.kineticdevelopment.arcana.common.objects.items.ItemWand;
import net.kineticdevelopment.arcana.core.wand.EnumAttachmentType;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Client Proxy
 * 
 * @author Atlas
 */

public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	@Override
	public void registerWand(IForgeRegistry<Item> registry, ItemWand wand) {
		super.registerWand(registry, wand);

		ModelResourceLocation main = new ModelResourceLocation(Main.MODID + ":wands/" + wand.getRegistryName().getResourcePath(), "inventory");
		ModelLoader.setCustomModelResourceLocation(wand, 0, main);

		ArrayList<ModelResourceLocation> list = new ArrayList<>();
		list.add(main);

		int i;

		for(i = 0; i < EnumAttachmentType.values().length; ++i) {
			for(ItemAttachment attachment : wand.getAttachments()[i]) {
				list.add(new ModelResourceLocation(Main.MODID + ":wands/" + attachment.getRegistryName().getResourcePath(), "inventory"));
			}
		}

		ModelBakery.registerItemVariants(wand, list.toArray(new ModelResourceLocation[list.size()]));
	}
}
