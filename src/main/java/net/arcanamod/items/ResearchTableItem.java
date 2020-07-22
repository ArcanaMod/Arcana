package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.blocks.ResearchTableBlock.EnumSide.RIGHT;
import static net.arcanamod.blocks.ResearchTableBlock.PART;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ResearchTableItem extends Item{
	
	public ResearchTableItem(Properties properties){
		super(properties);
	}
	
	public ActionResultType onItemUse(ItemUseContext context){
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();
		if(world.isRemote)
			return ActionResultType.SUCCESS;
		if(player == null)
			return ActionResultType.FAIL;
		
		BlockItemUseContext itemUseContext = new BlockItemUseContext(context);
		if(!world.getBlockState(pos).isReplaceable(itemUseContext))
			pos = pos.up();
		
		Direction direction = context.getPlacementHorizontalFacing();
		BlockPos blockpos = pos.offset(direction.rotateY());
		ItemStack itemStack = player.getHeldItem(context.getHand());
		if(player.canPlayerEdit(pos, context.getFace(), itemStack) && player.canPlayerEdit(blockpos, context.getFace(), itemStack)){
			boolean canReplaceMain = world.getBlockState(pos).isReplaceable(itemUseContext) || world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos);
			boolean canReplaceSecond = world.getBlockState(blockpos).isReplaceable(itemUseContext) || world.getBlockState(blockpos).getBlock().isAir(world.getBlockState(blockpos), world, blockpos);
			if(canReplaceMain && canReplaceSecond){
				BlockState state = ArcanaBlocks.RESEARCH_TABLE.get().getStateForPlacement(itemUseContext);
				if(state != null){
					world.setBlockState(pos, state, 10);
					world.setBlockState(blockpos, state.with(PART, RIGHT), 10);
					
					// play sound
					SoundType soundtype = state.getBlock().getSoundType(state, world, pos, player);
					world.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					
					if(player instanceof ServerPlayerEntity)
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)player, pos, itemStack);
					
					BlockItem.setTileEntityNBT(world, player, pos, itemStack);
					
					itemStack.shrink(1);
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.FAIL;
	}
}