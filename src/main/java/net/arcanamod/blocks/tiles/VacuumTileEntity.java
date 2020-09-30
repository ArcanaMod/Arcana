package net.arcanamod.blocks.tiles;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public class VacuumTileEntity extends TileEntity implements ITickableTileEntity {

    int existTime = 0;
    int duration = Short.MAX_VALUE;
    private BlockState originBlock;

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
    public void read(CompoundNBT compoundNBT) {
        super.read(compoundNBT);
        duration = compoundNBT.getInt("Duration");
        existTime = compoundNBT.getInt("ExistTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compoundNBT) {
        compoundNBT.putInt("Duration",duration);
        compoundNBT.putInt("Duration",existTime);
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
            world.setBlockState(pos,originBlock);
        }
    }
}
