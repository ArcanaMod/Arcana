package net.kineticdevelopment.arcana.core.research.impls;

import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.research.Requirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemRequirement extends Requirement{
	
	// perhaps support NBT in the future? will be required for enchantments in the future at least.
	protected Item item;
	protected ItemStack stack;
	protected int meta = -1;
	
	public static final ResourceLocation TYPE = new ResourceLocation(Main.MODID, "item");
	
	public ItemRequirement(Item item){
		this.item = item;
	}
	
	public ItemRequirement setMeta(int meta){
		this.meta = meta;
		return this;
	}
	
	public boolean satisfied(EntityPlayer player){
		return player.inventory.clearMatchingItems(item, meta, 0, null) >= (getAmount() == 0 ? 1 : getAmount());
	}
	
	public void take(EntityPlayer player){
		player.inventory.clearMatchingItems(item, meta, getAmount(), null);
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public NBTTagCompound data(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("itemType", String.valueOf(ForgeRegistries.ITEMS.getKey(item)));
		compound.setInteger("meta", meta);
		return compound;
	}
	
	public Item getItem(){
		return item;
	}
	
	public ItemStack getStack(){
		return stack == null ? stack = (meta == -1 ? new ItemStack(getItem()) : new ItemStack(getItem(), 1, meta)) : stack;
	}
}