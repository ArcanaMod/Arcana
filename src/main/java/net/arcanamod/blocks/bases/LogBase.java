package net.arcanamod.blocks.bases;

import net.arcanamod.blocks.OreDictEntry;
import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Basic Logs, all logs should either be this, or extend it
 *
 * @author Tea, Mozaran
 */
public class LogBase extends LogBlock implements IHasModel, OreDictEntry{
	
	public LogBase(String name){
		this.setDefaultState(this.getStateFromMeta(0));
		setUnlocalizedName(name);
		setRegistryName(name);
		setSoundType(SoundType.WOOD);
		setHardness(2.0f);
		setResistance(2.0f);
		setHarvestLevel("axe", 0);
		
		ArcanaBlocks.BLOCKS.add(this);
		ArcanaItems.ITEMS.add(new BlockItem(this).setRegistryName(this.getRegistryName()));
	}
	
	public String getOreDictName(){
		return "logWood";
	}
	
	@Override
	public MaterialColor getMapColor(BlockState state, IBlockAccess worldIn, BlockPos pos){
		return Blocks.LOG.getDefaultState().getMapColor(worldIn, pos);
	}
	
	@Override
	public void getSubBlocks(ItemGroup itemIn, NonNullList<ItemStack> items){
		items.add(new ItemStack(this));
	}
	
	@Override
	public BlockState getStateFromMeta(int meta){
		BlockState state = this.getDefaultState();
		
		switch(meta & 12){
			case 0:
				state = state.withProperty(LOG_AXIS, LogBlock.EnumAxis.Y);
				break;
			case 4:
				state = state.withProperty(LOG_AXIS, LogBlock.EnumAxis.X);
				break;
			case 8:
				state = state.withProperty(LOG_AXIS, LogBlock.EnumAxis.Z);
				break;
			default:
				state = state.withProperty(LOG_AXIS, LogBlock.EnumAxis.NONE);
		}
		
		return state;
	}
	
	@Override
	@SuppressWarnings("incomplete-switch")
	public int getMetaFromState(BlockState state){
		int meta = 0;
		
		switch(state.getValue(LOG_AXIS)){
			case X:
				meta |= 4;
				break;
			case Z:
				meta |= 8;
				break;
			case NONE:
				meta |= 12;
		}
		
		return meta;
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, LOG_AXIS);
	}
	
	@Override
	protected ItemStack getSilkTouchDrop(BlockState state){
		return new ItemStack(Item.getItemFromBlock(this), 1);
	}
	
	@Override
	public int damageDropped(BlockState state){
		return 0;
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}

