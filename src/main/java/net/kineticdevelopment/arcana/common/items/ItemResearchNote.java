package net.kineticdevelopment.arcana.common.items;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.common.objects.items.ItemBase;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.core.research.Researcher;
import net.kineticdevelopment.arcana.core.research.ResearchBooks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemResearchNote extends ItemBase{
	
	private boolean isComplete;
	
	public ItemResearchNote(String name, boolean complete){
		super(name);
		isComplete = complete;
		setMaxStackSize(1);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		if(!isComplete)
			return super.onItemRightClick(world, player, hand);
		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound compound = stack.getTagCompound();
		if(compound != null && compound.hasKey("puzzle")){
			Researcher from = Researcher.getFrom(player);
			Puzzle puzzle = ResearchBooks.puzzles.get(new ResourceLocation(compound.getString("puzzle")));
			if(!from.isPuzzleCompleted(puzzle)){
				from.completePuzzle(puzzle);
				if(!player.capabilities.isCreativeMode)
					stack.shrink(1);
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag){
		NBTTagCompound compound = stack.getTagCompound();
		if(compound != null && compound.hasKey("research")){
			ResourceLocation research = new ResourceLocation(compound.getString("research"));
			tooltip.add(TextFormatting.AQUA + I18n.format(ResearchBooks.getEntry(research).name()));
		}
	}
}