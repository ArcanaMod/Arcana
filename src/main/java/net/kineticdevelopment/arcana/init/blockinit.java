package net.kineticdevelopment.arcana.init;

import java.util.ArrayList;
import java.util.List;

import net.kineticdevelopment.arcana.common.blocks.AmberBlock;
import net.kineticdevelopment.arcana.common.blocks.AmberBrick;
import net.kineticdevelopment.arcana.common.blocks.AmberOre;
import net.kineticdevelopment.arcana.common.blocks.ArcaneStone;
import net.kineticdevelopment.arcana.common.blocks.ArcaneStoneBricks;
import net.kineticdevelopment.arcana.common.blocks.Cinnabar;
import net.kineticdevelopment.arcana.common.blocks.GreatwoodLeaves;
import net.kineticdevelopment.arcana.common.blocks.GreatwoodLog;
import net.kineticdevelopment.arcana.common.blocks.GreatwoodPlanks;
import net.kineticdevelopment.arcana.common.blocks.GreatwoodSapling;
import net.kineticdevelopment.arcana.common.blocks.InfusionStone;
import net.kineticdevelopment.arcana.common.blocks.SilverwoodLeaves;
import net.kineticdevelopment.arcana.common.blocks.SilverwoodLog;
import net.kineticdevelopment.arcana.common.blocks.SilverwoodPlanks;
import net.kineticdevelopment.arcana.common.blocks.SilverwoodSapling;
import net.kineticdevelopment.arcana.common.blocks.TaintedLeaves;
import net.kineticdevelopment.arcana.common.blocks.TaintedLog;
import net.kineticdevelopment.arcana.common.blocks.TaintedPlanks;
import net.kineticdevelopment.arcana.common.blocks.TaintedSapling;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class blockinit 
{
    public static final List<Block> BLOCKS = new ArrayList<Block>();
    
    public static final Block ARCANESTONE = new ArcaneStone("arcane_stone", Material.ROCK).setCreativeTab(Main.tabArcana);
    
    public static final Block ARCANESTONEBRICKS = new ArcaneStoneBricks("arcane_stone_bricks", Material.ROCK).setCreativeTab(Main.tabArcana);
    
    public static final Block GREATWOODSAPLING = new GreatwoodSapling("greatwood_sapling", Material.PLANTS).setCreativeTab(Main.tabArcana);
    
    public static final Block GREATWOODLOG = new GreatwoodLog("greatwood_log", Material.WOOD).setCreativeTab(Main.tabArcana);
    
    public static final Block GREATWOODPLANKS = new GreatwoodPlanks("greatwood_planks", Material.WOOD).setCreativeTab(Main.tabArcana);
    
    public static final Block GREATWOODLEAVES = new GreatwoodLeaves("greatwood_leaves", Material.LEAVES).setCreativeTab(Main.tabArcana);
    
    public static final Block AMBERBLOCK = new AmberBlock("amber_block", Material.GLASS).setCreativeTab(Main.tabArcana);
    
    public static final Block AMBERBRICK = new AmberBrick("amber_brick", Material.IRON).setCreativeTab(Main.tabArcana);
    
    public static final Block SILVERWOODSAPLING = new SilverwoodSapling("silverwood_sapling", Material.PLANTS).setCreativeTab(Main.tabArcana);
    
    public static final Block SILVERWOODLOG = new SilverwoodLog("silverwood_log", Material.WOOD).setCreativeTab(Main.tabArcana);
    
    public static final Block SILVERWOODPLANKS = new SilverwoodPlanks("silverwood_planks", Material.WOOD).setCreativeTab(Main.tabArcana);
    
    public static final Block SILVERWOODLEAVES = new SilverwoodLeaves("silverwood_leaves", Material.LEAVES).setCreativeTab(Main.tabArcana);
    
    public static final Block INFUSIONSTONE = new InfusionStone("infusion_stone", Material.IRON).setCreativeTab(Main.tabArcana);
    
    public static final Block AMBERORE = new AmberOre("amber_ore", Material.ROCK).setCreativeTab(Main.tabArcana);
    
    public static final Block CINNABAR = new Cinnabar("cinnabar", Material.ROCK).setCreativeTab(Main.tabArcana);
    
    public static final Block TAINTEDSAPLING= new TaintedSapling("tainted_sapling", Material.PLANTS).setCreativeTab(Main.tabArcana);
    
    public static final Block TAINTEDLOG = new TaintedLog("tainted_log", Material.WOOD).setCreativeTab(Main.tabArcana);
    
    public static final Block TAINTEDPLANKS = new TaintedPlanks("tainted_planks", Material.WOOD).setCreativeTab(Main.tabArcana);
    
    public static final Block TAINTEDLEAVES = new TaintedLeaves("tainted_leaves", Material.LEAVES).setCreativeTab(Main.tabArcana);
}
