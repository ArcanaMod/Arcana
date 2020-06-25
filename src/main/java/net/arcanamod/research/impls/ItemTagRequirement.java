package net.arcanamod.research.impls;

import net.arcanamod.research.Requirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.arcLoc;

public class ItemTagRequirement extends Requirement{
	
	protected Tag<Item> tag;
	
	public static final ResourceLocation TYPE = arcLoc("item_tag");
	
	public ItemTagRequirement(Tag<Item> tag){
		this.tag = tag;
	}
	
	public boolean satisfied(PlayerEntity player){
		return player.inventory.clearMatchingItems(x -> x.getItem().isIn(tag), 0) >= (getAmount() == 0 ? 1 : getAmount());
	}
	
	public void take(PlayerEntity player){
		player.inventory.clearMatchingItems(x -> x.getItem().isIn(tag), getAmount());
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public CompoundNBT data(){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("itemTag", tag.getId().toString());
		return compound;
	}
	
	public Tag<Item> getTag(){
		return tag;
	}
}