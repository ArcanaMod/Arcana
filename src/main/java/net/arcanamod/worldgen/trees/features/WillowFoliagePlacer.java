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
public class WillowFoliagePlacer extends FoliagePlacer{
	
	public static final Codec<WillowFoliagePlacer> CODEC = RecordCodecBuilder.create(a -> makeCodec(a).apply(a, WillowFoliagePlacer::new));
	
	protected static <P extends WillowFoliagePlacer> Products.P3<RecordCodecBuilder.Mu<P>, FeatureSpread, FeatureSpread, Integer> makeCodec(RecordCodecBuilder.Instance<P> builder) {
		return func_242830_b(builder).and(Codec.intRange(0, 24).fieldOf("height").forGetter((placer) -> placer.height));
	}
	
	protected final int height;
	
	public WillowFoliagePlacer(FeatureSpread radius, FeatureSpread offset, int height){
		super(radius, offset);
		this.height = height;
	}
	
	protected FoliagePlacerType<?> getPlacerType(){
		return ArcanaFeatures.WILLOW_FOLIAGE.get();
	}
	
	// generate
	protected void func_230372_a_(IWorldGenerationReader world, Random rand, BaseTreeFeatureConfig config, int height, Foliage foliage, int p_230372_6_, int radius, Set<BlockPos> foliagePositions, int p_230372_9_, MutableBoundingBox box){
		BlockPos node = foliage.func_236763_a_();
		// blob
		for(int x = -6; x < 6; x++){
			for(int y = 0; y < 5; y++){
				for(int z = -6; z < 6; z++){
					if(y < 4){
						// differently sized discs
						int rad = y == 0 ? 2 : (y == 1 ? 4 : y == 2 ? 5 : 3);
						if((x * x + z * z) <= (rad * rad + rand.nextInt(5) - 1)){
							BlockPos local = node.add(x, -y + 1, z);
							if(isAirAt(world, local))
								world.setBlockState(local, config.leavesProvider.getBlockState(rand, local), 3);
						}
					}
					if(y == 3){
						// overhangs
						int overhang = rand.nextInt(5) + 1;
						final int rad = 5;
						if((x * x + z * z) > 4)
							overhang -= 1;
						if(rand.nextInt(5) < 4)
							for(int i = 0; i < overhang; i++){
								BlockPos local = node.add(x, -y - i + 1, z);
								if(isAirAt(world, local) && (x * x + z * z) <= (rad * rad - 2)){
									
									world.setBlockState(local, config.leavesProvider.getBlockState(rand, local), 3);
								}
							}
					}
				}
			}
		}
	}
	
	public int func_230374_a_(Random rand, int p_230374_2_, BaseTreeFeatureConfig config){
		return 3;
	}
	
	protected boolean func_230373_a_(Random rand, int p_230373_2_, int p_230373_3_, int p_230373_4_, int p_230373_5_, boolean p_230373_6_){
		return false;
	}
}