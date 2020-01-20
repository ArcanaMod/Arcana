package net.kineticdevelopment.arcana.common.init;

import java.util.ArrayList;
import java.util.List;

import net.kineticdevelopment.arcana.common.objects.items.ItemFocus;
import net.kineticdevelopment.arcana.common.objects.items.ItemWand;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.item.Item;

public class ItemInit {
	public static final List<Item> ITEMS = new ArrayList<Item>();

	public static Item FOCUS = new ItemFocus("focus");
	public static Item WAND = new ItemWand("wand").setCreativeTab(Main.ARCANA);
	
}
