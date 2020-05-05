package net.arcanamod.items.tools;

import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.IHasModel;
import net.minecraft.item.SwordItem;

import static net.arcanamod.Arcana.TAB_ARCANA;

/**
 * Basic Sword, all Swords should either use this or extends this
 *
 * @author Wilkon
 */

public class SwordBase extends SwordItem implements IHasModel{
	public SwordBase(String name, ToolMaterial material){
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
