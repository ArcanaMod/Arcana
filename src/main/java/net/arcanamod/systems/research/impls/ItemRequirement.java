package net.arcanamod.systems.research.impls;

import net.arcanamod.systems.research.Requirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.arcLoc;

public class ItemRequirement extends Requirement{
	
	// perhaps support NBT in the future? will be required for enchantments in the future at least.
	protected Item item;
	protected ItemStack stack;
	
	public static final ResourceLocation TYPE = arcLoc("item");
	
	public ItemRequirement(Item item){
		this.item = item;
	}
	
	public boolean satisfied(PlayerEntity player){
		return player.inventory.func_234564_a_(x -> x.getItem() == item, 0, player.container.func_234641_j_()) >= (getAmount() == 0 ? 1 : getAmount());
	}
	
	public void take(PlayerEntity player){
		player.inventory.func_234564_a_(x -> x.getItem() == item, getAmount(), player.container.func_234641_j_());
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public CompoundNBT data(){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("itemType", String.valueOf(ForgeRegistries.ITEMS.getKey(item)));
		return compound;
	}
	
	public Item getItem(){
		return item;
	}
	
	public ItemStack getStack(){
		if(stack == null)
			return stack = new ItemStack(getItem());
		return stack;
	}
}