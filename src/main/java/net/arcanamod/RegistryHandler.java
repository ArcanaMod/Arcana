package net.arcanamod;

import com.google.common.collect.ImmutableList;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.CrystalClusterBlock;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.items.CrystalClusterItem;
import net.arcanamod.worldgen.trees.features.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
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
		
		// yes this has to be here. if you find a better way of doing this, be my guest.
		
		GREATWOOD_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.GREATWOOD_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.GREATWOOD_LEAVES.get().getDefaultState()), new GreatwoodFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 15), new GreatwoodTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build();
		GREATWOOD_TREE = Feature.TREE.withConfiguration(GREATWOOD_TREE_CONFIG);
		
		SILVERWOOD_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.SILVERWOOD_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.SILVERWOOD_LEAVES.get().getDefaultState()), new SilverwoodFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 15), new SilverwoodTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build();
		SILVERWOOD_TREE = Feature.TREE.withConfiguration(SILVERWOOD_TREE_CONFIG);

		LARGE_OAK_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()), new LargeOakFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 15), new LargeOakTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build();
		LARGE_OAK_TREE = Feature.TREE.withConfiguration(LARGE_OAK_TREE_CONFIG);

		TAINTED_GREATWOOD_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.TAINTED_GREATWOOD_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.TAINTED_GREATWOOD_LEAVES.get().getDefaultState()), new GreatwoodFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 15), new GreatwoodTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build();
		TAINTED_GREATWOOD_TREE = Feature.TREE.withConfiguration(TAINTED_GREATWOOD_TREE_CONFIG);
		
		TAINTED_OAK_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.TAINTED_OAK_LOG.get().getDefaultState()), new SimpleBlockStateProvider(ArcanaBlocks.TAINTED_OAK_LEAVES.get().getDefaultState()), new BlobFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build();
		TAINTED_OAK_TREE = Feature.TREE.withConfiguration(TAINTED_OAK_TREE_CONFIG);
		
		MAGICAL_FOREST_BONUS_TREES = Feature.RANDOM_SELECTOR
				.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
						LARGE_OAK_TREE.withChance(5 / 8f),
						SILVERWOOD_TREE.withChance(2 / 8f),
						GREATWOOD_TREE.withChance(2 / 8f)),
						GREATWOOD_TREE)) // yes this is dumb but whatever
				.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
				.withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 7.0f, 1)));
		
		MAGICAL_FOREST_GIANT_MUSHROOMS = Feature.RANDOM_SELECTOR
				.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
						Features.HUGE_RED_MUSHROOM.withChance(.5f),
						Features.HUGE_BROWN_MUSHROOM.withChance(.5f)),
						Features.HUGE_RED_MUSHROOM))
				.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
				.withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, .25f, 1)));
		
		MAGIC_MUSHROOM_PATCH = Feature.RANDOM_PATCH
				.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(ArcanaBlocks.MAGIC_MUSHROOM.get().getDefaultState()), SimpleBlockPlacer.PLACER)).tries(64).preventProjection().build())
				.withPlacement(Features.Placements.PATCH_PLACEMENT).chance(12);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onFeatureRegister(RegistryEvent.Register<Feature<?>> event){
		NODE.setRegistryName(arcLoc("node"));
		event.getRegistry().register(NODE);
	}
}