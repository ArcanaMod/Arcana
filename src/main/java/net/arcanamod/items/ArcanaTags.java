package net.arcanamod.items;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

import static net.arcanamod.Arcana.arcLoc;

public class ArcanaTags{
	
	public static final ITag<Item> SCRIBING_TOOLS = ItemTags.makeWrapperTag(arcLoc("scribing_tools").toString());
}