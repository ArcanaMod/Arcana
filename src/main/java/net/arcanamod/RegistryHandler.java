package net.arcanamod;

import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.CrystalClusterBlock;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.items.CrystalClusterItem;
import net.arcanamod.worldgen.ArcanaFeatures;
import net.arcanamod.worldgen.trees.features.GreatwoodFoliagePlacer;
import net.arcanamod.worldgen.trees.features.GreatwoodTrunkPlacer;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;

import static net.arcanamod.Arcana.arcLoc;
import static net.arcanamod.worldgen.ArcanaFeatures.*;

@EventBusSubscriber(modid = Arcana.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegistryHandler{
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onItemRegister(RegistryEvent.Register<Item> event){
		AspectUtils.register();
		IForgeRegistry<Item> registry = event.getRegistry();
		ArcanaBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
			Item.Properties properties = new Item.Properties();
			if(!(block instanceof FlowingFluidBlock)){
				if(block instanceof GroupedBlock){
					GroupedBlock grouped = (GroupedBlock)block;
					ItemGroup group = grouped.getGroup();
					if(group != null)
						properties = properties.group(group);
				}else if(block != ArcanaBlocks.WARDENED_BLOCK.orElse(null)
						&& block != ArcanaBlocks.VACUUM_BLOCK.orElse(null)
						&& block != ArcanaBlocks.LIGHT_BLOCK.orElse(null))
					properties = properties.group(Arcana.ITEMS);
				Item blockItem = block instanceof CrystalClusterBlock ? new CrystalClusterItem(block, properties, 3) : new BlockItem(block, properties);
				blockItem.setRegistryName(block.getRegistryName());
				registry.register(blockItem);
			}
		});
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onFeatureRegister(RegistryEvent.Register<Feature<?>> event){
		NODE.setRegistryName(arcLoc("node"));
		event.getRegistry().register(NODE);
		
		GREATWOOD_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.GREATWOOD_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.GREATWOOD_LEAVES.get().getDefaultState()), new GreatwoodFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 18), new GreatwoodTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build();
		GREATWOOD_TREE = Feature.TREE.withConfiguration(GREATWOOD_TREE_CONFIG);
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, arcLoc("greatwood_tree"), GREATWOOD_TREE);
	}
}