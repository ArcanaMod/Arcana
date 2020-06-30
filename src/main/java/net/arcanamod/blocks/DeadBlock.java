package net.arcanamod.blocks;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;

import javax.annotation.Nullable;

public class DeadBlock extends DelegatingBlock implements GroupedBlock {

	@Deprecated() // Use Taint#deadOf instead
	public DeadBlock(Block block){
		super(block);
		Taint.addDeadMapping(block, this);
	}

	@Nullable
	@Override
	public ItemGroup getGroup(){
		return Arcana.TAINT;
	}
}