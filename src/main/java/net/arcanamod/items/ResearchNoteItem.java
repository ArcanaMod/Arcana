package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.client.ClientUtils;
import net.arcanamod.systems.research.Puzzle;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.systems.research.ResearchEntry;
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
				// If this note is associated with a research entry,
				if(compound.contains("research")){
					ResourceLocation research = new ResourceLocation(compound.getString("research"));
					ResearchEntry entry = ResearchBooks.getEntry(research);
					// and that entry only has one requirement,
					int stage = from.entryStage(entry);
					if(stage < entry.sections().size() && entry.sections().get(stage).getRequirements().size() == 1)
						// continue straight away.
						from.advanceEntry(entry);
					// display a toast
					if(world.isRemote())
						ClientUtils.displayPuzzleToast(entry);
				}else if(world.isRemote())
					ClientUtils.displayPuzzleToast(null);
				return new ActionResult<>(ActionResultType.SUCCESS, stack);
			}
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag){
		CompoundNBT compound = stack.getTag();
		if(compound != null && compound.contains("research")){
			ResearchEntry research = ResearchBooks.getEntry(new ResourceLocation(compound.getString("research")));
			if(research != null)
				tooltip.add(new TranslationTextComponent(research.name()).mergeStyle(TextFormatting.AQUA));
		}
	}
}