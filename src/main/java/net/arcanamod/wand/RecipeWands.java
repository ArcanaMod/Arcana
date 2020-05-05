package net.arcanamod.wand;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.ItemWand;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.WandCore;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipeWands implements ICraftingRecipe, net.minecraftforge.common.crafting.IShapedRecipe<CraftingInventory>{
	
	// I can either make WandCore into a capability, or add special support for sticks just here. I prefer the latter.
	
	static final ResourceLocation id = new ResourceLocation(Arcana.MODID, "wands");
	
	public boolean matches(CraftingInventory inv, World world){
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
	
	public ResourceLocation getId(){
		return id;
	}
	
	public IRecipeSerializer<?> getSerializer(){
		// TODO: recipe serializer
		return null;
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
	
	public int getRecipeWidth(){
		return 3;
	}
	
	public int getRecipeHeight(){
		return 3;
	}
	
	/*@SuppressWarnings("unused") // Referenced in _factories.json
	public static class Factory implements IRecipeFactory{
		
		public IRecipe parse(JsonContext context, JsonObject json){
			return new RecipeWands();
		}
	}*/
}