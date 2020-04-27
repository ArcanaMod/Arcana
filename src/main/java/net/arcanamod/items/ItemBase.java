package net.arcanamod.items;

import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.minecraft.item.Item;

/**
 * Basic Item, all Items should either be this, or extend it
 *
 * @author Atlas
 */
public class ItemBase extends Item implements IHasModel{
	
	public ItemBase(String name){
		setUnlocalizedName(name);
		setRegistryName(name);
		
		if(shouldRegister())
			ArcanaItems.ITEMS.add(this);
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
	// TODO: stop registering things automatically, its bad design and requires stupid things like this to get around.
	protected boolean shouldRegister(){
		return true;
	}
}
