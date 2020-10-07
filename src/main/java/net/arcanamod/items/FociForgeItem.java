package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.multiblocks.foci_forge.FociForgeComponentBlock;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FociForgeItem extends Item{
	
	public FociForgeItem(Properties properties){
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
		
		BlockItemUseContext blockCtx = new BlockItemUseContext(context);
		ItemStack stack = player.getHeldItem(context.getHand());
		if(!world.getBlockState(pos).isReplaceable(blockCtx))
			pos = pos.up();
		
		boolean canModify = true;
		BlockPos.Mutable point = new BlockPos.Mutable();
		
		for(int x = 0; x < 2; x++)
			for(int y = 0; y < 2; y++)
				for(int z = 0; z < 2; z++){
					canModify = canModify && player.canPlayerEdit(point.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z), context.getFace(), stack)
								&& (world.getBlockState(point).isReplaceable(blockCtx) || world.getBlockState(point).isAir(world, point));
					if(!canModify)
						break;
				}
		
		if(canModify){
			BlockState coreState = ArcanaBlocks.FOCI_FORGE.get().getDefaultState();
			BlockState defaultComponentState = ArcanaBlocks.FOCI_FORGE_COMPONENT.get().getDefaultState();
			
			for(int x = 0; x < 2; x++)
				for(int y = 0; y < 2; y++)
					for(int z = 0; z < 2; z++){
						point.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
						if(x == 0 && y == 0 && z == 0){
							// place core
							world.setBlockState(point, coreState);
						}else{
							// place component
							world.setBlockState(point, defaultComponentState.with(FociForgeComponentBlock.TOP, y > 0).with(FociForgeComponentBlock.RIGHT, z > 0).with(FociForgeComponentBlock.FORWARD, x > 0));
						}
					}
			
			SoundType soundtype = coreState.getBlock().getSoundType(coreState, world, pos, player);
			world.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			
			if(player instanceof ServerPlayerEntity)
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)player, pos, stack);
			
			stack.shrink(1);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}
}