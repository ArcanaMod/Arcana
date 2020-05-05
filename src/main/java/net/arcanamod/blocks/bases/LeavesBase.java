package net.arcanamod.blocks.bases;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.OreDictEntry;
import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.items.ArcanaItems;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Base Leaves, all leaves should either be this, or extend it
 *
 * @author Mozaran
 * @see LeavesBase
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class LeavesBase extends LeavesBlock implements IHasModel, OreDictEntry{
	String name;
	
	public LeavesBase(String name){
		this.name = name;
		Arcana.proxy.setGraphicsLevel(this, true);
		setDefaultState(blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.TRUE).withProperty(DECAYABLE, Boolean.TRUE));
		setUnlocalizedName(name);
		setRegistryName(name);
		
		ArcanaBlocks.BLOCKS.add(this);
		ArcanaItems.ITEMS.add(new BlockItem(this).setRegistryName(this.getRegistryName()));
	}
	
	public String getOreDictName(){
		return "treeLeaves";
	}
	
	@Override
	public BlockPlanks.EnumType getWoodType(int meta){
		return null;
	}
	
	@Override
	public Item getItemDropped(BlockState state, Random rand, int fortune){
		Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(Arcana.MODID, name.replace("leaves", "sapling")));
		if(block == null)
			return Item.getItemFromBlock(this);
		return Item.getItemFromBlock(block);
	}
	
	@Override
	public void getSubBlocks(ItemGroup itemIn, NonNullList<ItemStack> items){
		items.add(new ItemStack(this));
	}
	
	@Override
	protected ItemStack getSilkTouchDrop(BlockState state){
		return new ItemStack(Item.getItemFromBlock(this));
	}
	
	@Override
	public BlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
	}
	
	@Override
	public int getMetaFromState(BlockState state){
		int i = 0;
		
		if(!state.getValue(DECAYABLE).booleanValue()){
			i |= 4;
		}
		
		if(state.getValue(CHECK_DECAY).booleanValue()){
			i |= 8;
		}
		
		return i;
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
	}
	
	@Override
	public int damageDropped(BlockState state){
		return 0;
	}
	
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack){
		if(!worldIn.isRemote && stack.getItem() == Items.SHEARS){
			player.addStat(Stats.getBlockStats(this));
		}else{
			super.harvestBlock(worldIn, player, pos, state, te, stack);
		}
	}
	
	public NonNullList<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune){
		return NonNullList.withSize(1, new ItemStack(this));
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
