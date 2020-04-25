package net.kineticdevelopment.arcana.common.objects.blocks.bases;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Basic Planks, all planks should either be this, or extend it
 *
 * @author Tea
 *
 */
public class PlanksBase extends BlockBase {

    public PlanksBase(String name, Material material) {
        super(name, material);

        setSoundType(SoundType.WOOD);
        setHardness(2.0f);
        setResistance(2.0f);
        setHarvestLevel("axe",0);
        OreDictionary.registerOre("plankWood", this);
    }
}


