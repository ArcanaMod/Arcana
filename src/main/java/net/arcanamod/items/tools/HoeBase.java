package net.arcanamod.items.tools;

import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.IHasModel;
import net.minecraft.item.HoeItem;

import static net.arcanamod.Arcana.TAB_ARCANA;

/**
 * Basic Hoe, all hoes should either use this or extends this
 * <p>
 * This is a basic hoe, get yourself a nice girl instead.
 *
 * @author Wilkon
 */

public class HoeBase extends HoeItem implements IHasModel{
	public HoeBase(String name, ToolMaterial material){
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
