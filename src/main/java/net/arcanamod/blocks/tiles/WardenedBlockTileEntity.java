package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNullableByDefault;
import java.util.Objects;
import java.util.Optional;

@ParametersAreNullableByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "RedundantTypeArguments", "unused"})
public class WardenedBlockTileEntity extends TileEntity {
	private Optional<BlockState> copyState = Optional.empty();

	public WardenedBlockTileEntity() {
		super(ArcanaTiles.WARDENED_BLOCK_TE.get());
	}

	public void setState(Optional<BlockState> state) {
		copyState = state;
	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		tag.putString("type", Objects.requireNonNull(copyState.orElse(Blocks.AIR.getDefaultState()).getBlock().getRegistryName()).toString());

		return super.write(tag);
	}

	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);
		copyState = Optional.<BlockState>of(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString("type")))).getDefaultState());
	}

	public Optional<BlockState> getState() {
		return copyState;
	}
}
