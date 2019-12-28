package net.kineticdevelopment.arcana.common.items;

import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.init.iteminit;
import net.kineticdevelopment.arcana.util.IHasModel;
import net.minecraft.item.Item;

public class Quicksilver extends Item implements IHasModel
{
	public Quicksilver(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		
		iteminit.ITEMS.add(this);
	}

	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
