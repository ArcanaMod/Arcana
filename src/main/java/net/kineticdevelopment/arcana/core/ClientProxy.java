package net.kineticdevelopment.arcana.core;

import net.kineticdevelopment.arcana.common.items.ItemAttachment;
import net.kineticdevelopment.arcana.common.objects.items.ItemWand;
import net.kineticdevelopment.arcana.core.wand.EnumAttachmentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

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
	public void preInit(FMLPreInitializationEvent event) {}
	@Override
	public void init(FMLInitializationEvent event) {}
	@Override
	public void postInit(FMLPostInitializationEvent event) {}

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

	private static void drawParticle(World worldObj, Particle particle) {
		if(particle != null)
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}
/* PLEASE MAKE SURE YOU DON'T PUSH BROKEN BUILDS :D
	public static Particle getParticle(World worldObj, BlockPos eventAt, int id, String textureloc) {
		Particle particle = null;
		if(id == 0) {
			particle = new NormalNodeParticle(worldObj, eventAt.getX(), eventAt.getY(), eventAt.getZ(), 0, 0, 0, textureloc);//3, 20
		}
		return particle;
	}

 */
}
