package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.research.Requirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class ItemRequirement extends Requirement{
	
	// perhaps support NBT in the future? will be required for enchantments in the future at least.
	private Item item;
	
	public ItemRequirement(Item item){
		this.item = item;
	}
	
	public boolean satisfied(EntityPlayer player){
		return player.inventory.clearMatchingItems(item, -1, 0, null) >= getCount();
	}
	
	public void take(EntityPlayer player){
		player.inventory.clearMatchingItems(item, -1, getCount(), null);
	}
}