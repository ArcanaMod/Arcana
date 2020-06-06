package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.ResearchBooks;
import net.arcanamod.research.Researcher;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ResearchNoteItem extends Item{
	
	private boolean isComplete;
	
	public ResearchNoteItem(Properties properties, boolean complete){
		super(properties);
		isComplete = complete;
		//setMaxStackSize(1);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand){
		if(!isComplete)
			return super.onItemRightClick(world, player, hand);
		ItemStack stack = player.getHeldItem(hand);
		CompoundNBT compound = stack.getTag();
		if(compound != null && compound.contains("puzzle")){
			Researcher from = Researcher.getFrom(player);
			Puzzle puzzle = ResearchBooks.puzzles.get(new ResourceLocation(compound.getString("puzzle")));
			if(!from.isPuzzleCompleted(puzzle)){
				from.completePuzzle(puzzle);
				if(!player.abilities.isCreativeMode)
					stack.shrink(1);
				return new ActionResult<>(ActionResultType.SUCCESS, stack);
			}
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag){
		CompoundNBT compound = stack.getTag();
		if(compound != null && compound.contains("research")){
			ResourceLocation research = new ResourceLocation(compound.getString("research"));
			//tooltip.add(TextFormatting.AQUA + I18n.format(ResearchBooks.getEntry(research).name()));
			tooltip.add(new TranslationTextComponent(ResearchBooks.getEntry(research).name()).applyTextStyle(TextFormatting.AQUA));
		}
	}
}