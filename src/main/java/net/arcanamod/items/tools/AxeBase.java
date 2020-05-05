package net.arcanamod.items.tools;

import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.IHasModel;
import net.minecraft.item.ItemAxe;

import static net.arcanamod.Arcana.TAB_ARCANA;

/**
 * Basic Axe, all Axes should either use this or extends this
 *
 * @author Wilkon
 */

public class AxeBase extends ItemAxe implements IHasModel{
	public AxeBase(String name, ToolMaterial material){
		super(material, 9.0F, -3.2F);
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
