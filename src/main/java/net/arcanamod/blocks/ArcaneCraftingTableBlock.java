package net.arcanamod.blocks;

import net.arcanamod.blocks.bases.WaterloggableBlock;
import net.arcanamod.blocks.tiles.ArcaneCraftingTableTileEntity;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

import static net.arcanamod.blocks.ResearchTableBlock.EnumSide.RIGHT;

public class ArcaneCraftingTableBlock extends WaterloggableBlock {
	public ArcaneCraftingTableBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ArcaneCraftingTableTileEntity();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTraceResult) {
		if(world.isRemote)
			return ActionResultType.PASS;
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof ArcaneCraftingTableTileEntity)
		{
			NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, buf -> buf.writeBlockPos(pos));
			return ActionResultType.SUCCESS;
		}
		return super.onBlockActivated(state, world, pos, player, handIn, rayTraceResult);
	}
}
