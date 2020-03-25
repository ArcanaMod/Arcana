package net.kineticdevelopment.arcana.common.objects.items;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.kineticdevelopment.arcana.common.objects.blocks.BlockResearchTable.EnumSide.RIGHT;
import static net.kineticdevelopment.arcana.common.objects.blocks.BlockResearchTable.PART;

@MethodsReturnNonnullByDefault
public class ItemResearchTable extends ItemBase{
	
	public ItemResearchTable(){
		super("research_table_placer");
		setMaxDamage(0);
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(world.isRemote)
			return EnumActionResult.SUCCESS;
		if(!world.getBlockState(pos).getBlock().isReplaceable(world, pos))
			pos = pos.up();
		EnumFacing direction = player.getHorizontalFacing();
		BlockPos blockpos = pos.offset(direction.rotateY());
		ItemStack itemStack = player.getHeldItem(hand);
		if(player.canPlayerEdit(pos, facing, itemStack) && player.canPlayerEdit(blockpos, facing, itemStack)){
			boolean canReplaceMain = world.getBlockState(pos).getBlock().isReplaceable(world, pos) || world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos);
			boolean canReplaceSecond = world.getBlockState(blockpos).getBlock().isReplaceable(world, blockpos) || world.getBlockState(blockpos).getBlock().isAir(world.getBlockState(blockpos), world, blockpos);
			if(canReplaceMain && canReplaceSecond){
				IBlockState state = BlockInit.RESEARCH_TABLE.getStateForPlacement(world, pos, direction, hitX, hitY, hitZ, 0, player, hand);
				world.setBlockState(pos, state, 10);
				world.setBlockState(blockpos, state.withProperty(PART, RIGHT), 10);
				
				// play sound
				SoundType soundtype = state.getBlock().getSoundType(state, world, pos, player);
				world.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				
				// update world
				// this should be updated to follow ItemBed, I don't think this will cause problems but still
				world.notifyNeighborsRespectDebug(pos, state.getBlock(), false);
				world.notifyNeighborsRespectDebug(blockpos, state.getBlock(), false);
				
				if(player instanceof EntityPlayerMP)
					CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemStack);
				
				itemStack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
		}
		return  EnumActionResult.FAIL;
	}
}