package net.kineticdevelopment.arcana.common.objects.items;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.utilities.IHasModel;
import net.minecraft.item.Item;

/**
 * Basic Item, all Items should either be this, or extend it
 * 
 * @author Atlas
 */
public class ItemBase extends Item implements IHasModel {
	
	public ItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);

		if(shouldRegister())
			ItemInit.ITEMS.add(this);
	}

	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}

	// TODO: stop registering things automatically, its bad design and requires stupid things like this to get around.
	protected boolean shouldRegister(){
		return true;
	}
}
