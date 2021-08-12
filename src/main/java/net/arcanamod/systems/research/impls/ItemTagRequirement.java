package net.arcanamod.systems.research.impls;

import net.arcanamod.systems.research.Requirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import static net.arcanamod.Arcana.arcLoc;

public class ItemTagRequirement extends Requirement{
	
	protected ITag<Item> tag;
	protected ResourceLocation tagName;
	
	public static final ResourceLocation TYPE = arcLoc("item_tag");
	
	public ItemTagRequirement(ResourceLocation tagName){
		this(ItemTags.getCollection().get(tagName), tagName);
	}
	
	public ItemTagRequirement(ITag<Item> tag, ResourceLocation tagName){
		this.tag = tag;
	}
	
	public boolean satisfied(PlayerEntity player){
		return player.inventory.func_234564_a_(x -> x.getItem().isIn(tag), 0, player.container.func_234641_j_()) >= (getAmount() == 0 ? 1 : getAmount());
	}
	
	public void take(PlayerEntity player){
		player.inventory.func_234564_a_(x -> x.getItem().isIn(tag), getAmount(), player.container.func_234641_j_());
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public CompoundNBT data(){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("itemTag", tagName.toString());
		return compound;
	}
	
	public ITag<Item> getTag(){
		return tag;
	}
	
	public ResourceLocation getTagName(){
		return tagName;
	}
}