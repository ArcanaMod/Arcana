package net.kineticdevelopment.arcana.common.init;

import net.kineticdevelopment.arcana.common.items.attachment.Cap;
import net.kineticdevelopment.arcana.common.objects.items.ItemFocus;
import net.kineticdevelopment.arcana.common.objects.items.ItemWand;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
	public static final List<Item> ITEMS = new ArrayList<Item>();

	public static Item FOCUS = new ItemFocus("focus");

	// Wand stuff
	public static Item WOOD_WAND = new ItemWand("wood_wand").setCreativeTab(Main.ARCANA);

	// Wand Attachments
	public static Item IRON_CAP = new Cap("iron_cap").setId(0);
	public static Item GOLD_CAP = new Cap("gold_cap").setId(1);
	public static Item THAUMIUM_CAP = new Cap("thaumium_cap");
	public static Item VOID_CAP = new Cap("void_cap").setId(3);
	public static Item COPPER_CAP = new Cap("copper_cap").setId(4);
	public static Item ELEMENTIUM_CAP = new Cap("elementium_cap").setId(5);
	public static Item MANASTEEL_CAP = new Cap("manasteel_cap").setId(6);
	public static Item TERRASTEEL_CAP = new Cap("terrasteel_cap").setId(7);



	
}
