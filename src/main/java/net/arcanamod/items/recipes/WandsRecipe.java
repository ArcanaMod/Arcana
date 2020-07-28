package net.arcanamod.items.recipes;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.WandItem;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.CapItem;
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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WandsRecipe extends SpecialRecipe{
	
	// For items that aren't CapItems or CoreItems that are associated with a Cap or Core.
	// The item is counted as a cap or core by isCap and isCore if the function returns nonnull.
	// toCap and toCore will use the Function value.
	public static Map<Item, Function<ItemStack, Cap>> EXTRA_CAPS = new HashMap<>();
	public static Map<Item, Function<ItemStack, Core>> EXTRA_CORES = new HashMap<>();
	
	static{
		EXTRA_CORES.put(Items.STICK, __ -> ArcanaItems.WOOD_WAND_CORE);
	}
	
	public WandsRecipe(ResourceLocation id){
		super(id);
	}
	
	public boolean matches(CraftingInventory inv, World worldIn){
		if(inv.getWidth() >= 3 && inv.getHeight() >= 3){
			// check TL slot
			// if there's a cap, TL->BR
			// else, TR->BL
			
			ItemStack TL = inv.getStackInSlot(0);
			if(isCap(TL)){
				if(TL.isItemEqual(inv.getStackInSlot(8)))
					return isCore(inv.getStackInSlot(4)) && toCore(inv.getStackInSlot(4)).capAllowed(toCap(TL));
			}else{
				ItemStack stack = inv.getStackInSlot(2);
				if(isCap(stack))
					if(stack.isItemEqual(inv.getStackInSlot(6)))
						return isCore(inv.getStackInSlot(4)) && toCore(inv.getStackInSlot(4)).capAllowed(toCap(stack));
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
		return item instanceof CoreItem ? WandItem.withCapAndCore(caps, (Core)item) : WandItem.withCapAndCore(caps, EXTRA_CORES.get(item).apply(inv.getStackInSlot(4)));
	}
	
	public boolean canFit(int width, int height){
		return width >= 3 && height >= 3;
	}
	
	public IRecipeSerializer<?> getSerializer(){
		return ArcanaRecipes.Serializers.CRAFTING_WANDS.get();
	}
	
	private static boolean isCore(@Nullable ItemStack stack){
		return stack != null && (stack.getItem() instanceof CoreItem || (EXTRA_CORES.get(stack.getItem()) != null && EXTRA_CORES.get(stack.getItem()).apply(stack) != null));
	}
	
	private static Core toCore(@Nonnull ItemStack stack){
		if(stack.getItem() instanceof CoreItem)
			return ((CoreItem)stack.getItem());
		return EXTRA_CORES.get(stack.getItem()).apply(stack);
	}
	
	private static boolean isCap(@Nullable ItemStack stack){
		return stack != null && (stack.getItem() instanceof CapItem || (EXTRA_CAPS.get(stack.getItem()) != null && EXTRA_CAPS.get(stack.getItem()).apply(stack) != null));
	}
	
	private static Cap toCap(@Nonnull ItemStack stack){
		if(stack.getItem() instanceof CapItem)
			return ((CapItem)stack.getItem());
		return EXTRA_CAPS.get(stack.getItem()).apply(stack);
	}
}