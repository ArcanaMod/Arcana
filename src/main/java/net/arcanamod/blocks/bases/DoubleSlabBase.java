package net.arcanamod.blocks.bases;

import net.minecraft.block.material.Material;

/**
 * Basic Double Slabs, all double slabs should either be this, or extend it
 *
 * @author Tea, Mozaran
 */
public class DoubleSlabBase extends SlabBase{
	
	public DoubleSlabBase(String name, Material material){
		super(name, material);
	}
	
	@Override
	public boolean isDouble(){
		return true;
	}
}
