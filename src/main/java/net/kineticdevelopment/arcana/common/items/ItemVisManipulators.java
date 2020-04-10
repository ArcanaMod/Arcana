package net.kineticdevelopment.arcana.common.items;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.ArcanaGuiHandler;
import net.kineticdevelopment.arcana.common.objects.items.ItemBase;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemVisManipulators extends ItemBase{
	
	public ItemVisManipulators(){
		this("vis_manipulation_tools");
		setMaxStackSize(1);
	}
	
	public ItemVisManipulators(String name){
		super(name);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		player.openGui(Main.instance, ArcanaGuiHandler.VIS_MANIPULATORS_ID, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		return super.onItemRightClick(world, player, hand);
	}
}