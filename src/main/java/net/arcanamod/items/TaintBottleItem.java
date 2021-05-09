package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.entities.TaintBottleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaintBottleItem extends Item{
	
	public TaintBottleItem(Properties properties){
		super(properties);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand){
		world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		
		if(!world.isRemote()){
			TaintBottleEntity entity = new TaintBottleEntity(player, world);
			entity.shoot(player.getLookVec().getX(), player.getLookVec().getY(), player.getLookVec().getZ(), .5f, 1);
			world.addEntity(entity);
		}
		
		ItemStack itemstack = player.getHeldItem(hand);
		player.addStat(Stats.ITEM_USED.get(this));
		if(!player.abilities.isCreativeMode)
			itemstack.shrink(1);
		
		return ActionResult.resultSuccess(itemstack);
	}
}