package net.arcanamod;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class SupplierItemGroup extends ItemGroup{

	private Supplier<ItemStack> iconSupplier;
	private ResourceLocation backgroundImage;
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
	
	public SupplierItemGroup setBackgroundImage(@Nonnull ResourceLocation backgroundImage){
		this.backgroundImage = backgroundImage;
		return this;
	}
	
	@Nonnull
	public ResourceLocation getBackgroundImage(){
		return backgroundImage == null ? super.getBackgroundImage() : backgroundImage;
	}
	
	@Nonnull
	public ItemStack createIcon(){
		return iconSupplier.get();
	}
}
