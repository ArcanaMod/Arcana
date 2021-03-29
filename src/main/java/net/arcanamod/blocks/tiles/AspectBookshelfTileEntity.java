package net.arcanamod.blocks.tiles;

import net.arcanamod.aspects.VisShareable;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class AspectBookshelfTileEntity extends TileEntity implements ITickableTileEntity, VisShareable {
	public AspectBookshelfTileEntity() {
		super(ArcanaTiles.ASPECT_SHELF_TE.get());
	}

	@Override
	public boolean isVisShareable() {
		return false;
	}

	@Override
	public boolean isManual() {
		return false;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public void tick() {

	}
}