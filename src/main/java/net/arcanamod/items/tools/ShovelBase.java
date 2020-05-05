package net.arcanamod.items.tools;

import net.arcanamod.Arcana;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.IHasModel;
import net.minecraft.item.ShovelItem;

import static net.arcanamod.Arcana.TAB_ARCANA;

/**
 * Basic Shovel, all Shovels should either use this or extends this
 *
 * @author Wilkon
 */

public class ShovelBase extends ShovelItem implements IHasModel{
	public ShovelBase(String name, ToolMaterial material){
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
