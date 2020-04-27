package net.arcanamod.items.tools;

import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.IHasModel;
import net.minecraft.item.ItemPickaxe;

import static net.arcanamod.Arcana.TAB_ARCANA;

/**
 * Basic Pickaxe, all Pickaxes should either use this or extends this
 *
 * @author Wilkon
 */

public class PickaxeBase extends ItemPickaxe implements IHasModel{
	public PickaxeBase(String name, ToolMaterial material){
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(TAB_ARCANA);
		
		ArcanaItems.ITEMS.add(this);
	}
	
	@Override
	public void registerModels(){
		
		Arcana.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
