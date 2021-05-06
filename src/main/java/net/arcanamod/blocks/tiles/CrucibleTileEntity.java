package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.ItemAspectRegistry;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.CrucibleBlock;
import net.arcanamod.items.recipes.AlchemyInventory;
import net.arcanamod.items.recipes.AlchemyRecipe;
import net.arcanamod.world.AuraView;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.Constants;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.arcanamod.blocks.CrucibleBlock.FULL;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrucibleTileEntity extends TileEntity implements ITickableTileEntity{
	
	// Not an aspect handler; cannot be drawn directly from, and has infinite size
	// Should decay or something - to avoid very large NBT, at least.
	Map<Aspect, AspectStack> aspectStackMap = new HashMap<>();
	boolean boiling = false;
	
	public CrucibleTileEntity(){
		super(ArcanaTiles.CRUCIBLE_TE.get());
	}
	
	public Map<Aspect, AspectStack> getAspectStackMap(){
		return aspectStackMap;
	}
	
	public void tick(){
		BlockState below = world.getBlockState(pos.down());
		FluidState fluidState = world.getFluidState(pos.down());
		// TODO: use a block+fluid tag
		boiling = hasWater() && (below.getBlock() == Blocks.FIRE || below.getBlock() == Blocks.MAGMA_BLOCK || below.getBlock() == ArcanaBlocks.NITOR.get() || fluidState.getFluid() == Fluids.FLOWING_LAVA || fluidState.getFluid() == Fluids.LAVA);
		
		// check for items
		// if there are items that have aspects, boil them :)
		if(!world.isRemote() && isBoiling()){
			List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, CrucibleBlock.INSIDE.getBoundingBox().offset(getPos()));
			for(ItemEntity item : items){
				ItemStack stack = item.getItem();
				// check if that item makes a recipe
				// requires a player; droppers and such will never craft alchemy recipes
				// yet; we could make a knowledgeable dropper or such later
				boolean melt = true;
				if(item.getThrowerId() != null && world.getPlayerByUuid(item.getThrowerId()) != null){
					PlayerEntity thrower = world.getPlayerByUuid(item.getThrowerId());
					AlchemyInventory inventory = new AlchemyInventory(this, thrower);
					inventory.setInventorySlotContents(0, stack);
					Optional<AlchemyRecipe> optionalRecipe = world.getRecipeManager().getRecipe(AlchemyRecipe.ALCHEMY, inventory, world);
					if(optionalRecipe.isPresent()){
						melt = false;
						AlchemyRecipe recipe = optionalRecipe.get();
						if(stack.getCount() == 1)
							item.remove();
						else
							stack.shrink(1);
						ItemStack result = recipe.getCraftingResult(inventory);
						if(!thrower.addItemStackToInventory(result))
							thrower.dropItem(result, false);
						for(AspectStack aspectStack : recipe.getAspects()){
							Aspect aspect = aspectStack.getAspect();
							AspectStack newStack = new AspectStack(aspect, aspectStackMap.get(aspect).getAmount() - aspectStack.getAmount());
							if(!newStack.isEmpty())
								aspectStackMap.put(aspect, newStack);
							else
								aspectStackMap.remove(aspect);
						}
						markDirty();
						world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
					}
				}
				if(melt){
					List<AspectStack> aspects = ItemAspectRegistry.get(stack);
					if(aspects.size() > 0){
						item.remove();
						world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
						markDirty();
						world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
						for(AspectStack aspect : aspects)
							if(aspect.getAmount() != 0)
								aspectStackMap.put(aspect.getAspect(), new AspectStack(aspect.getAspect(), aspect.getAmount() * stack.getCount() + (aspectStackMap.containsKey(aspect.getAspect()) ? aspectStackMap.get(aspect.getAspect()).getAmount() : 0)));
					}
				}
			}
		}
	}
	
	public void empty(){
		// release aspects as flux
		AuraView.SIDED_FACTORY.apply(world).addFluxAt(getPos(), (float)(aspectStackMap.values().stream().mapToDouble(AspectStack::getAmount).sum() * ArcanaConfig.ASPECT_DUMPING_WASTE.get()));
		aspectStackMap.clear();
		// block handles changing state
	}
	
	private boolean hasWater(){
		return getWorld().getBlockState(getPos()).get(FULL);
	}
	
	public boolean isBoiling(){
		return boiling;
	}
	
	public void read(BlockState state, CompoundNBT compound){
		super.read(state, compound);
		ListNBT aspects = compound.getList("aspects", Constants.NBT.TAG_COMPOUND);
		aspectStackMap.clear();
		for(INBT inbt : aspects){
			CompoundNBT aspectNbt = ((CompoundNBT)inbt);
			Aspect aspect = AspectUtils.getAspectByName(aspectNbt.getString("aspect"));
			int amount = aspectNbt.getInt("amount");
			aspectStackMap.put(aspect, new AspectStack(aspect, amount));
		}
	}
	
	public CompoundNBT write(CompoundNBT compound){
		super.write(compound);
		ListNBT aspects = new ListNBT();
		for(AspectStack stack : aspectStackMap.values()){
			CompoundNBT stackNbt = new CompoundNBT();
			stackNbt.putString("aspect", stack.getAspect().name());
			stackNbt.putFloat("amount", stack.getAmount());
			aspects.add(stackNbt);
		}
		compound.put("aspects", aspects);
		return compound;
	}
	
	public CompoundNBT getUpdateTag(){
		return write(new CompoundNBT());
	}
	
	public void handleUpdateTag(BlockState state, CompoundNBT tag){
		read(state, tag);
	}
	
	public SUpdateTileEntityPacket getUpdatePacket(){
		return new SUpdateTileEntityPacket(pos, -1, getUpdateTag());
	}
	
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
		handleUpdateTag(getBlockState(), pkt.getNbtCompound());
	}
}