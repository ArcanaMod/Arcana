package net.arcanamod.blocks.bases;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;

public class GroupedBlock extends Block{
	
	private final ItemGroup group;
	
	public GroupedBlock(Properties properties, ItemGroup group){
		super(properties);
		this.group = group;
	}
	
	public ItemGroup getGroup(){
		return group;
	}
}