package net.arcanamod.wand;

import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.items.ItemWand;
import net.arcanamod.items.attachment.WandCore;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.attachment.Cap;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipeWands extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe{
	
	// I can either make WandCore into a capability, or add special support for sticks just here. I prefer the latter.
	
	public boolean matches(InventoryCrafting inv, World world){
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
	
	public ItemStack getCraftingResult(InventoryCrafting inv){
		Cap caps;
		if(inv.getStackInSlot(0).getItem() instanceof Cap)
			caps = (Cap)inv.getStackInSlot(0).getItem();
		else
			caps = (Cap)inv.getStackInSlot(2).getItem();
		if(inv.getStackInSlot(4).getItem() == Items.STICK)
			return WandCore.createWoodenWandWithAttachments(caps);
		return ((WandCore)inv.getStackInSlot(4).getItem()).getWandWithAttachments(caps);
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
	
	private static boolean isCore(@Nullable ItemStack stack){
		return stack != null && (stack.getItem() instanceof WandCore || stack.getItem() == Items.STICK);
	}
	
	private static ItemWand toCore(@Nonnull ItemStack stack){
		if(stack.getItem() instanceof WandCore)
			return ((WandCore)stack.getItem()).getWand();
		if(stack.getItem() == Items.STICK)
			return ArcanaItems.WOOD_WAND;
		return null;
	}
	
	@SuppressWarnings("unused") // Referenced in _factories.json
	public static class Factory implements IRecipeFactory{
		
		public IRecipe parse(JsonContext context, JsonObject json){
			return new RecipeWands();
		}
	}
}