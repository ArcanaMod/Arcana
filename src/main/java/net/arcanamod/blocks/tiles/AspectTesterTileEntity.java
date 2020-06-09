package net.arcanamod.blocks.tiles;

import net.arcanamod.aspects.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class AspectTesterTileEntity extends TileEntity {

	public AspectBattery battery = new AspectBattery(100);

	public AspectTesterTileEntity() {
		super(ArcanaTiles.ASPECT_TESTER.get());
		init();
	}

	private void init() {
		AspectCell cell = new AspectCell();
		cell.insert(new AspectStack(Aspect.EXCHANGE,16),false);
		battery.addCell(cell);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
		if (cap.equals(AspectHandlerCapability.ASPECT_HANDLER))
			return battery.getCapability(cap).cast();
		return null;
	}
}
