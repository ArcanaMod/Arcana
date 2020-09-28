package net.arcanamod.blocks.tiles;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public class VacuumTileEntity extends TileEntity {
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
}
