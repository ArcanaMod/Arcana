package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.CrystalClusterBlock;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Copy of BlockItem, but allows specifying growth stage & uses different render layer.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrystalClusterItem extends Item{
	
	private final Block block;
	private int stage;
	
	public CrystalClusterItem(Block block, Item.Properties builder, int stage){
		super(builder);
		this.block = block;
		this.stage = stage;
	}
	
	/**
	 * Called when this item is used when targetting a Block
	 */
	public ActionResultType onItemUse(ItemUseContext context){
		ActionResultType actionresulttype = this.tryPlace(new BlockItemUseContext(context));
		return actionresulttype != ActionResultType.SUCCESS && this.isFood() ? this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType() : actionresulttype;
	}
	
	public ActionResultType tryPlace(BlockItemUseContext context){
		if(!context.canPlace())
			return ActionResultType.FAIL;
		else{
			BlockItemUseContext ctx = getBlockItemUseContext(context);
			if(ctx == null)
				return ActionResultType.FAIL;
			else{
				BlockState blockstate = getStateForPlacement(ctx);
				if(blockstate == null)
					return ActionResultType.FAIL;
				else if(!placeBlock(ctx, blockstate.with(CrystalClusterBlock.AGE, stage)))
					return ActionResultType.FAIL;
				else{
					BlockPos blockpos = ctx.getPos();
					World world = ctx.getWorld();
					PlayerEntity playerentity = ctx.getPlayer();
					ItemStack itemstack = ctx.getItem();
					BlockState blockstate1 = world.getBlockState(blockpos);
					Block block = blockstate1.getBlock();
					if(block == blockstate.getBlock()){
						blockstate1 = func_219985_a(blockpos, world, itemstack, blockstate1);
						onBlockPlaced(blockpos, world, playerentity, itemstack, blockstate1);
						block.onBlockPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
						if(playerentity instanceof ServerPlayerEntity)
							CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerentity, blockpos, itemstack);
					}
					
					SoundType soundtype = blockstate1.getSoundType(world, blockpos, context.getPlayer());
					world.playSound(playerentity, blockpos, this.getPlaceSound(blockstate1, world, blockpos, context.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					itemstack.shrink(1);
					return ActionResultType.SUCCESS;
				}
			}
		}
	}
	
	protected SoundEvent getPlaceSound(BlockState state, World world, BlockPos pos, PlayerEntity entity){
		return state.getSoundType(world, pos, entity).getPlaceSound();
	}
	
	@Nullable
	public BlockItemUseContext getBlockItemUseContext(BlockItemUseContext context){
		return context;
	}
	
	protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state){
		return setTileEntityNBT(worldIn, player, pos, stack);
	}
	
	@Nullable
	protected BlockState getStateForPlacement(BlockItemUseContext context){
		BlockState blockstate = this.getBlock().getStateForPlacement(context);
		return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
	}
	
	private BlockState func_219985_a(BlockPos pos, World world, ItemStack stack, BlockState state){
		BlockState blockstate = state;
		CompoundNBT compoundnbt = stack.getTag();
		if(compoundnbt != null){
			CompoundNBT tag = compoundnbt.getCompound("BlockStateTag");
			StateContainer<Block, BlockState> statecontainer = state.getBlock().getStateContainer();
			
			for(String s : tag.keySet()){
				Property<?> prop = statecontainer.getProperty(s);
				if(prop != null){
					String s1 = tag.get(s).getString();
					blockstate = func_219988_a(blockstate, prop, s1);
				}
			}
		}
		
		if(blockstate != state)
			world.setBlockState(pos, blockstate, 2);
		
		return blockstate;
	}
	
	private static <T extends Comparable<T>> BlockState func_219988_a(BlockState state, Property<T> prop, String str){
		return prop.parseValue(str).map((val) -> state.with(prop, val)).orElse(state);
	}
	
	protected boolean canPlace(BlockItemUseContext p_195944_1_, BlockState p_195944_2_){
		PlayerEntity playerentity = p_195944_1_.getPlayer();
		ISelectionContext iselectioncontext = playerentity == null ? ISelectionContext.dummy() : ISelectionContext.forEntity(playerentity);
		return (!this.checkPosition() || p_195944_2_.isValidPosition(p_195944_1_.getWorld(), p_195944_1_.getPos())) && p_195944_1_.getWorld().placedBlockCollides(p_195944_2_, p_195944_1_.getPos(), iselectioncontext);
	}
	
	protected boolean checkPosition(){
		return true;
	}
	
	protected boolean placeBlock(BlockItemUseContext context, BlockState state){
		return context.getWorld().setBlockState(context.getPos(), state, 11);
	}
	
	public static boolean setTileEntityNBT(World worldIn, @Nullable PlayerEntity player, BlockPos pos, ItemStack stackIn){
		MinecraftServer minecraftserver = worldIn.getServer();
		if(minecraftserver != null){
			CompoundNBT compoundnbt = stackIn.getChildTag("BlockEntityTag");
			if(compoundnbt != null){
				TileEntity tileentity = worldIn.getTileEntity(pos);
				if(tileentity != null){
					if(!worldIn.isRemote && tileentity.onlyOpsCanSetNbt() && (player == null || !player.canUseCommandBlock()))
						return false;
					
					CompoundNBT compoundnbt1 = tileentity.write(new CompoundNBT());
					CompoundNBT compoundnbt2 = compoundnbt1.copy();
					compoundnbt1.merge(compoundnbt);
					compoundnbt1.putInt("x", pos.getX());
					compoundnbt1.putInt("y", pos.getY());
					compoundnbt1.putInt("z", pos.getZ());
					if(!compoundnbt1.equals(compoundnbt2)){
						tileentity.read(tileentity.getBlockState(), compoundnbt1);
						tileentity.markDirty();
						return true;
					}
				}
			}
			
		}
		return false;
	}
	
	public String getTranslationKey(){
		return stage == 3 ? getBlock().getTranslationKey() : super.getTranslationKey();
	}
	
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
		super.addInformation(stack, worldIn, tooltip, flagIn);
		getBlock().addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	public Block getBlock(){
		return getBlockRaw().delegate.get();
	}
	
	private Block getBlockRaw(){
		return block;
	}
}