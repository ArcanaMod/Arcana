package net.kineticdevelopment.arcana.common.objects.blocks;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.common.items.ItemAttachment;
import net.kineticdevelopment.arcana.common.objects.blocks.bases.BlockBase;
import net.kineticdevelopment.arcana.common.objects.items.ItemWand;
import net.kineticdevelopment.arcana.common.objects.tile.NodeTileEntity;
import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.wand.CapType;
import net.kineticdevelopment.arcana.core.wand.CoreType;
import net.kineticdevelopment.arcana.core.wand.EnumAttachmentType;
import net.kineticdevelopment.arcana.utilities.WandUtil;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Barrier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opencl.CL;

import javax.annotation.Nullable;
import javax.xml.soap.Node;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BlockNormalNode extends BlockBase implements ITileEntityProvider {


    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.25d, 0.25d, 0.25d, 0.75d, 0.75d, 0.75d);

    public boolean isOn = true;

    public BlockNormalNode() {
        super("normal_node", Material.BARRIER);
        this.translucent = true;

    }

    public void hitboxOff() {
        isOn = false;
    }

    public void hitboxOn() {
        isOn = true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        NodeTileEntity entity = new NodeTileEntity();
        entity.storedAspects.put(Aspect.AspectType.EARTH, 100);
        entity.storedAspects.put(Aspect.AspectType.AIR, 100);
        entity.markDirty();
        return entity;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return isOn ? BOUNDING_BOX : new AxisAlignedBB(0,0,0,0,0,0);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }


    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getAmbientOcclusionLightValue(IBlockState state) {
        return 1.0f;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        super.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0, 0, 0, 0, 0, 0));
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        ItemStack itemActivated = playerIn.getHeldItem(hand);
        CoreType core = WandUtil.getCore(itemActivated);
        CapType cap = WandUtil.getCap(itemActivated);
        if(itemActivated.getItem() instanceof ItemWand) {
            TileEntity entity = worldIn.getTileEntity(pos);
            if(entity instanceof NodeTileEntity) {
                NodeTileEntity tileEntity = (NodeTileEntity) entity;
                Aspect.AspectType[] coreAspects = new Aspect.AspectType[]{Aspect.AspectType.EARTH, Aspect.AspectType.AIR, Aspect.AspectType.WATER, Aspect.AspectType.ORDER, Aspect.AspectType.CHAOS, Aspect.AspectType.FIRE};
                NBTTagList aspectList = itemActivated.getTagCompound().getTagList("aspects", Constants.NBT.TAG_COMPOUND);
                if(!itemActivated.getTagCompound().hasKey("aspects")) {
                    aspectList = new NBTTagList();
                }
                for(Aspect.AspectType coreAspect : coreAspects) {
                    if(tileEntity.storedAspects.containsKey(coreAspect)) {
                        if(aspectList.hasNoTags()) {
                            tileEntity.storedAspects.replace(coreAspect, tileEntity.storedAspects.get(coreAspect) - 1);
                            NBTTagCompound compound = new NBTTagCompound();
                            compound.setString("type", coreAspect.toString());
                            compound.setInteger("amount", 1);
                            aspectList.appendTag(compound);
                        }
                        Iterator<NBTBase> iter = aspectList.iterator();
                        while (iter.hasNext()) {
                            if(iter.next() instanceof NBTTagCompound) {
                                NBTTagCompound compound = (NBTTagCompound) iter.next();
                                if(coreAspect.toString().equals(compound.getString("type"))) {
                                    if(compound.getInteger("amount") < core.getMaxVis()) {
                                        compound.setInteger("amount", compound.getInteger("amount") + 1);
                                        iter.remove();
                                        aspectList.appendTag(compound);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
