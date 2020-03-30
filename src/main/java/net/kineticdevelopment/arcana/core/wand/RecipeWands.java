package net.kineticdevelopment.arcana.core.wand;

import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.common.items.attachment.Cap;
import net.kineticdevelopment.arcana.common.items.attachment.WandCore;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipeWands extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe{
	
	public boolean matches(InventoryCrafting inv, World world){
		if(inv.getWidth() >= 3 && inv.getHeight() >= 3)
			// check TL slot
			// if there's a cap, TL->BR
			// else, TR->BL
			if(inv.getStackInSlot(0).getItem() instanceof Cap){
				if(inv.getStackInSlot(0).isItemEqual(inv.getStackInSlot(8)))
					return inv.getStackInSlot(4).getItem() instanceof WandCore;
			}else if(inv.getStackInSlot(2).getItem() instanceof Cap)
				if(inv.getStackInSlot(2).isItemEqual(inv.getStackInSlot(6)))
					return inv.getStackInSlot(4).getItem() instanceof WandCore;
		return false;
	}
	
	public ItemStack getCraftingResult(InventoryCrafting inv){
		Cap caps;
		WandCore core;
		if(inv.getStackInSlot(0).getItem() instanceof Cap)
			caps = (Cap)inv.getStackInSlot(0).getItem();
		else
			caps = (Cap)inv.getStackInSlot(2).getItem();
		core = (WandCore)inv.getStackInSlot(4).getItem();
		return core.getWandWithAttachments(caps);
	}
	
	public boolean canFit(int width, int height){
		return width >= 3 && height >= 3;
	}
	
	public ItemStack getRecipeOutput(){
		return ItemStack.EMPTY;
	}
	
	public boolean isDynamic(){
		return true;
	}
	
	public static class Factory implements IRecipeFactory{
		
		public IRecipe parse(JsonContext context, JsonObject json){
			return new RecipeWands();
		}
	}
}