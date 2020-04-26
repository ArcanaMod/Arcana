package net.kineticdevelopment.arcana.common.objects.blocks.bases;

import net.kineticdevelopment.arcana.common.blocks.OreDictEntry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Basic Planks, all planks should either be this, or extend it
 *
 * @author Tea
 *
 */
public class PlanksBase extends BlockBase implements OreDictEntry{

    public PlanksBase(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.WOOD);
        setHardness(2.0f);
        setResistance(2.0f);
        setHarvestLevel("axe",0);
    }
    
    public String getOreDictName(){
        return "plankWood";
    }
}