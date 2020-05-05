package net.arcanamod.blocks.bases.untainted;

import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.bases.StairsBase;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;

/**
 * Basic Untainted Stairs Block, all untainted stairs block should either be this, or extend it
 *
 * @author Mozaran
 * @see StairsBase
 */
public class UntaintedStairBase extends StairsBase implements IHasModel{
	public UntaintedStairBase(String name, BlockState state){
		super(name, state);
		this.setDefaultState(this.getDefaultState());
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
