package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.AspectBattery;
import net.arcanamod.aspects.AspectHandlerCapability;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlembicTileEntity extends TileEntity{
	
	// 50 of 5 aspects
	public AspectBattery aspects = new AspectBattery(5, 50);
	
	public AlembicTileEntity(){
		super(ArcanaTiles.ALEMBIC_TE.get());
	}
	
	public void read(CompoundNBT compound){
		super.read(compound);
		aspects.deserializeNBT(compound.getCompound("aspects"));
	}
	
	public CompoundNBT write(CompoundNBT compound){
		CompoundNBT nbt = super.write(compound);
		nbt.put("aspects", aspects.serializeNBT());
		return nbt;
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
		if(cap == AspectHandlerCapability.ASPECT_HANDLER)
			return aspects.getCapability(AspectHandlerCapability.ASPECT_HANDLER).cast();
		return LazyOptional.empty();
	}
}