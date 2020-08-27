package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.client.render.aspects.AspectHelixParticleData;
import net.arcanamod.util.VisUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.aspects.Aspects.EMPTY;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlembicTileEntity extends TileEntity implements ITickableTileEntity{
	
	// 50 of 5 aspects
	public AspectBattery aspects = new AspectBattery(5, 50);
	int crucibleLevel = -1;
	boolean stacked = false;
	
	public boolean suppressedByRedstone = false;
	
	public static int maxAspectOut = ArcanaConfig.MAX_ALEMBIC_ASPECT_OUT.get();
	public static int maxAspectDistill = ArcanaConfig.MAX_ALEMBIC_ASPECT_DISTILL.get();
	
	public AlembicTileEntity(){
		super(ArcanaTiles.ALEMBIC_TE.get());
		// why isn't this done by default :(
		for(int i = 0; i < 5; i++)
			aspects.createCell(new AspectCell(50));
	}
	
	public void tick(){
		if(isOn() && world != null){
			// scan for boiling crucibles
			// 1-4 blocks of air, +3 of other alembics by default
			int maxAirs = ArcanaConfig.MAX_ALEMBIC_AIR.get(), maxAlembics = ArcanaConfig.MAX_ALEMBIC_STACK.get();
			// if alembics are present, don't show particles
			crucibleLevel = -1;
			boolean pocket = false;
			int stack = 0;
			int airs = -1;
			for(int i = getPos().getY() - 1; i > getPos().getY() - (maxAirs + maxAlembics + 1); i--){
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
				if(stack > maxAlembics)
					break;
				if(state.isAir(world, pos)){
					// up to 3 alembics + 4 air blocks
					if(airs > maxAirs)
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
				if(te != null && te.getAspectStackMap().size() > 0){
					Aspect aspect = EMPTY;
					// find an aspect stack we can actually pull
					IAspectHolder adding = null;
					for(IAspectHolder holder : aspects.getHolders()){
						if(holder.getCapacity() - holder.getCurrentVis() > 0){
							adding = holder;
							Aspect maybe = te.getAspectStackMap().values().stream().filter(stack1 -> stack1.getAspect() == holder.getContainedAspect() || holder.getContainedAspect() == Aspects.EMPTY).findFirst().map(AspectStack::getAspect).orElse(EMPTY);
							if(maybe != EMPTY){
								aspect = maybe;
								break;
							}
						}
					}
					
					if(aspect != EMPTY){
						AspectStack aspectStack = te.getAspectStackMap().get(aspect);
						if(!stacked)
							world.addParticle(new AspectHelixParticleData(aspect, 20 * airs + 15, world.rand.nextInt(180), new Vec3d(0, 1, 0)), cruciblePos.getX() + .5 + world.rand.nextFloat() * .1, cruciblePos.getY() + .7, cruciblePos.getZ() + .5 + world.rand.nextFloat() * .1, 0, 0, 0);
						// pick a random aspect, take from it, and store them in our actual aspect handler
						int diff = Math.min(aspectStack.getAmount(), maxAspectDistill);
						AspectStack newStack = new AspectStack(aspectStack.getAspect(), aspectStack.getAmount() - maxAspectDistill);
						if(!newStack.isEmpty())
							te.getAspectStackMap().put(aspect, newStack);
						else
							te.getAspectStackMap().remove(aspect);
						adding.insert(new AspectStack(aspectStack.getAspect(), diff), false);
					}
				}
				// then push them out into the total pipe system from sides
				for(Direction directions : Direction.Plane.HORIZONTAL){
					TileEntity tubeTe = world.getTileEntity(pos.offset(directions));
					if(tubeTe instanceof AspectTubeTileEntity){
						AspectTubeTileEntity aspectTube = (AspectTubeTileEntity)tubeTe;
						VisUtils.moveAllAspects(aspects, IAspectHandler.getFrom(aspectTube), maxAspectOut);
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