package net.arcanamod.entities.tainted;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TaintedSnowGolemEntity extends SnowGolemEntity {
	public TaintedSnowGolemEntity(EntityType<? extends Entity> p_i50244_1_, World p_i50244_2_) {
		super((EntityType<? extends SnowGolemEntity>) p_i50244_1_, p_i50244_2_);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	public void livingTick() {
		super.livingTick();
		if (!this.world.isRemote) {
			int i = MathHelper.floor(this.getPosX());
			int j = MathHelper.floor(this.getPosY());
			int k = MathHelper.floor(this.getPosZ());
			if (this.isInWaterRainOrBubbleColumn()) {
				this.attackEntityFrom(DamageSource.DROWN, 1.0F);
			}

			if (this.world.getBiome(new BlockPos(i, 0, k)).getTemperature(new BlockPos(i, j, k)) > 1.0F) {
				this.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
			}

			if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
				return;
			}

			BlockState blockstate = ArcanaBlocks.TAINTED_SNOW.get().getDefaultState();

			for(int l = 0; l < 4; ++l) {
				i = MathHelper.floor(this.getPosX() + (double)((float)(l % 2 * 2 - 1) * 0.25F));
				j = MathHelper.floor(this.getPosY());
				k = MathHelper.floor(this.getPosZ() + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
				BlockPos blockpos = new BlockPos(i, j, k);
				if (this.world.isAirBlock(blockpos) && this.world.getBiome(blockpos).getTemperature(blockpos) < 0.8F && blockstate.isValidPosition(this.world, blockpos)) {
					this.world.setBlockState(blockpos, blockstate);
				}
			}
		}

	}
}
