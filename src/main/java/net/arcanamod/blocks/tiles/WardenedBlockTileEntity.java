package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.Objects;
import java.util.Optional;

@ParametersAreNullableByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "RedundantTypeArguments", "unused"})
public class WardenedBlockTileEntity extends TileEntity {
	private Optional<BlockState> copyState = Optional.of(Blocks.CLAY.getDefaultState());;

	public WardenedBlockTileEntity() {
		super(ArcanaTiles.WARDENED_BLOCK_TE.get());
	}

	public void setState(Optional<BlockState> state) {
		copyState = state;
	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		if (copyState.orElse(Blocks.BEDROCK.getDefaultState()).getBlock() != ArcanaBlocks.WARDENED_BLOCK.get().getBlock())
			tag.putString("type", Objects.requireNonNull(copyState.orElse(Blocks.BEDROCK.getDefaultState()).getBlock().getRegistryName()).toString());

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

	//  When the world loads from disk, the server needs to send the TileEntity information to the client
	//  it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this:
	//  getUpdatePacket() and onDataPacket() are used for one-at-a-time TileEntity updates
	//  getUpdateTag() and handleUpdateTag() are used by vanilla to collate together into a single chunk update packet
	//  Not really required for this example since we only use the timer on the client, but included anyway for illustration
	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket(){
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		int tileEntityType = ArcanaTiles.WARDENED_BLOCK_TE.hashCode();
		return new SUpdateTileEntityPacket(this.pos, tileEntityType, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		read(pkt.getNbtCompound());
	}

	/* Creates a tag containing all of the TileEntity information, used by vanilla to transmit from server to client */
	@Override
	public CompoundNBT getUpdateTag(){
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		return nbtTagCompound;
	}

	/* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client */
	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		this.read(tag);
	}
}
