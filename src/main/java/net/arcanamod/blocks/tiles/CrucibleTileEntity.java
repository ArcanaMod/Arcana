package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.ItemAspectRegistry;
import net.arcanamod.blocks.CrucibleBlock;
import net.arcanamod.util.StreamUtils;
import net.arcanamod.world.AuraView;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.Constants;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

import static net.arcanamod.blocks.CrucibleBlock.FULL;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrucibleTileEntity extends TileEntity implements ITickableTileEntity{
	
	// Not an aspect handler; cannot be drawn directly from, and has infinite size
	// Should decay or something - to avoid very large NBT, at least.
	List<AspectStack> aspectStacks = new ArrayList<>();
	boolean boiling = false;
	
	public CrucibleTileEntity(){
		super(ArcanaTiles.CRUCIBLE_TE.get());
	}
	
	public List<AspectStack> getAspectStacks(){
		return aspectStacks;
	}
	
	public void tick(){
		BlockState below = world.getBlockState(pos.down());
		IFluidState fluidState = world.getFluidState(pos.down());
		boiling = hasWater() && (below.getBlock() == Blocks.FIRE || below.getBlock() == Blocks.MAGMA_BLOCK || fluidState.getFluid() == Fluids.FLOWING_LAVA || fluidState.getFluid() == Fluids.LAVA);
		
		// check for items
		// if there are items that have aspects, boil them :)
		if(isBoiling()){
			List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, CrucibleBlock.INSIDE.getBoundingBox().offset(getPos()));
			for(ItemEntity item : items){
				ItemStack stack = item.getItem();
				List<AspectStack> aspects = ItemAspectRegistry.get(stack);
				if(aspects.size() > 0){
					item.remove();
					world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
					markDirty();
					for(AspectStack aspect : aspects)
						aspectStacks.add(new AspectStack(aspect.getAspect(), aspect.getAmount() * stack.getCount()));
					aspectStacks = StreamUtils.partialReduce(aspectStacks, AspectStack::getAspect, (left, right) -> new AspectStack(left.getAspect(), left.getAmount() + right.getAmount()));
				}
			}
		}
	}
	
	public void empty(){
		// release aspects as flux :)
		// half as much as there are aspects
		AuraView.SIDED_FACTORY.apply(world).addTaintAt(getPos(), aspectStacks.stream().mapToInt(AspectStack::getAmount).sum() / 2);
		aspectStacks.clear();
		// block handles changing state
	}
	
	private boolean hasWater(){
		return getWorld().getBlockState(getPos()).get(FULL);
	}
	
	public boolean isBoiling(){
		return boiling;
	}
	
	public void read(CompoundNBT compound){
		super.read(compound);
		ListNBT aspects = compound.getList("aspects", Constants.NBT.TAG_COMPOUND);
		for(INBT inbt : aspects){
			CompoundNBT aspectNbt = ((CompoundNBT)inbt);
			Aspect aspect = AspectUtils.getAspectByName(aspectNbt.getString("aspect"));
			int amount = aspectNbt.getInt("amount");
			aspectStacks.add(new AspectStack(aspect, amount));
		}
	}
	
	public CompoundNBT write(CompoundNBT compound){
		super.write(compound);
		ListNBT aspects = new ListNBT();
		for(AspectStack stack : aspectStacks){
			CompoundNBT stackNbt = new CompoundNBT();
			stackNbt.putString("aspect", stack.getAspect().name());
			stackNbt.putInt("amount", stack.getAmount());
			aspects.add(stackNbt);
		}
		compound.put("aspects", aspects);
		return compound;
	}
	
	public CompoundNBT getUpdateTag(){
		return write(new CompoundNBT());
	}
	
	public void handleUpdateTag(CompoundNBT tag){
		read(tag);
	}
}