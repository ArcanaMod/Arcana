package net.arcanamod.blocks.tiles;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class AspectBookshelfTileEntity extends TileEntity implements ITickableTileEntity
{
	public AspectBookshelfTileEntity() {
		super(ArcanaTiles.ASPECT_SHELF_TE.get());
	}

	@Override
	public void tick() {

	}
}
