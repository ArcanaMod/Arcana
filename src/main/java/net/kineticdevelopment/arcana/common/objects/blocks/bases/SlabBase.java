package net.kineticdevelopment.arcana.common.objects.blocks.bases;

import net.kineticdevelopment.arcana.common.init.BlockInit;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Slab Base. To be extended by double slab base and half slab base.
 *
 * @author Tea, Mozaran
 * @see HalfSlabBase
 * @see DoubleSlabBase
 */
public abstract class SlabBase extends BlockSlab {
    private String name;

    public SlabBase(String name, Material material) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        this.name = name;
        OreDictionary.registerOre("slabWood", this);

        IBlockState state = this.blockState.getBaseState();

        if(!this.isDouble()) {

            state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
        }

        setDefaultState(state);
        this.useNeighborBrightness = true;

        BlockInit.BLOCKS.add(this);
    }

    @Override
    public String getUnlocalizedName(int meta) {

        return this.getUnlocalizedName();
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return HALF;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return EnumBlockHalf.BOTTOM;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        if(!this.isDouble()) {
            return this.getDefaultState().withProperty(HALF, EnumBlockHalf.values()[meta % EnumBlockHalf.values().length]);
        }
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if(this.isDouble()) {
            return  0;
        }
        return ((EnumBlockHalf)state.getValue(HALF)).ordinal() +1;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(Main.MODID, name.replace("_double", "")));
        if(block != null) {
            Item item = Item.getItemFromBlock(block);
            drops.add(new ItemStack(item, isDouble() ? 2 : 1, this.damageDropped(state)));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {HALF});
    }
}
