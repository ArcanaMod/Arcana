package net.kineticdevelopment.arcana.init;

import java.util.ArrayList;
import java.util.List;

import net.kineticdevelopment.arcana.common.items.Amber;
import net.kineticdevelopment.arcana.common.items.Quicksilver;
import net.kineticdevelopment.arcana.common.items.Thaumonomicon;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.item.Item;

public class iteminit 
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item THAUMONOMICON = new Thaumonomicon("thaumonomicon").setCreativeTab(Main.tabArcana);
	
	public static final Item AMBER = new Amber("amber").setCreativeTab(Main.tabArcana);
	
	public static final Item QUICKSILVER = new Quicksilver("quicksilver").setCreativeTab(Main.tabArcana);
}
