package net.arcanamod.blocks.tiles;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class VacuumTileEntity extends TileEntity implements ITickableTileEntity {

    int existTime = 0;
    int duration = Short.MAX_VALUE;
    private BlockState originBlock = null;

    public VacuumTileEntity() {
        super(ArcanaTiles.VACUUM_TE.get());
    }

    public boolean shouldRenderFace(Direction p_228884_15_) {
        BlockState block = world.getBlockState(pos.offset(p_228884_15_,-1));
        return block.getBlock() != Blocks.AIR
                && block.getBlock() != Blocks.CAVE_AIR
                && block.getBlock() != Blocks.VOID_AIR
                && block.getBlock() != ArcanaBlocks.VACUUM_BLOCK.get();
    }

    @Override
    public void read(BlockState state, CompoundNBT compoundNBT) {
        super.read(state, compoundNBT);
        duration = compoundNBT.getInt("Duration");
        existTime = compoundNBT.getInt("ExistTime");
        originBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compoundNBT.getString("Block"))).getDefaultState();
    }

    @Override
    public CompoundNBT write(CompoundNBT compoundNBT) {
        compoundNBT.putInt("Duration",duration);
        compoundNBT.putInt("ExistTime",existTime);
        compoundNBT.putString("Block",originBlock.getBlock().getRegistryName().toString());
        return super.write(compoundNBT);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setOriginBlock(BlockState originBlock) {
        this.originBlock = originBlock;
    }

    @Override
    public void tick() {
        existTime++;
        if (existTime >= duration) {
            if (originBlock != null)
                world.setBlockState(pos,originBlock);
            else world.setBlockState(pos,Blocks.AIR.getDefaultState());
        }
    }
}
