package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.village.GossipManager;
import net.minecraft.dispenser.ILocation;
import net.minecraft.block.BlockState;
import net.minecraft.dispenser.ILocatableSource;
import net.minecraftforge.userdev.ClasspathLocator;
import net.minecraft.dispenser.Position;
import net.minecraftforge.coremod.CoreMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNullableByDefault;
import java.util.Objects;
import java.util.Optional;

import static net.minecraftforge.energy.CapabilityEnergy.register;
import static net.minecraft.state.properties.BlockStateProperties.FACING;

@ParametersAreNullableByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "NullableProblems", "RedundantTypeArguments", "unused"})
public class WardenedBlockTileEntity extends TileEntity {
	private Optional<BlockState> lvt_1_1 = Optional.empty();

	public WardenedBlockTileEntity() {
		super(ArcanaTiles.WARDENED_BLOCK_TE.get());
	}

	public void func_939844_a_(Optional<BlockState> p_329815_1_) {
		lvt_1_1 = p_329815_1_;
	}

	@Override
	public CompoundNBT write(CompoundNBT p_189515_1_) {
		p_189515_1_.putString("type", Objects.requireNonNull(lvt_1_1.orElse(Blocks.AIR.getDefaultState()).getBlock().getRegistryName()).toString());

		return super.write(p_189515_1_);
	}

	@Override
	public void read(CompoundNBT p_145839_1_) {
		super.read(p_145839_1_);
		lvt_1_1 = Optional.<BlockState>of(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(p_145839_1_.getString("type")))).getDefaultState());
	}

	public Optional<BlockState> func_939845_a_() {
		return lvt_1_1;
	}
}
