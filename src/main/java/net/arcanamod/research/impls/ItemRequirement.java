package net.arcanamod.research.impls;

import net.arcanamod.Arcana;
import net.arcanamod.research.Requirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemRequirement extends Requirement{
	
	// perhaps support NBT in the future? will be required for enchantments in the future at least.
	protected Item item;
	protected ItemStack stack;
	protected int meta = -1;
	
	public static final ResourceLocation TYPE = new ResourceLocation(Arcana.MODID, "item");
	
	public ItemRequirement(Item item){
		this.item = item;
	}
	
	public ItemRequirement setMeta(int meta){
		this.meta = meta;
		return this;
	}
	
	public boolean satisfied(PlayerEntity player){
		return player.inventory.clearMatchingItems(item, meta, 0, null) >= (getAmount() == 0 ? 1 : getAmount());
	}
	
	public void take(PlayerEntity player){
		player.inventory.clearMatchingItems(item, meta, getAmount(), null);
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public CompoundNBT data(){
		CompoundNBT compound = new CompoundNBT();
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