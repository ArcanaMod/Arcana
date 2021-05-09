package net.arcanamod.systems.research.impls;

import net.arcanamod.systems.research.Requirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import static net.arcanamod.Arcana.arcLoc;

public class ItemTagRequirement extends Requirement{
	
	protected ITag.INamedTag<Item> tag;
	
	public static final ResourceLocation TYPE = arcLoc("item_tag");
	
	public ItemTagRequirement(ITag.INamedTag<Item> tag){
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
		compound.putString("itemTag", tag.getName().toString());
		return compound;
	}
	
	public ITag.INamedTag<Item> getTag(){
		return tag;
	}
}