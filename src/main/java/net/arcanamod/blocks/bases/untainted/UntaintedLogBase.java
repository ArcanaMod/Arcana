package net.arcanamod.blocks.bases.untainted;

import net.arcanamod.util.IHasModel;
import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaProperties;
import net.arcanamod.blocks.bases.LogBase;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

/**
 * Basic Untainted Log, all untainted log block should either be this, or extend it
 *
 * @author Mozaran
 * @see LogBase
 */
public class UntaintedLogBase extends LogBase implements IHasModel{
	public static final PropertyBool FULLYTAINTED = ArcanaProperties.FULLYTAINTED;
	
	public UntaintedLogBase(String name){
		super(name);
		this.setDefaultState(this.getStateFromMeta(0));
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		int i = 0;
		boolean tainted = state.getValue(FULLYTAINTED);
		EnumAxis enumfacing$axis = state.getValue(LOG_AXIS);
		
		// Log Axis Variations are even
		if(enumfacing$axis == EnumAxis.X){
			i = 2;
		}else if(enumfacing$axis == EnumAxis.Z){
			i = 4;
		}else if(enumfacing$axis == EnumAxis.NONE){
			i = 6;
		}
		
		// Adding Fully Tainted Variants are Odd
		if(tainted){
			i += 1;
		}
		
		return i;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		EnumAxis enumfacing$axis = EnumAxis.Y;
		boolean tainted;
		int i = meta;
		
		// Axis's are Even and Fully Tainted Variants are Odd
		if(i < 2){
			tainted = (i % 2 == 1);
		}else if(i < 4){
			enumfacing$axis = EnumAxis.X;
			tainted = (i % 2 == 1);
		}else if(i < 6){
			enumfacing$axis = EnumAxis.Z;
			tainted = (i % 2 == 1);
		}else{
			enumfacing$axis = EnumAxis.NONE;
			tainted = (i % 2 == 1);
		}
		
		return this.getDefaultState().withProperty(FULLYTAINTED, tainted).withProperty(LOG_AXIS, enumfacing$axis);
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, FULLYTAINTED, LOG_AXIS);
	}
	
	@Override
	public void registerModels(){
		Arcana.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
