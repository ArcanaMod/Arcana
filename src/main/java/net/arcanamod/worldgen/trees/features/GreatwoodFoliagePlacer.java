package net.arcanamod.worldgen.trees.features;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.worldgen.ArcanaFeatures;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.Set;

import static net.minecraft.world.gen.feature.Feature.isAirAt;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GreatwoodFoliagePlacer extends FoliagePlacer{
	
	public static final Codec<GreatwoodFoliagePlacer> CODEC = RecordCodecBuilder.create(a -> makeCodec(a).apply(a, GreatwoodFoliagePlacer::new));
	
	protected static <P extends GreatwoodFoliagePlacer> Products.P3<RecordCodecBuilder.Mu<P>, FeatureSpread, FeatureSpread, Integer> makeCodec(RecordCodecBuilder.Instance<P> builder) {
		return func_242830_b(builder).and(Codec.intRange(0, 24).fieldOf("height").forGetter((placer) -> placer.height));
	}
	
	protected final int height;
	
	public GreatwoodFoliagePlacer(FeatureSpread radius, FeatureSpread offset, int height){
		super(radius, offset);
		this.height = height;
	}
	
	protected FoliagePlacerType<?> getPlacerType(){
		return ArcanaFeatures.GREATWOOD_FOLIAGE.get();
	}
	
	// generate
	protected void func_230372_a_(IWorldGenerationReader world, Random rand, BaseTreeFeatureConfig config, int height, Foliage foliage, int p_230372_6_, int radius, Set<BlockPos> foliagePositions, int p_230372_9_, MutableBoundingBox box){
		BlockPos node = foliage.func_236763_a_();
		// Iterate in a spheroid to place leaves
		for(int x1 = -3; x1 <= 3; x1++){
			for(int z1 = -3; z1 <= 3; z1++){
				for(int y1 = -2; y1 <= 2; y1++){
					double rX = x1 / 3.0;
					double rZ = z1 / 3.0;
					double rY = y1 / 2.0;
					// Scale the distance to customize the blob shape
					rX *= 1.1;
					rZ *= 1.1;
					rY *= 0.95;
					double dist = rX * rX + rZ * rZ + rY * rY;
					
					// Apply randomness to the radius and place leaves
					if(dist <= 1 + (rand.nextDouble() * 0.3)){
						BlockPos local = node.add(x1, y1, z1);
						if(isAirAt(world, local)){
							world.setBlockState(local, config.leavesProvider.getBlockState(rand, local), 3);
						}
					}
				}
			}
		}
	}
	
	// height
	public int func_230374_a_(Random rand, int trunkHeight, BaseTreeFeatureConfig config){
		return 3;
	}
	
	protected boolean func_230373_a_(Random p_230373_1_, int p_230373_2_, int p_230373_3_, int p_230373_4_, int p_230373_5_, boolean p_230373_6_){
		return false;
	}
}
