package net.kineticdevelopment.arcana.common.init;

import java.util.ArrayList;
import java.util.List;

import net.kineticdevelopment.arcana.common.items.ItemFocus;
import net.kineticdevelopment.arcana.common.items.ItemWand;
import net.minecraft.item.Item;

public class ItemInit {
	public static final List<Item> ITEMS = new ArrayList<Item>();

	public static Item FOCUS = new ItemFocus("focus");
	public static Item WAND = new ItemWand("wand");
	
}
