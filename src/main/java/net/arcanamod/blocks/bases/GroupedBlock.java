package net.arcanamod.blocks.bases;

import net.minecraft.item.ItemGroup;

import javax.annotation.Nullable;

public interface GroupedBlock{
	
	@Nullable
	ItemGroup getGroup();
}