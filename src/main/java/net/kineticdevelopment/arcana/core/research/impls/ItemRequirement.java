package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.Requirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemRequirement extends Requirement{
	
	// perhaps support NBT in the future? will be required for enchantments in the future at least.
	protected Item item;
	
	public static final ResourceLocation TYPE = new ResourceLocation(Main.MODID, "item");
	
	public ItemRequirement(Item item){
		this.item = item;
	}
	
	public boolean satisfied(EntityPlayer player){
		return player.inventory.clearMatchingItems(item, -1, 0, null) >= getAmount();
	}
	
	public void take(EntityPlayer player){
		player.inventory.clearMatchingItems(item, -1, getAmount(), null);
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public NBTTagCompound data(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("itemType", String.valueOf(ForgeRegistries.ITEMS.getKey(item)));
		return compound;
	}
}