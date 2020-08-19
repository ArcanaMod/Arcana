package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.tiles.AspectCrystallizerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectCrystallizerBlock extends Block{
	
	public AspectCrystallizerBlock(Properties properties){
		super(properties);
	}
	
	public boolean hasTileEntity(BlockState state){
		return true;
	}
	
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new AspectCrystallizerTileEntity();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult){
		if(world.isRemote)
			return ActionResultType.SUCCESS;
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof AspectCrystallizerTileEntity){
			NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)te, buf -> buf.writeBlockPos(pos));
			return ActionResultType.SUCCESS;
		}
		return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
	}
	
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving){
		if(state.getBlock() != newState.getBlock()){
			TileEntity tileentity = world.getTileEntity(pos);
			if(tileentity instanceof IInventory){
				InventoryHelper.dropInventoryItems(world, pos, (IInventory)tileentity);
				world.updateComparatorOutputLevel(pos, this);
			}
			
			super.onReplaced(state, world, pos, newState, moving);
		}
	}
}