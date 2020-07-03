package net.arcanamod;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class SupplierItemGroup extends ItemGroup{

	private Supplier<ItemStack> iconSupplier;
	private boolean hasSearchBar = false;
	
	public SupplierItemGroup(String name, Supplier<ItemStack> iconSupplier){
		super(name);
		this.iconSupplier = iconSupplier;
	}
	
	public SupplierItemGroup setHasSearchBar(boolean hasSearchBar){
		this.hasSearchBar = hasSearchBar;
		return this;
	}
	
	public boolean hasSearchBar(){
		return hasSearchBar;
	}
	
	public int getSearchbarWidth(){
		return 80;
	}
	
	@Nonnull
	public ItemStack createIcon(){
		return iconSupplier.get();
	}
}
