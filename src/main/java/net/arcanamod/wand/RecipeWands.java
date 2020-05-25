package net.arcanamod.wand;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.ArcanaRecipes;
import net.arcanamod.items.WandItem;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.CoreItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipeWands extends SpecialRecipe{
	
	public RecipeWands(ResourceLocation id){
		super(id);
	}
	
	public boolean matches(CraftingInventory inv, World worldIn){
		if(inv.getWidth() >= 3 && inv.getHeight() >= 3){
			// check TL slot
			// if there's a cap, TL->BR
			// else, TR->BL
			
			ItemStack TL = inv.getStackInSlot(0);
			if(TL.getItem() instanceof Cap){
				if(TL.isItemEqual(inv.getStackInSlot(8)))
					return isCore(inv.getStackInSlot(4)) && toCore(inv.getStackInSlot(4)).capAllowed((Cap)TL.getItem());
			}else{
				ItemStack stack = inv.getStackInSlot(2);
				if(stack.getItem() instanceof Cap)
					if(stack.isItemEqual(inv.getStackInSlot(6)))
						return isCore(inv.getStackInSlot(4)) && toCore(inv.getStackInSlot(4)).capAllowed((Cap)stack.getItem());
			}
		}
		return false;
	}
	
	public ItemStack getCraftingResult(CraftingInventory inv){
		Cap caps;
		if(inv.getStackInSlot(0).getItem() instanceof Cap)
			caps = (Cap)inv.getStackInSlot(0).getItem();
		else
			caps = (Cap)inv.getStackInSlot(2).getItem();
		Item item = inv.getStackInSlot(4).getItem();
		if(item == Items.STICK)
			return WandItem.withCapAndCore(caps, ArcanaItems.WOOD_WAND_CORE);
		return item instanceof CoreItem ? WandItem.withCapAndCore(caps, (Core)item) : null;
	}
	
	public boolean canFit(int width, int height){
		return width >= 3 && height >= 3;
	}
	
	public IRecipeSerializer<?> getSerializer(){
		return ArcanaRecipes.CRAFING_WANDS.get();
	}
	
	private static boolean isCore(@Nullable ItemStack stack){
		return stack != null && (stack.getItem() instanceof CoreItem || stack.getItem() == Items.STICK);
	}
	
	private static Core toCore(@Nonnull ItemStack stack){
		if(stack.getItem() instanceof CoreItem)
			return ((CoreItem)stack.getItem());
		if(stack.getItem() == Items.STICK)
			return ArcanaItems.WOOD_WAND_CORE;
		return null;
	}
}