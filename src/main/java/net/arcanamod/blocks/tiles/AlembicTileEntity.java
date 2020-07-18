package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.client.render.AspectHelixParticleData;
import net.arcanamod.util.VisUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlembicTileEntity extends TileEntity implements ITickableTileEntity{
	
	// 50 of 5 aspects
	public AspectBattery aspects = new AspectBattery(5, 50);
	int crucibleLevel = -1;
	boolean stacked = false;
	
	public boolean suppressedByRedstone = false;
	
	public static final int MAX_ASPECT_OUT = 3;
	public static final int MAX_ASPECT_DISTILL = 2;
	
	public AlembicTileEntity(){
		super(ArcanaTiles.ALEMBIC_TE.get());
		// why isn't this done by default :(
		for(int i = 0; i < 5; i++)
			aspects.createCell(new AspectCell(50));
	}
	
	public void tick(){
		if(isOn()){
			// scan for boiling crucibles
			// 1-4 blocks of air, +3 of other alembics
			// if alembics are present, don't show particles
			crucibleLevel = -1;
			boolean pocket = false;
			int stack = 0;
			int airs = -1;
			for(int i = getPos().getY() - 1; i > getPos().getY() - 8; i--){
				int passes = (getPos().getY() - 1) - i;
				BlockPos pos = new BlockPos(getPos().getX(), i, getPos().getZ());
				BlockState state = world.getBlockState(pos);
				airs = passes - stack;
				if(state.getBlock() == ArcanaBlocks.ALEMBIC.get()){
					// air followed by alembic is invalid
					if(pocket)
						break;
					stack++;
				}
				// only three alembics
				if(stack > 3)
					break;
				if(state.isAir(world, pos)){
					// up to 3 alembics + 4 air blocks
					if(airs > 4)
						break;
					pocket = true;
				}
				if(world.getTileEntity(pos) instanceof CrucibleTileEntity){
					// found it
					// if we haven't gotten any air, don't save (it's invalid)
					if(airs > 0)
						crucibleLevel = i;
					break;
				}
			}
			stacked = stack > 0;
			if(crucibleLevel != -1){
				BlockPos cruciblePos = new BlockPos(getPos().getX(), crucibleLevel, getPos().getZ());
				CrucibleTileEntity te = (CrucibleTileEntity)world.getTileEntity(cruciblePos);
				if(te.getAspectStacks().size() > 0){
					int index = -1;
					// find an aspect stack we can actually pull
					IAspectHolder adding = null;
					for(IAspectHolder holder : aspects.getHolders()){
						if(holder.getCapacity() - holder.getCurrentVis() > 0){
							adding = holder;
							int maybe = te.getAspectStacks().indexOf(te.getAspectStacks().stream().filter(stack1 -> stack1.getAspect() == holder.getContainedAspect() || holder.getContainedAspect() == Aspects.EMPTY).findFirst().orElse(null));
							if(maybe != -1){
								index = maybe;
								break;
							}
						}
					}
					
					if(index != -1){
						AspectStack aspectStack = te.getAspectStacks().get(index);
						if(!stacked){
							Aspect aspect = aspectStack.getAspect();
							world.addParticle(new AspectHelixParticleData(aspect, 20 * airs + 15, world.rand.nextInt(180)), cruciblePos.getX() + .5 + world.rand.nextFloat() * .1, cruciblePos.getY() + .7, cruciblePos.getZ() + .5 + world.rand.nextFloat() * .1, 0, 0, 0);
						}
						// pick a random aspect, take from it, and store them in our actual aspect handler
						int diff = Math.min(aspectStack.getAmount(), MAX_ASPECT_DISTILL);
						AspectStack newStack = new AspectStack(aspectStack.getAspect(), aspectStack.getAmount() - MAX_ASPECT_DISTILL);
						if(!newStack.isEmpty())
							te.getAspectStacks().set(index, newStack);
						else
							te.getAspectStacks().remove(index);
						adding.insert(new AspectStack(aspectStack.getAspect(), diff), false);
					}
				}
				// then push them out into the total pipe system from sides
				for(Direction directions : Direction.Plane.HORIZONTAL){
					TileEntity tubeTe = world.getTileEntity(pos.offset(directions));
					if(tubeTe instanceof AspectTubeTileEntity){
						AspectTubeTileEntity aspectTube = (AspectTubeTileEntity)tubeTe;
						VisUtils.moveAllAspects(aspects, IAspectHandler.getFrom(aspectTube), MAX_ASPECT_OUT);
						break;
					}
				}
				// aspects can be pulled from the top when pulling becomes a thing but that doesn't matter here
			}
		}
	}
	
	public boolean isOn(){
		return !suppressedByRedstone;
	}
	
	public void read(CompoundNBT compound){
		super.read(compound);
		aspects.deserializeNBT(compound.getCompound("aspects"));
		suppressedByRedstone = compound.getBoolean("suppressed");
	}
	
	public CompoundNBT write(CompoundNBT compound){
		CompoundNBT nbt = super.write(compound);
		nbt.put("aspects", aspects.serializeNBT());
		nbt.putBoolean("suppressed", suppressedByRedstone);
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