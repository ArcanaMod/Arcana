package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkModifyResearch;
import net.arcanamod.systems.research.ResearchBooks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CheatersResearchBookItem extends ResearchBookItem{
	
	public CheatersResearchBookItem(Properties properties, ResourceLocation book){
		super(properties, book);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand){
		// also grant all research
		if(!world.isRemote() && player instanceof ServerPlayerEntity)
			ResearchBooks.streamEntries().forEach(entry -> {
				Researcher from = Researcher.getFrom(player);
				if(from != null){
					from.completeEntry(entry);
					Connection.sendModifyResearch(PkModifyResearch.Diff.complete, entry.key(), (ServerPlayerEntity)player);
				}
			});
		return super.onItemRightClick(world, player, hand);
	}
	
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag){
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(new TranslationTextComponent("item.arcana.cheaters_arcanum.desc"));
	}
}