package net.kineticdevelopment.arcana.common.objects.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.BlockBase;
import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class BlockResearchTable extends BlockBase implements ITileEntityProvider{
	
	public BlockResearchTable(){
		super("research_table", Material.WOOD);
		translucent = true;
	}
	
	public boolean hasTileEntity(IBlockState state){
		return true;
	}
	
	@Nullable
	@ParametersAreNonnullByDefault
	public TileEntity createNewTileEntity(World worldIn, int meta){
		return new ResearchTableTileEntity();
	}
	
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos){
		return false;
	}
	
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	public BlockRenderLayer getBlockLayer(){
		return BlockRenderLayer.TRANSLUCENT;
	}
}